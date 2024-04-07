package com.bookbazaar.hub.bookservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Wishlist {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long wishlistId;
	
	@ManyToOne
	private UserInfo user;
	
	@ManyToOne
	private Book book;
	
	public Wishlist() {
		super();
		
	}

	public Wishlist(Long wishlistId, UserInfo user, Book book) {
		super();
		this.wishlistId = wishlistId;
		this.user = user;
		this.book = book;
	}

	public Long getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(Long wishlistId) {
		this.wishlistId = wishlistId;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	


}
