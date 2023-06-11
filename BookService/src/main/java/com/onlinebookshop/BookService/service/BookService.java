package com.onlinebookshop.BookService.service;

import com.onlinebookshop.BookService.model.ApiResponse;
import com.onlinebookshop.BookService.model.BookDto;
import com.onlinebookshop.BookService.model.BuyRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {

    ResponseEntity<ApiResponse> create(BookDto bookDto);
    ResponseEntity<List<BookDto>> getAllBooks();
    void delete(Long bookId);
    ResponseEntity<ApiResponse> updateBookEntity(Long bookId, BookDto requestModel);
    ResponseEntity<ApiResponse> getBookById(Long bookId);
    ResponseEntity<ApiResponse> buyBook(BuyRequest buyRequest);
}
