package io.seanapse.shop.service;

import io.seanapse.shop.domain.Order;
import io.seanapse.shop.domain.OrderItem;
import io.seanapse.shop.domain.Product;
import io.seanapse.shop.repository.OrderItemRepository;
import io.seanapse.shop.repository.OrderRepository;
import io.seanapse.shop.repository.ProductRepository;
import io.seanapse.shop.service.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public List<OrderItemDto> findAll(){
        log.debug("Request to get all order items.");
        return this.orderItemRepository.findAll()
                .stream()
                .map(OrderItemService::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderItemDto findById(Long id){
        log.debug("Request to get OrderItem : {}", id);
        return this.orderItemRepository.findById(id).map(OrderItemService::mapToDto).orElse(null);
    }

    public OrderItemDto create(OrderItemDto orderItemDto) {
        log.debug("Request to create order item : {}", orderItemDto);
        Order order =
                this.orderRepository.findById(orderItemDto.getOrderId())
                        .orElseThrow(
                                () ->
                                        new IllegalStateException("The Order does not exist!")
                        );
        Product product =
                this.productRepository.findById(orderItemDto.getProductId())
                        .orElseThrow(
                                () ->
                                        new IllegalStateException("The Product does not exist!")
                        );
        return mapToDto(
                this.orderItemRepository.save(
                        new OrderItem(
                                orderItemDto.getQuantity(),
                                product,
                                order
                        )));
    }

    public void delete(Long id) {
        log.debug("Request to delete order item : {}", id);
        this.orderItemRepository.deleteById(id);
    }

    public static OrderItemDto mapToDto(OrderItem orderItem) {
        if (orderItem != null) {
            return new OrderItemDto(
                    orderItem.getId(),
                    orderItem.getQuantity(),
                    orderItem.getProduct().getId(),
                    orderItem.getOrder().getId()
            );
        }
        return null;
    }
}
