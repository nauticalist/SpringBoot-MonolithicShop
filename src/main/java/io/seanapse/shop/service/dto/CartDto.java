package io.seanapse.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long orderId;
    private CustomerDto customerDto;
    private String status;
}