package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);


        List<Predicate> criteria = new ArrayList<>();

        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("orderStatus"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        if (StringUtils.hasText(orderSearch.getName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return  query.getResultList();
    }

    public List<Order> findOrdersFetch() {
        return em.createQuery("select o from Order o join fetch o.delivery d join fetch o.member m", Order.class).getResultList();
    }

    public List<Order> findOrdersFetchWithOrderItem() {
        return em.createQuery("select o from Order o join fetch o.delivery d join fetch o.member m join fetch o.orderItems i", Order.class)
                .getResultList();
    }

    public List<Order> findOrdersFetchWithOrderItemDistinct() {
        return em.createQuery("select distinct o from Order o join fetch o.delivery d join fetch o.member m join fetch o.orderItems oi join fetch oi.item i", Order.class)
                /*.setFirstResult(1)
                .setMaxResults(100)*/
                .getResultList();
    }


    public List<Order> findOrdersFetchWithOrderItemBatch(int offset, int limit) {
        return em.createQuery("select o from Order o join fetch o.delivery d join fetch o.member m", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
