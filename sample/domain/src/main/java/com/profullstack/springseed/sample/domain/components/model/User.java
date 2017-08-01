package com.profullstack.springseed.sample.domain.components.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by christianxiao on 4/7/16.
 */
@Entity
@Table(name="users")
@Data
public class User implements Serializable {
	public enum EmployStatus{
		EMPLOYED,UNEMPLOYED;
	}
	public enum EmployType{
		REGULAR,OUTSOURCE;
	}

	@Id
	@GeneratedValue
	private long id;
	private String loginId;
	private String password;
	private String employeeId;
	private String name;
	private String nameKr;
	private boolean enable;
	private String employStatus;
	private String employType;
	private Date createdAt;
	private Date updatedAt;

	@ManyToMany()
	@JoinTable(name="user_group",
			joinColumns = @JoinColumn(name = "userId"),
			inverseJoinColumns = @JoinColumn(name = "groupId"))
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Group> groups;

	@OneToMany(mappedBy="user")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Phone> phones;

	@OneToOne(mappedBy="user")
	private EmployeeCard employeeCard;

	@Transient
	private List<Role> roles;
	@Transient
	private Set<String> roleNames;
	@Transient
	private List<Menu> menus;

	public User(){}

	public List<Role> getRoles(){
		if(roles != null){
			return roles;
		}
		roles = new LinkedList<>();
		for(Group g:getGroups()){
			for(Role r:g.getRoles()){
				roles.add(r);
			}
		}
		return roles;
	}

	public Set<String> getRoleNames(){
		if(roleNames != null){
			return roleNames;
		}
		roleNames = new TreeSet<>();
		for(Role r: this.getRoles()){
			roleNames.add(r.getName());
		}
		return roleNames;
	}

	public List<Menu> getMenus(){
		if(menus != null){
			return menus;
		}
		menus = new LinkedList<>();
		for(Role r: this.getRoles()){
			for(Menu m: r.getMenus()){
				menus.add(m);
			}
		}
		return menus;
	}
}
