package com.webrixtec.buildandbeautify.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name="login_client_Model")
public class loginCleintModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "clientId", nullable = false, referencedColumnName = "id")
	private UserModel userModel;
	@NotEmpty(message="Complaint must not to be null")
	private String complaint;
	@NotEmpty(message="comment must not to be null")
	private String comment;
	@NotEmpty(message="category must not to be null")
	private String category;
	@NotEmpty(message="product must not to be null")
	private String product;
	@NotEmpty(message="quantity must not to be null")
	private String quantity;
	@NotEmpty(message="other must not to be null")
	private String other;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getComplaint() {
		return complaint;
	}
	
	public UserModel getUserModel() {
		return userModel;
	}
	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	

	
	
}
