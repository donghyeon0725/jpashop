package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    // 이렇게 해주지 않으면 이름이 member_member_id 가 되어 버린다.
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    // 기본적으로 FK 는 주테이블 쪽에 있는 것이 확장성에는 불리하지만(DBA 도 싫어하지만) 성능상 유리하다.
    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
