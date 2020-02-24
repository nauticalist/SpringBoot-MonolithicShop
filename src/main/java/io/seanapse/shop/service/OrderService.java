package io.seanapse.shop.service;

import io.seanapse.shop.domain.Cart;
import io.seanapse.shop.domain.Order;
import io.seanapse.shop.domain.enumeration.OrderStatus;
import io.seanapse.shop.repository.OrderRepository;
import io.seanapse.shop.service.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public List<OrderDto> findAll(){
        log.debug("Request to get all orders.");
        return this.orderRepository.findAll()
                .stream()
                .map(OrderService::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto findById(Long id) {
        log.debug("Request to get order: {}", id);
        return this.orderRepository.findById(id).map(OrderService::mapToDto).orElse(null);
    }

    public List<OrderDto> findAllByUser(Long id) {
        return this.orderRepository.findByCartCustomer_Id(id)
                .stream()
                .map(OrderService::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto create(OrderDto orderDto) {
        log.debug("Request to create order : {}", orderDto);
        return mapToDto(
                this.orderRepository.save(
                        new Order(
                                BigDecimal.ZERO,
                                OrderStatus.CREATION,
                                null,
                                null,
                                null,
                                Collections.emptySet(),
                                null
                        )
                )
        );
    }

    public Order create(Cart cart) {
        log.debug("Request to create order with a cart: {}", cart);
        return this.orderRepository.save(
                new Order(
                        BigDecimal.ZERO,
                        OrderStatus.CREATION,
                        null,
                        null,
                        null,
                        Collections.emptySet(),
                        cart
                )
        );
    }

    public void delete(Long id) {
        log.debug("Request to delete order: {}", id);
        this.orderRepository.deleteById(id);
    }

    public static OrderDto mapToDto(Order order) {
        if (order != null) {
            return new OrderDto(
                    order.getId(),
            order.getTotalPrice(),
                    order.getStatus().name(),
            order.getShipped(),
                    PaymentService.mapToDto(order.getPayment()),
                    AddressService.mapToDto(order.getShipmentAddress()),
                    order.getOrderItems()
                    .stream()
            .map(OrderItemService::mapToDto)
            .collect(Collectors.toSet()));
        }
        return null;
        }
}
