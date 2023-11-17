package com.watch.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@Entity 
public class Glass_material implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Column(columnDefinition = "NVARCHAR(255)  NULL")
	String name;
	//@Column(columnDefinition = "NVARCHAR(200)  NULL")
	private boolean status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public Glass_material(int id, String name, boolean status) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
	}
	public Glass_material() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
}
