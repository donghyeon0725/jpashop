package jpabook.jpashop.api;

import jpabook.jpashop.api.dtos.OrderDto;
import jpabook.jpashop.api.dtos.OrderQueryDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    // 절대 사용하면 안되는 방법
    @GetMapping("/api/v1/orders")
    public List<Order> orders() {
        return orderRepository.findAll(new OrderSearch());
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        return orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findOrdersFetchWithOrderItem();

        return orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * 페이징이 없을 경우 일대다는 이렇게 구현해도 됩니다.
     * */
    @GetMapping("/api/v3-1/orders")
    public List<OrderDto> ordersV3_1() {
        List<Order> orders = orderRepository.findOrdersFetchWithOrderItemDistinct();

        return orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * 최적화된 fetch join + batch
     * */
    @GetMapping("/api/v3-2/orders")
    public List<OrderDto> ordersV3_2(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findOrdersFetchWithOrderItemBatch(offset, limit);

        return orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * N + 1 문제가 있습니다.
     * */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    /**
     * N + 1 문제를 해결
     * 1 + 1 이 되었습니다.
     *
     * */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findOrderQueryDtos_optimization();
    }

    /**
     * 한번에 데이터를 당겨오고 중복 제거를 하는 방법
     * 직접 묶는다.
     * */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        return orderQueryRepository.findOrderQueryDtos_flat().stream()
                .collect(Collectors.groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getLocalDateTime(), o.getOrderStatus(), o.getAddress())))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getLocalDateTime(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getKey().getOrderItems()))
                .collect(Collectors.toList());

    }

}
