package jpabook.jpashop.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "m")
public class Movie extends Item{
    private String director;
    private String actor;
}
