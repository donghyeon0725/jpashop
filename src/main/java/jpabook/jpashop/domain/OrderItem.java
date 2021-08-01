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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="item_id")
    private Item item;

    // 가격이 바뀔 수 있으므로
    private Integer orderPrice; //  주문 당시 가격
    private Integer count; // 주문 당시 수량

    // 취소
    public void cancel() {
        getItem().addStockQuantity(count);
    }

    // 조회
    public int getTotalPrice() {
        return count * orderPrice;
    }

    // 여기서 order 는 필요 없다. 왜냐하면 Order 와 OrderITem 은 양방향 연관관계의 엔티티로 주 엔티티가 Order 라서 Order 쪽에서 연관관계 편의 메서드로 OrderItem 을 set 해줄 것이기 때문이다.
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // OrderItem 을 생성할 때 Item 의 개수를 제거해주어야 함 => 미리 가지고 있는 것
        item.removeStockQuantity(count);
        return orderItem;
    }
}
