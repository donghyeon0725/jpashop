package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.api.dtos.OrderFlatDto;
import jpabook.jpashop.api.dtos.OrderItemQueryDto;
import jpabook.jpashop.api.dtos.OrderQueryDto;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> dtos = findOrders();

        dtos.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return dtos;
    }

    public List<OrderQueryDto> findOrderQueryDtos_optimization() {
        List<OrderQueryDto> dtos = findOrders();

        List<Long> orderIds = dtos.stream().map(s->s.getOrderId()).collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = em.createQuery("select new jpabook.jpashop.api.dtos.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        // 성능 최적화를 위해서 orderId 를 key 값으로 map 으로 변경
        Map<Long, List<OrderItemQueryDto>> collect = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        dtos.forEach(dto -> dto.setOrderItems(collect.get(dto.getOrderId())));

        return dtos;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.api.dtos.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItemsIn(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.api.dtos.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new jpabook.jpashop.api.dtos.OrderQueryDto(o.id, m.name, o.localDateTime, o.orderStatus, d.address) from Order o join o.member m join o.delivery d", OrderQueryDto.class).getResultList();
    }

    public List<OrderFlatDto> findOrderQueryDtos_flat() {
        return em.createQuery("select new jpabook.jpashop.api.dtos.OrderFlatDto(o.id, m.name, o.localDateTime, o.orderStatus, d.address, i.name, oi.orderPrice, oi.count) " +
                "from Order o join o.member m join o.delivery d join o.orderItems oi join oi.item i", OrderFlatDto.class).getResultList();
    }
}
