package com.onlinebookshop.BookService.service;

import com.onlinebookshop.BookService.model.BookDto;
import com.onlinebookshop.BookService.model.BuyRequest;

import java.util.List;

public interface BookService {

    BookDto create(BookDto bookDto);
    List<BookDto> getAllBooks();
    void delete(Long bookId);
    BookDto updateBookEntity(Long bookId, BookDto requestModel);

    BookDto getBookById(Long bookId);

    String buyBook(BuyRequest buyRequest);




}
