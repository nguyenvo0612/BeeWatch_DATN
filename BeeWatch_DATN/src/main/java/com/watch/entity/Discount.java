package com.watch.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@SuppressWarnings("serial")
@Data
@Entity 
public class Discount implements Serializable{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int discountId;
	private int percentDiscount;
	private boolean status;

	@Column(name = "begin_discount")
	private Date beginDiscount;

	@Column(name = "finish_discount")
	private Date finishDiscount;
	
	@JsonIgnore
	@OneToMany(mappedBy = "discount")
	List<Category> categories;

	public int getDiscountId() {
		return discountId;
	}

	public void setDiscountId(int discountId) {
		this.discountId = discountId;
	}

	public int getPercentDiscount() {
		return percentDiscount;
	}

	public void setPercentDiscount(int percentDiscount) {
		this.percentDiscount = percentDiscount;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Discount(int discountId, int percentDiscount, boolean status, Date beginDiscount, Date finishDiscount, List<Category> categories) {
		this.discountId = discountId;
		this.percentDiscount = percentDiscount;
		this.status = status;
		this.beginDiscount = beginDiscount;
		this.finishDiscount = finishDiscount;
		this.categories = categories;
	}

	public Date getBeginDiscount() {
		return beginDiscount;
	}

	public void setBeginDiscount(Date beginDiscount) {
		this.beginDiscount = beginDiscount;
	}

	public Date getFinishDiscount() {
		return finishDiscount;
	}

	public void setFinishDiscount(Date finishDiscount) {
		this.finishDiscount = finishDiscount;
	}

	public Discount() {
		// TODO Auto-generated constructor stub
	}
	
}
