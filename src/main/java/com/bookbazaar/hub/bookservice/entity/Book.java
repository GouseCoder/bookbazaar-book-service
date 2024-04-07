package com.bookbazaar.hub.bookservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String imgUrl;
	@Column(length = 1000)
	private String description;
	private long price;
	private String authorName;
	private String category;
	private int ratinginStar;
	@Column(length = 1000)
	private String bookCondition;
	private Long sellerId;
	
	public String getCondition() {
		return bookCondition;
	}

	public void setCondition(String bookCondition) {
		this.bookCondition = bookCondition;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Book() {
		super();
		
	}

	public Book(Long id, String name, String imgUrl, String description, long price, String authorName, String category,
			int ratinginStar) {
		super();
		this.id = id;
		this.name = name;
		this.imgUrl = imgUrl;
		this.description = description;
		this.price = price;
		this.authorName = authorName;
		this.category = category;
		this.ratinginStar = ratinginStar;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getRatinginStar() {
		return ratinginStar;
	}

	public void setRatinginStar(int ratinginStar) {
		this.ratinginStar = ratinginStar;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getBookCondition() {
		return bookCondition;
	}

	public void setBookCondition(String bookCondition) {
		this.bookCondition = bookCondition;
	}
	
	

}
