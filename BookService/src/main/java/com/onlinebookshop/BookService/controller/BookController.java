package com.onlinebookshop.BookService.controller;

import com.onlinebookshop.BookService.model.BookRequestModel;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody BookRequestModel requestModel) {
        return bookService.create(requestModel);
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<Object> updateBookEntity(@PathVariable Long bookId, @RequestBody BookRequestModel requestModel) {
        return bookService.updateBookEntity(bookId, requestModel);
    }


    @GetMapping("/showAll")
    public ResponseEntity<Object> showAll() {
        return bookService.showAll();
    }

    //single book find
    @GetMapping("/{bookId}")
    public ResponseEntity<Object> findByBookId(@PathVariable Long bookId) {
        return bookService.findByBookId(bookId);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<Object> delete(@PathVariable Long bookId) {
        return bookService.delete(bookId);
    }



}
