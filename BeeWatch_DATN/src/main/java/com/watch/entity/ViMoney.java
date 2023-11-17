package com.watch.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@Entity
public class ViMoney implements Serializable{

	@Id
	private String name;	
	private float money;
	public ViMoney() {
		
	}
	public ViMoney(String name, float money) {
		super();
		this.name = name;
		this.money = money;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	
	
}
