package com.profullstack.springseed.sample.domain.components.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by christianxiao on 4/13/16.
 */
@Data
@Entity
@Table(name="menus")
public class Menu {
	public enum Status{
		SHOW,HIDE,DISABLE;
	}
	public enum HttpMethod{
		GET,POST,PUT,HEAD;
	}
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String status;
	private String uri;
	private String httpMethod;
	private int depth;
	private int menuOrder;
	private Long parentId;

	@ManyToMany(mappedBy="menus")
	private List<Role> roles;
}
