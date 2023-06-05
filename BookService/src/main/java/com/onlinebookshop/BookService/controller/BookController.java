package com.onlinebookshop.BookService.controller;

import com.onlinebookshop.BookService.model.BookRequestModel;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody BookRequestModel requestModel) {
        return bookService.create(requestModel);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateBookEntity(@PathVariable("id") Long bookId, @RequestBody BookRequestModel requestModel) {
        return bookService.updateBookEntity(bookId, requestModel);
    }


    @GetMapping("/all")
    public ResponseEntity<Object> showAll() {
        return bookService.showAll();
    }

    //single book find
    @GetMapping("/{id}")
    public ResponseEntity<Object> findByBookId(@PathVariable("id") Long bookId) {
        return bookService.findByBookId(bookId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long bookId) {
        return bookService.delete(bookId);
    }



}
