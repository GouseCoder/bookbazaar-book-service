package com.bookbazaar.hub.bookservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookbazaar.hub.bookservice.entity.Book;

public interface BooksRepository extends JpaRepository<Book, Long> {

	List<Book> findByCategory(String category);

	@Query(value = "select * from book where category = :category order by ratingin_star", nativeQuery = true)
	List<Book> getPopularBooksFromDb(String category);

	@Query(value = "select * from book where id IN (:lastViwedItems) ", nativeQuery = true)
	List<Book> getLastViewedBooksById(@Param("lastViwedItems") List<Long> lastViwedItems);

	@Query(value = "SELECT * FROM book " 
			+ "WHERE (:category IS NULL OR category = :category) "
			+ "AND ratingin_star = :rating " 
			+ "AND (:minPrice IS NULL OR price >= :minPrice) "
			+ "AND (:maxPrice IS NULL OR price <= :maxPrice)", nativeQuery = true)
	List<Book> getFilteredBooks(@Param("category") String category, 
			@Param("rating") int rating,
			@Param("minPrice") Long minPrice, 
			@Param("maxPrice") Long maxPrice);

	@Query(value = "select * from book order by ratingin_star", nativeQuery = true)
	List<Book> getPopularBooks();
}
