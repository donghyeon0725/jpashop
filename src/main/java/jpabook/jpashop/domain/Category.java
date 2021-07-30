package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 여기서만 조회하는 단방향 관계를 맺어 놓아도 되지만, 양방향으로 만듬. 필드를 더 추가하는 것도 안되고 관리도 어려움
    @ManyToMany
    // 자동으로 jointable 을 가져가지만 jointable 을 직접 명시해서 테이블 이름, 컬럼 이름을 변경해보자
    @JoinTable(
            name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();



}
