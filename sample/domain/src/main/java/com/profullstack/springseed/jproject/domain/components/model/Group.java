package com.profullstack.springseed.jproject.domain.components.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

/**
 * Created by christianxiao on 4/13/16.
 */
@Data
@Entity
@Table(name="groups")
public class Group {

	@Id
	@GeneratedValue
	private long id;
	private String name;

	@ManyToMany(mappedBy="groups")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<User> users;

	@ManyToMany()
	@JoinTable(name="group_role",
			joinColumns = @JoinColumn(name = "groupId"),
			inverseJoinColumns = @JoinColumn(name = "roleId"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Role> roles;

}
