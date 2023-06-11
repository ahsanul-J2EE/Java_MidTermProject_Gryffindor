package com.onlinebookshop.BookService.controller;

import com.onlinebookshop.BookService.model.ApiResponse;
import com.onlinebookshop.BookService.model.BookDto;
import com.onlinebookshop.BookService.model.BuyRequest;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    @CacheEvict(value = "books", allEntries = true)
    public ResponseEntity<ApiResponse> create(@RequestBody BookDto bookDto) {
        return bookService.create(bookDto);
    }

    @PutMapping("/update/{id}")
    @CacheEvict(value = "books", allEntries = true)
    public ResponseEntity<ApiResponse> updateBookEntity(@PathVariable("id") Long bookId, @RequestBody BookDto bookDto) {
        return bookService.updateBookEntity(bookId, bookDto);
    }

    @DeleteMapping("/delete/{id}")
    @CacheEvict(value = "books", allEntries = true)
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long bookId) {
        this.bookService.delete(bookId);
        return new ResponseEntity(new ApiResponse("Book Details Deleted Successfully",null,true), HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    @Cacheable(value = "books", key = "#bookId")
    public ResponseEntity<ApiResponse> getBookById(@PathVariable("id") Long bookId) {
        return bookService.getBookById(bookId);
    }

    @GetMapping("/book/all")
    @Cacheable("books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/book/buy")
    @CacheEvict(value = "books", allEntries = true)
    public ResponseEntity<ApiResponse> buyBook(@RequestBody BuyRequest buyRequest) {
        return bookService.buyBook(buyRequest);
    }
}
