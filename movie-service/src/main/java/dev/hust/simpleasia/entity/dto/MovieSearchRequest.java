package dev.hust.simpleasia.entity.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MovieSearchRequest {
    private String query;
    private Integer limit = 10;
    private List<Long> filter;
    private Order order = new Order("id.asc", "id.asc");

    public String getOrderValue() {
        return order.value;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Order {
        private String label;
        private String value;
    }
}
