package jpabook.jpashop.repository.order.simpleRepository;

import jpabook.jpashop.api.dtos.SimpleOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleRepository {
    private final EntityManager em;

    public List<SimpleOrderDto> findOrdersDto() {
        return em.createQuery("select new jpabook.jpashop.api.SimpleOrderDto(o.id, m.name, o.localDateTime, o.orderStatus, d.address) from Order o join o.delivery d join o.member m", SimpleOrderDto.class).getResultList();
    }
}
