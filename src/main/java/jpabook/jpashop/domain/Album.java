package jpabook.jpashop.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "a")
public class Album extends Item{
    private String artist;
    private String etc;
}
