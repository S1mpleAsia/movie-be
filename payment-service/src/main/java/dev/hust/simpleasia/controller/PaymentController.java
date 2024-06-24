package dev.hust.simpleasia.controller;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.entity.dto.PaymentResponse;
import dev.hust.simpleasia.entity.dto.StripeCheckoutRequest;
import dev.hust.simpleasia.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public String checkoutMovie(@RequestBody StripeCheckoutRequest stripeCheckoutRequest) {
        return paymentService.checkoutMovie(stripeCheckoutRequest);
    }

    @PostMapping("/webhook")
    public GeneralResponse<String> postEventsWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        return paymentService.postEventsWebhook(payload, sigHeader);
    }

    @GetMapping
    public GeneralResponse<PaymentResponse> getUserPayment(@RequestParam("userId") String userId) {
        return paymentService.getUserPayment(userId);
    }

    @GetMapping("/overview")
    public GeneralResponse<List<Long>> getRevenueOverview(@RequestParam("period") String period, @RequestParam("date") String date) {
        return paymentService.getRevenueOverview(period, date);
    }

    @GetMapping("/summary")
    public GeneralResponse<Long> getTotalRevenue() {
        return paymentService.getTotalRevenue();
    }
}
