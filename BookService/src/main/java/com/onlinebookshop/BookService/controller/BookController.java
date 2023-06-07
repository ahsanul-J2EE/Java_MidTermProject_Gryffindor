package com.onlinebookshop.BookService.controller;

import com.onlinebookshop.BookService.model.ApiResponse;
import com.onlinebookshop.BookService.model.BookDto;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

@RestController
@RequestMapping("/book-service")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public BookDto create(@RequestBody BookDto bookDto) {

//        ResponseEntity<Object> newBookDto = bookService.create(bookDto);
        return bookService.create(bookDto);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<BookDto> updateBookEntity(@PathVariable("id") Long bookId, @RequestBody BookDto bookDto) {

        BookDto updatedBook =  bookService.updateBookEntity(bookId,bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long bookId) {
//        return bookService.delete(bookId);

        this.bookService.delete(bookId);

        return new ResponseEntity(new ApiResponse("User Deleted Successfully",true),HttpStatus.OK);

    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable("id") Long bookId) {
        return ResponseEntity.ok( bookService.getBookById(bookId));
    }

    @GetMapping("/book/all")
    public ResponseEntity<List<BookDto>> getBookById() {
        return ResponseEntity.ok( bookService.getAllBooks());
    }







}
