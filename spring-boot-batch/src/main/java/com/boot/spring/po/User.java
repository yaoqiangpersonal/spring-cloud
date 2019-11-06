package com.boot.spring.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import lombok.Data;

@Data
@Entity
@Table(name ="user")
public class User {
	
	@Id
	private Integer id;
	private String username;
	private String password;

}
