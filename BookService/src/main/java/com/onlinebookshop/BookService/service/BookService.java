package com.onlinebookshop.BookService.service;

import com.onlinebookshop.BookService.model.BookRequestModel;
import org.springframework.http.ResponseEntity;

public interface BookService {

    public ResponseEntity<Object> create(BookRequestModel bookRequestModel);

    public ResponseEntity<Object> showAll();

    ResponseEntity<Object> delete(Long bookId);

    ResponseEntity<Object> updateBookEntity(Long bookId, BookRequestModel requestModel);

    ResponseEntity<Object> findByBookId(Long bookId);


}
