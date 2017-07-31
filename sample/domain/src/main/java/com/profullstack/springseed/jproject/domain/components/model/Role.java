package com.profullstack.springseed.jproject.domain.components.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Created by christianxiao on 4/13/16.
 */
@Entity
@Table(name="roles")
@Data
public class Role {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String description;
	private Date createdAt;
	private Date updatedAt;

	@ManyToMany(mappedBy="roles")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Group> groups;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="role_menu",
			joinColumns = @JoinColumn(name = "roleId"),
			inverseJoinColumns = @JoinColumn(name = "menuId"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Menu> menus;

	public Role(){}
}
