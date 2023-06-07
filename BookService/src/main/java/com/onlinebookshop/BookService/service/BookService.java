package com.onlinebookshop.BookService.service;

import com.onlinebookshop.BookService.model.BookDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {

    ResponseEntity<Object> create(BookDto bookDto);
    List<BookDto> getAllBooks();
    void delete(Long bookId);
    BookDto updateBookEntity(Long bookId, BookDto requestModel);

    BookDto getBookById(Long bookId);




}
