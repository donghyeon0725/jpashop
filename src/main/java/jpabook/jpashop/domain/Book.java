package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "b")
@Setter @Getter
public class Book extends Item{
    private String author;
    private String isbn;

}
