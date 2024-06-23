package dev.hust.simpleasia.service;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.entity.domain.Purchase;
import dev.hust.simpleasia.entity.domain.UserCredential;
import dev.hust.simpleasia.entity.dto.MerchantCustomerDto;
import dev.hust.simpleasia.entity.dto.PaymentResponse;
import dev.hust.simpleasia.entity.dto.StripeCheckoutRequest;
import dev.hust.simpleasia.port.RestTemplateClient;
import dev.hust.simpleasia.repository.PurchaseRepository;
import dev.hust.simpleasia.utils.CustomerUtils;
import dev.hust.simpleasia.utils.MerchantConstant;
import dev.hust.simpleasia.utils.PackageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    @Value("${spring.stripe.apiKey}")
    private String secretKey;
    @Value("${spring.clientUrl}")
    private String clientUrl;
    @Value("${spring.stripe.webhook.secretKey}")
    private String webhookSecretKey;

    private final RestTemplateClient restTemplateClient;
    private final PurchaseRepository purchaseRepository;
    private final MerchantCustomerService merchantCustomerService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Transactional
    public String checkoutMovie(StripeCheckoutRequest request) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findFirstByUserIdAndType(request.getUserId(), request.getType().name());
        if (purchaseOptional.isPresent()) throw new BusinessException("Already purchased the package");

        Optional<Purchase> currentPurchaseOptional = purchaseRepository.findFirstByUserId(request.getUserId());

        Long amount = getUnitAmount(request.getType());

        if (currentPurchaseOptional.isPresent()) {
            Purchase purchase = currentPurchaseOptional.get();

            amount = amount - getUnitAmount(PackageType.valueOf(purchase.getType()));
        }

        Customer customer = CustomerUtils.findOrCreateCustomer(request.getEmail(), request.getName());

        MerchantCustomerDto merchantCustomerDto = merchantCustomerService.findCustomer(customer.getId(), MerchantConstant.STRIPE);

        if (merchantCustomerDto == null) {
            MerchantCustomerDto newCustomer = MerchantCustomerDto.builder()
                    .userId(request.getUserId())
                    .email(request.getEmail())
                    .customerId(customer.getId())
                    .merchantName(MerchantConstant.STRIPE.name())
                    .build();

            merchantCustomerService.save(newCustomer);
        }

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomer(customer.getId())
                .setSuccessUrl(clientUrl + "/subscription")
                .setCancelUrl(clientUrl + "/subscription");

        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .putMetadata("email", request.getEmail())
                                                        .putMetadata("name", request.getName())
                                                        .putMetadata("package", request.getType().name())
                                                        .setName(request.getType().name() + " PACKAGE")
                                                        .build()
                                        )
                                        .setCurrency("USD")
                                        .setUnitAmount(amount)
                                        .build()
                        )
                        .build()
        );

        paramsBuilder.putMetadata("email", request.getEmail());
        paramsBuilder.putMetadata("userId", request.getUserId());
        paramsBuilder.putMetadata("package", request.getType().name());

        try {
            Session session = Session.create(paramsBuilder.build());
            session.getMetadata();
            return session.getUrl();

        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }

    }

    private Long getUnitAmount(PackageType type) {
        if (type.equals(PackageType.BASIC)) return 999L;
        if (type.equals(PackageType.STANDARD)) return 1299L;
        if (type.equals(PackageType.PREMIUM)) return 1499L;

        return 0L;
    }

    @Transactional
    public GeneralResponse<String> postEventsWebhook(String payload, String sigHeader) {
        log.info("Event Webhook payload: {}", payload);

        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecretKey);
        } catch (SignatureVerificationException e) {
            log.error("Failed signature verification: {}", e.getMessage(), e);
            throw new BusinessException("Failed signature verification");
        }

        StripeObject stripeObject = event.getData().getObject();

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) stripeObject;

            log.info("checkout.session.completed");
            String userId = session.getMetadata().get("userId");
            String type = session.getMetadata().get("package");
            String email = session.getMetadata().get("email");

            if (userId.isEmpty() || type.isEmpty() || email.isEmpty()) {
                throw new BusinessException("Missing metadata");
            }


            Purchase purchase = purchaseRepository.findFirstByUserId(userId).orElse(
                    Purchase.builder()
                            .userId(userId)
                            .type(type)
                            .build()
            );

            purchase.setType(type);
            purchaseRepository.save(purchase);
        }

        return GeneralResponse.success("Purchase successfully");
    }

    public GeneralResponse<PaymentResponse> getUserPayment(String userId) {
        GeneralResponse<UserCredential> generalResponse = restTemplateClient.get("http://localhost:8081/api/auth/detail?id={id}",
                new ParameterizedTypeReference<GeneralResponse<UserCredential>>() {
                },
                null,
                userId).getBody();

        Purchase purchase = purchaseRepository.findFirstByUserId(userId).orElse(null);

        assert generalResponse != null;
        return GeneralResponse.success(
                PaymentResponse.builder()
                        .userId(userId)
                        .userCredential(generalResponse.getData())
                        .purchase(purchase)
                        .build()
        );
    }

    public GeneralResponse<List<Long>> getRevenueOverview(String period) {
        if (period.equals("Month")) {
            return GeneralResponse.success(getMonthlyRevenue());

        } else if (period.equals("Week")) {
            return GeneralResponse.success(getWeeklyRevenue());
        }

        return GeneralResponse.error("Invalid input format", new BusinessException("Invalid input format"));
    }

    private List<Long> getWeeklyRevenue() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        Date startOfWeekDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfWeekDate = Date.from(endOfWeek.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        List<Purchase> purchases = purchaseRepository.findAllByDateRange(startOfWeekDate, endOfWeekDate);
        Map<DayOfWeek, Long> purchaseSummary = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek day : DayOfWeek.values()) {
            purchaseSummary.put(day, 0L);
        }

        for (Purchase purchase : purchases) {
            LocalDate purchaseDate = purchase.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DayOfWeek day = purchaseDate.getDayOfWeek();
            purchaseSummary.put(day, purchaseSummary.get(day) + PackageType.valueOf(purchase.getType()).getValue());
        }

        // Convert map values to Long[]
        List<Long> response = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            response.add(purchaseSummary.get(day));
        }

        return response;
    }

    private List<Long> getMonthlyRevenue() {
        List<Purchase> purchaseList = purchaseRepository.findAllByYear(Year.now().getValue());
        Map<Month, Long> monthlyPurchases = new EnumMap<>(Month.class);
        for (Month month : Month.values()) {
            monthlyPurchases.put(month, 0L);
        }

        for (Purchase purchase : purchaseList) {
            Month month = Month.of(purchase.getCreatedAt().getMonth());

            monthlyPurchases.put(month, monthlyPurchases.get(month) + PackageType.valueOf(purchase.getType()).getValue());
        }

        List<Long> response = new ArrayList<>();
        for (Month month : Month.values()) {
            response.add(monthlyPurchases.get(month));
        }

        return response;
    }

    public GeneralResponse<Long> getTotalRevenue() {
        List<Purchase> purchases = purchaseRepository.findAll();
        Long totalRevenue = 0L;

        for (Purchase purchase : purchases) {
            totalRevenue += PackageType.valueOf(purchase.getType()).getValue();
        }

        return GeneralResponse.success(totalRevenue);
    }
}
