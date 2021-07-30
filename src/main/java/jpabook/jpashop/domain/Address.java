package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * 불변 객체로 만들어야 합니다
 * 기본 생성자는 있어야 합니다.
 * 다만 불변 객체여야 하기 때문에 protected 로 한다.
 *
 * 왜 불변 객체여야 하나?
 *
 * 자바는 기본적으로 클래스에 대해서 깊은 복사가 아니라, 참조값을 가져가기 때문에 다른 두 엔티티에서 임베디드 타입을 공유하고 있을 경우
 * 변경이 일어나면 두 엔티티 모두에 변경이 일어나기 때문이다.
 * */
@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city;

     private String street;

     private String zipcode;


}
