package com.bookbazaar.hub.bookservice.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookbazaar.hub.bookservice.entity.Book;
import com.bookbazaar.hub.bookservice.entity.UserBookView;
import com.bookbazaar.hub.bookservice.entity.UserInfo;

public interface UserBookViewRepository extends JpaRepository<UserBookView, Long>{
	
	Optional<UserBookView> findByUserAndBook(UserInfo user, Book book);

	List<UserBookView> findByUser(UserInfo user);

}
