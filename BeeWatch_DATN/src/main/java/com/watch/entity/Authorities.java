package com.watch.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table
public class Authorities implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long authorityId;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Accounts account;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Roles role;

	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	public Authorities(Long authorityId, Accounts account, Roles role) {
		super();
		this.authorityId = authorityId;
		this.account = account;
		this.role = role;
	}
	
	public Authorities() {
		// TODO Auto-generated constructor stub
	}
}
