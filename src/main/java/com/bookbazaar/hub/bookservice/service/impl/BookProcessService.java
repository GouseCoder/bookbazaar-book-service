package com.bookbazaar.hub.bookservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookbazaar.hub.bookservice.dto.AddBookRequestDto;
import com.bookbazaar.hub.bookservice.entity.Book;
import com.bookbazaar.hub.bookservice.entity.UserBookView;
import com.bookbazaar.hub.bookservice.entity.UserInfo;
import com.bookbazaar.hub.bookservice.entity.Wishlist;
import com.bookbazaar.hub.bookservice.repo.BooksRepository;
import com.bookbazaar.hub.bookservice.repo.UserBookViewRepository;
import com.bookbazaar.hub.bookservice.repo.UserRepository;
import com.bookbazaar.hub.bookservice.repo.WishlistRepo;
import com.bookbazaar.hub.bookservice.utils.AppConstants;
import com.bookbazaar.hub.bookservice.utils.CommonsUtils;
import com.bookbazaar.hub.bookservice.utils.JacksonUtil;
import com.bookbazaar.hub.bookservice.utils.ResponseConstants;
import com.bookbazaar.hub.bookservice.utils.ResponseKeyConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class BookProcessService {

	private static final Logger logger = LoggerFactory.getLogger(BookProcessService.class);

	@Autowired
	BooksRepository booksRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	WishlistRepo wishlistRepo;

	@Autowired
	UserBookViewRepository userBookViewRepository;

	@Autowired
	CommonsUtils commonsUtils;
	
	@Value("${recommendation.api.url}")
    private String recommendationApiUrl;

	public JsonNode addBook(AddBookRequestDto bookdto) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			Book book = new Book();
			book.setAuthorName(bookdto.getAuthorName());
			book.setCategory(bookdto.getCategory());
			book.setDescription(bookdto.getDescription());
			book.setName(bookdto.getName());
			book.setImgUrl(bookdto.getImgUrl());
			book.setPrice(bookdto.getPrice());
			book.setCondition(bookdto.getBookCondition());
			book.setSellerId(bookdto.getSellerId());

			booksRepository.save(book);
			dataObject.put(AppConstants.ERROR_CODE, ResponseConstants.BOOK_ADDED_SUCCESSFULLY);
			dataObject.put(AppConstants.ERROR_REASON, "Book added sucessfully!!");

		} catch (Exception e) {
			logger.error("Exception in addBook ", e);
		}

		return resultNode;

	}

	public JsonNode getBookByCat(String category) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			List<Book> booksbycat = booksRepository.findByCategory(category);
			ArrayNode booksList = JacksonUtil.mapper.createArrayNode();
			for (Book book : booksbycat) {
				JsonNode bookObject = convertEntityToObject(book);
				booksList.add(bookObject);
			}

			dataObject.set(ResponseKeyConstants.BOOKS_BY_CAT, booksList);

		} catch (Exception e) {
			logger.error("Exception in getBookByCat ", e);
		}

		return resultNode;

	}

	public JsonNode getBookById(Long bookId) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			Optional<Book> bookDetails = booksRepository.findById(bookId);
			Book book = bookDetails.get();
			JsonNode bookObject = convertEntityToObject(book);
			dataObject.set(ResponseKeyConstants.BOOK_DETAILS, bookObject);

		} catch (Exception e) {
			logger.error("Exception in getBookById ", e);
		}

		return resultNode;

	}

	public JsonNode getbooksForDashboard(Long userId) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		boolean isUserNewlyLoggedIn = false;

		try {
			if(userId == null) {
				isUserNewlyLoggedIn = true;
			}
			else {
				Optional<UserInfo> userOptional = userRepository.findById(userId);
				if (!userOptional.isPresent()) {
					isUserNewlyLoggedIn = true;
				}
			}

			if (isUserNewlyLoggedIn) {
				createDashboardForNewUser(dataObject);
			} else {
				createDashboardForExistingUser(dataObject, userId);
			}
		} catch (Exception e) {
			logger.error("Exception in getbooksForDashboard ", e);
		}

		return resultNode;
	}

	private void createDashboardForNewUser(ObjectNode dataObject) {

		try {

			dataObject.set("trendingBooks", getTrendingBooks());
			dataObject.set("categorizedBooks", getPopularBooksBycat());

		} catch (Exception e) {
			logger.error("Exception in createDashboardForNewUser ", e);
		}

	}

	private void createDashboardForExistingUser(ObjectNode dataObject, Long userId) {

		try {
			dataObject.set("lastViewdBooks", getLastViwedItems(userId));
			dataObject.set("recommendations", getRecommendedBooks(userId));
			dataObject.set("categorizedBooks", getPopularBooksBycat());
		} catch (Exception e) {
			logger.error("Exception in createDashboardForExistingUser ", e);
		}
	}

	private ArrayNode getRecommendedBooks(long userId) {
		ArrayNode recommendedBooksArray = JacksonUtil.mapper.createArrayNode();
		try {
			List<Long> recommendedBooksIds = getRecommendedBooksIds(userId);
			logger.debug("recommendedBooksIds : " + recommendedBooksIds);
			List<Book> recommendedBooks = booksRepository.getLastViewedBooksById(recommendedBooksIds);
			for(Book book : recommendedBooks) {
				JsonNode bookObject = convertEntityToObject(book);
				recommendedBooksArray.add(bookObject);
			}
			
		} catch (Exception e) {
			logger.error("Exception in getRecommendedBooks " + e);
		}
		
		return recommendedBooksArray;
	}

	private ArrayNode getPopularBooksBycat() {

		List<String> popularCtegories = new ArrayList<String>();
		popularCtegories.add("Fiction");
		popularCtegories.add("Science Fiction");
		popularCtegories.add("Mystery");
		popularCtegories.add("Philosophy");
		popularCtegories.add("Self-Help");
		popularCtegories.add("Biography");

		ArrayNode outerAray = JacksonUtil.mapper.createArrayNode();

		try {
			for (String category : popularCtegories) {

				ObjectNode categoryObject = JacksonUtil.mapper.createObjectNode();
				ArrayNode categoryAray = JacksonUtil.mapper.createArrayNode();
				List<Book> booksbycat = booksRepository.getPopularBooksFromDb(category);
				for (Book book : booksbycat) {
					JsonNode bookObject = convertEntityToObject(book);
					categoryAray.add(bookObject);
				}
				categoryObject.set(category, categoryAray);
				outerAray.add(categoryObject);
			}
		} catch (Exception e) {
			logger.error("Exception in getPopularBooksBycat ", e);
		}

		return outerAray;
	}

	private ArrayNode getTrendingBooks() {

		return JacksonUtil.mapper.createArrayNode();
	}

	private ArrayNode getLastViwedItems(Long userId) {
		ArrayNode lastViwedAray = JacksonUtil.mapper.createArrayNode();
		Optional<UserInfo> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {

			UserInfo user = userOptional.get();
			List<UserBookView> lastViewedBooks = userBookViewRepository.findByUser(user);
			logger.debug("UserBookView list " + lastViewedBooks);
			for (UserBookView lastViewed : lastViewedBooks) {
				Book book = lastViewed.getBook();
				JsonNode bookObject = convertEntityToObject(book);
				lastViwedAray.add(bookObject);
			}
		}

		return lastViwedAray;
	}

	private JsonNode convertEntityToObject(Book book) {

		ObjectNode bookObject = JacksonUtil.mapper.createObjectNode();
		try {

			bookObject.put(ResponseKeyConstants.BOOK_ID, book.getId());
			bookObject.put(ResponseKeyConstants.BOOK_NAME, book.getName());
			bookObject.put(ResponseKeyConstants.BOOK_IMG_URL, book.getImgUrl());
			bookObject.put(ResponseKeyConstants.BOOK_DESC, book.getDescription());
			bookObject.put(ResponseKeyConstants.PRICE, book.getPrice());
			bookObject.put(ResponseKeyConstants.BOOK_AUTHOR, book.getAuthorName());
			bookObject.put(ResponseKeyConstants.BOOK_CAT, book.getCategory());
			bookObject.put(ResponseKeyConstants.BOOK_RATING_STAR, book.getRatinginStar());
			bookObject.put(ResponseKeyConstants.BOOK_CONDITION, book.getCondition());
			bookObject.put(ResponseKeyConstants.SELLER_ID, book.getSellerId());

		} catch (Exception e) {
			logger.error("Exception in convertEntityToObject ", e);
		}

		return bookObject;
	}

	public JsonNode addToWishList(Long userId, Long bookId) {

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);
		ObjectNode errorObject = (ObjectNode) resultNode.get(AppConstants.ERROR_OBJECT);

		try {

			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			Book book = booksRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

			// Check if the book is already in the user's cart
			Wishlist existingItem = wishlistRepo.findByUserAndBook(user, book);

			if (existingItem != null) {
				// If the book is already in the wishlist, send message
				errorObject.put(AppConstants.ERROR_CODE, ResponseConstants.PRODUCT_ALREADY_IN_CART);
				errorObject.put(AppConstants.ERROR_REASON, "Product Already in wishlist");
			} else {

				Wishlist newWishListItem = new Wishlist();
				newWishListItem.setUser(user);
				newWishListItem.setBook(book);
				wishlistRepo.save(newWishListItem);

				dataObject.put(AppConstants.ERROR_CODE, ResponseConstants.ADDED_TO_CART);
				dataObject.put(AppConstants.ERROR_REASON, "Added to Wishlist");
			}

		} catch (Exception e) {
			logger.error("Exception in addToWishList ", e);
		}

		return resultNode;

	}

	public JsonNode showWishList(Long userId) {

		ArrayNode wishListArray = JacksonUtil.mapper.createArrayNode();

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

			List<Wishlist> wishListItems = wishlistRepo.findByUser(user);
			for (Wishlist wishlist : wishListItems) {
				ObjectNode obj = JacksonUtil.mapper.createObjectNode();
				JsonNode book = convertEntityToObject(wishlist.getBook());
				obj.set(ResponseKeyConstants.BOOK, book);
				wishListArray.add(obj);
			}
			dataObject.set("wishList", wishListArray);

		} catch (Exception e) {
			logger.error("Exception in showCart ", e);
		}

		return resultNode;
	}

	public JsonNode getBookByFiltering(String category, int rating, Long minPrice, Long maxPrice) {

		ArrayNode bookArray = JacksonUtil.mapper.createArrayNode();

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			List<Book> filteredBooks = booksRepository.getFilteredBooks(category, rating, minPrice, maxPrice);
			for (Book book : filteredBooks) {
				JsonNode bookObject = convertEntityToObject(book);
				bookArray.add(bookObject);
			}

			dataObject.set("Books", bookArray);

		} catch (Exception e) {
			logger.error("Exception in getBookByFiltering ", e);
		}

		return resultNode;
	}

	public JsonNode getPopularBooks() {

		ArrayNode bookArray = JacksonUtil.mapper.createArrayNode();

		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);

		try {

			List<Book> popluarBooks = booksRepository.getPopularBooks();
			for (Book book : popluarBooks) {
				JsonNode bookObject = convertEntityToObject(book);
				bookArray.add(bookObject);
			}

			dataObject.set("popularBooks", bookArray);

		} catch (Exception e) {
			logger.error("Exception in getPopularBooks ", e);
		}

		return resultNode;
	}

	public JsonNode handleBookView(Long bookId, Long userId) {
		
		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);
		
		try {
			
			Book book = booksRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
			if (userId != null) {
				// User is registered, update view count in UserBookView table
				logger.debug("User is registered, update view count in UserBookView table");
				UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

				if (user != null && book != null) {
					Optional<UserBookView> userBookViewOptional = userBookViewRepository.findByUserAndBook(user, book);

					if (userBookViewOptional.isPresent()) {
						logger.debug("user already viwed this book!!");
						logger.debug("hence increasing the count ");
						UserBookView userBookView = userBookViewOptional.get();
						userBookView.setViewedCount(userBookView.getViewedCount() + 1);
						userBookViewRepository.save(userBookView);
					} else {
						logger.debug("user seeing this book for the first time!!");
						UserBookView newUserBookView = new UserBookView();
						newUserBookView.setUser(user);
						newUserBookView.setBook(book);
						newUserBookView.setViewedCount(1);
						userBookViewRepository.save(newUserBookView);
					}
				}
			}
			
			JsonNode bookObject = convertEntityToObject(book);
			dataObject.set(ResponseKeyConstants.BOOK_DETAILS, bookObject);
			
		} catch (Exception e) {
			logger.error("Exception in handleBookView ", e);
		}
		
		return resultNode;
	}

	public JsonNode getAllBooks() {
		
		ObjectNode resultNode = commonsUtils.createResultNode();
		ObjectNode dataObject = (ObjectNode) resultNode.get(AppConstants.DATA_OBJECT);
		ArrayNode bookArray = JacksonUtil.mapper.createArrayNode();
		
		try {
			
			List<Book> allBooks = booksRepository.findAll();
			
			for (Book book : allBooks) {
				JsonNode bookObject = convertEntityToObject(book);
				bookArray.add(bookObject);
			}

			dataObject.set("allBooks", bookArray);
			
		} catch (Exception e) {
			logger.error("exception in getAllBooks " + e);
		}
		
		return resultNode;
	}
	
	 

	    public List<Long> getRecommendedBooksIds(Long userId) {
	    	
	    	List<Long> recommendedBookIds = null;
	    	
	    	try {
	    		// Prepare the URL with the user ID parameter
		        String apiUrl = recommendationApiUrl + "?user_id=" + userId;
		        
		        System.out.println(apiUrl);

		        // Create a RestTemplate instance
		        RestTemplate restTemplate = new RestTemplate();

		        // Make a GET request to the API endpoint
		        ResponseEntity<List<Long>> response = restTemplate.exchange(
		                apiUrl,
		                HttpMethod.GET,
		                null,
		                new ParameterizedTypeReference<List<Long>>() {});
		        
		        logger.debug("response " + response);

		        // Extract the recommended book IDs from the response body
		        recommendedBookIds = response.getBody();

			} catch (Exception e) {
				logger.error("Exception in getRecommendedBooksIds " + e);
			}
	        
	        return recommendedBookIds;
	    }

}
