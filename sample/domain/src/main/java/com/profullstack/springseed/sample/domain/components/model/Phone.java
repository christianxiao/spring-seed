package com.profullstack.springseed.sample.domain.components.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by christianxiao on 4/17/16.
 */
@Data
@Entity
@Table(name="phones")
public class Phone {
    @Id
    @GeneratedValue
    private long id;
    private String os;
    private String name;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
}
