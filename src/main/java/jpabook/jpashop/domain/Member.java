package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // 연관관계의 주인이 아님을 표기해주지 않으면 연관관계의 주인인이라고 인식하기 때문에 FK 또는 매핑 테이블이 생긴다.
    // 기본전략은 조인 전략이 기본이기 때문에 JoinColumn 을 만들지 않으면 매핑 테이블을 만들어버린다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
