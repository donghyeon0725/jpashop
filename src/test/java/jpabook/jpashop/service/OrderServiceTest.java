package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.exception.NotEnoughQuantityException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 테스트 요구사항
 *
 * 상품 주문이 성공해야한다.
 * 상품을 주문할 때 재고 수량을 초과하면 안된다.
 * 주문 취소가 성공해야 한다.
 * */
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager em;

    // 주문
    @Test
    public void 상품_주문() {
        Member member = settingMember("회원", "서울", "강북", "1234-1234");

        Book book = settingItem("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order order =  orderRepository.findOne(orderId);


        assertEquals(OrderStatus.ORDER, order.getOrderStatus(), () -> "상품 주문시 상태는 ORDER 여야 한다.");
        assertEquals(1, order.getOrderItems().size(), () -> "주문한 상품 종류 수가 정확해야 한다");
        assertEquals(book.getPrice() * orderCount, order.getTotalPrice(), () -> "주문 가격은 상품 가격 * 주문 수량이어야 한다.");
        assertEquals(8, book.getStockQuantity(), () -> "주문 수량만큼 재고가 줄어야 한다.");

    }

    // 주문 초과
    @Test
    public void 상품_주문_초과() {
        Member member = settingMember("회원", "서울", "강북", "1234-1234");

        Book book = settingItem("JPA", 10000, 10);

        int orderCount = 11;

        assertThrows(NotEnoughQuantityException.class, () -> orderService.order(member.getId(), book.getId(), orderCount), () -> "주문 수량이 재고보다 많아서는 안된다.");
    }

    // 취소
    @Test
    public void 상품_주문_취소() {
        Member member = settingMember("회원", "서울", "강북", "1234-1234");

        Book book = settingItem("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancel(orderId);



        Order order = orderRepository.findOne(orderId);

        // 재고가 원래대로, 돌아와야 한다.
        // 주문 상태가 취소여야 한다.

        assertEquals(OrderStatus.CANCLE, order.getOrderStatus(), () -> "주문 상태는 취소가 되어야 한다.");
        assertEquals(10, book.getStockQuantity(), () -> "주문 재고의 수량은 원래대로 돌아와야 한다.");
    }


    private Book settingItem(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member settingMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        em.persist(member);
        return member;
    }




}
