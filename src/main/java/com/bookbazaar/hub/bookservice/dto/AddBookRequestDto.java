package com.bookbazaar.hub.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AddBookRequestDto {

	private Long id;
	
	@Schema(description = "Name", example = "Atomic Habits")
	private String name;
	
	@Schema(description = "imgUrl", example = "jgdasfuyagasieafggy12548aggfggffzx789gUGFWEGKGGG")
	private String imgUrl;
	
	@Schema(description = "Description", example = "A self-help book focusing on the science of habit formation and how tiny "
			+ "changes can lead to remarkable results.")
	private String description;
	
	@Schema(description = "Price", example = "463")
	private long price;
	
	@Schema(description = "AuthorName", example = "James Clear")
	private String authorName;
	
	@Schema(description = "Category", example = "Productivity")
	private String category;
	
	@Schema(description = "Book Current condition", example = "new book used only 3 month in good condition")
	private String bookCondition;
	
	@Schema(description = "SellerId", example = "1")
	private Long sellerId;

	public String getBookCondition() {
		return bookCondition;
	}

	public void setBookCondition(String bookCondition) {
		this.bookCondition = bookCondition;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public AddBookRequestDto() {

	}

	public AddBookRequestDto(Long id, String name, String description, long price, String authorName, String category) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.authorName = authorName;
		this.category = category;
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

}
