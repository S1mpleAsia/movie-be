package dev.hust.simpleasia.utils;

import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;
import dev.hust.simpleasia.core.exception.BusinessException;

public class CustomerUtils {
    public static Customer findCustomerByEmail(String email) {
        try {
            CustomerSearchParams params = CustomerSearchParams.builder()
                    .setQuery("email:'" + email + "'")
                    .build();

            CustomerSearchResult result = Customer.search(params);
            return !result.getData().isEmpty() ? result.getData().get(0) : null;
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public static Customer findOrCreateCustomer(String email, String name) {
        try {
            CustomerSearchParams params = CustomerSearchParams.builder()
                    .setQuery("email:'" + email + "'")
                    .build();

            CustomerSearchResult result = Customer.search(params);

            if (result.getData().isEmpty()) {
                CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                        .setName(name)
                        .setEmail(email)
                        .build();

                return Customer.create(customerCreateParams);
            } else {
                return result.getData().get(0);
            }
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }
}
