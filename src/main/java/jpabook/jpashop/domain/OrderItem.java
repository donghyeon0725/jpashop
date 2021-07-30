package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name ="item_id")
    private Item item;

    // 가격이 바뀔 수 있으므로
    private Integer orderPrice; //  주문 당시 가격
    private Integer count; // 주문 당시 수량
}
