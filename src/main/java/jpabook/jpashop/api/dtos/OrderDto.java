package jpabook.jpashop.api.dtos;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime localDateTime;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order o) {
        orderId = o.getId();
        name = o.getMember().getName();
        localDateTime = o.getLocalDateTime();
        orderStatus = o.getOrderStatus();
        address = o.getDelivery().getAddress();
        orderItems = o.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
    }

}
