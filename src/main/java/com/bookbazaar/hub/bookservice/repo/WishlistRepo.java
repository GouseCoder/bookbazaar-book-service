package com.bookbazaar.hub.bookservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookbazaar.hub.bookservice.entity.Book;
import com.bookbazaar.hub.bookservice.entity.UserInfo;
import com.bookbazaar.hub.bookservice.entity.Wishlist;

public interface WishlistRepo extends JpaRepository<Wishlist, Long>{
	
	Wishlist findByUserAndBook(UserInfo user, Book book);

	List<Wishlist> findByUser(UserInfo user);

}
