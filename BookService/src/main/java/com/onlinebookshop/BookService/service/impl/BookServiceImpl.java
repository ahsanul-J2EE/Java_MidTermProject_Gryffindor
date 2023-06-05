package com.onlinebookshop.BookService.service.impl;

import com.onlinebookshop.BookService.entity.BookEntity;
import com.onlinebookshop.BookService.model.BookRequestModel;
import com.onlinebookshop.BookService.repository.BookRepository;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public ResponseEntity<Object> create(BookRequestModel bookRequestModel) {
        BookEntity bookEntity = BookEntity.builder()
                .bookName(bookRequestModel.getBookName())
                .authorName(bookRequestModel.getAuthorName())
                .genre(bookRequestModel.getGenre())

                .build();
        BookEntity savedBookEntity = bookRepository.save(bookEntity);

        return new ResponseEntity<>(savedBookEntity, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<Object> showAll() {
        List<BookEntity> bookList = bookRepository.findAll();
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> delete(Long bookId) {
        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        if (bookEntity == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        bookRepository.deleteById(bookId);
        return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateBookEntity(Long bookId, BookRequestModel requestModel) {
        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        if (bookEntity == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        // Update the bookEntity with the requestModel data
        bookEntity.setBookName(requestModel.getBookName());
        bookEntity.setAuthorName(requestModel.getAuthorName());
        bookEntity.setGenre(requestModel.getGenre());

        BookEntity updatedBookEntity = bookRepository.save(bookEntity);
        return new ResponseEntity<>(updatedBookEntity, HttpStatus.OK);
    }

    //single user find
    @Override
    public ResponseEntity<Object> findByBookId(Long bookId) {
        BookEntity bookEntity = bookRepository.findByBookId(bookId);
        if (bookEntity == null) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bookEntity, HttpStatus.OK);
    }
}


