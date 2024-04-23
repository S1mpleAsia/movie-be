package dev.hust.simpleasia.entity.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "genre")
@Getter
@Setter
@ToString
public class Genre {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

}
