package com.profullstack.springseed.jproject.domain.components.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by christianxiao on 4/17/16.
 */
@Data
@Entity
@Table(name="employeeCards")
public class EmployeeCard {
    @Id
    @GeneratedValue
    private long id;
    private String department;
    private Date createdAt;

    @OneToOne
    @JoinColumn(name="userId")
    private User user;
}
