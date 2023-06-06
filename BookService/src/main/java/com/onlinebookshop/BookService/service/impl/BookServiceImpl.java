package com.onlinebookshop.BookService.service.impl;

import com.onlinebookshop.BookService.entity.BookEntity;
import com.onlinebookshop.BookService.exceptions.ResourceNotFoundException;
import com.onlinebookshop.BookService.model.BookDto;
import com.onlinebookshop.BookService.repository.BookRepository;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BookDto create(BookDto bookDto) {
//        BookEntity bookEntity = BookEntity.builder()
//                .bookName(bookDto.getBookName())
//                .authorName(bookDto.getAuthorName())
//                .genre(bookDto.getGenre())
//                .build();
//        BookEntity savedBookEntity = bookRepository.save(bookEntity);

        BookEntity bookEntity = this.dtoToBookEntity(bookDto);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        return this.bookEntityToDto(savedBookEntity);

    }

    @Override
    public List<BookDto> getAllBooks() {

        List<BookEntity> bookEntities =  this.bookRepository.findAll();
        List<BookDto> bookDtos = bookEntities.stream().map(book -> this.bookEntityToDto(book)).collect(Collectors.toList());
        return bookDtos;

    }


    @Override
    public void delete(Long bookId) {

        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));

        bookRepository.delete(bookEntity);

    }

    @Override
    public BookDto updateBookEntity(Long bookId, BookDto bookDto) {

        BookEntity bookEntity = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));

        bookEntity.setBookName(bookDto.getBookName());
        bookEntity.setAuthorName(bookDto.getAuthorName());
        bookEntity.setGenre(bookDto.getGenre());
        BookEntity updatedBook = this.bookRepository.save(bookEntity);
        BookDto updatedBook1 =  this.bookEntityToDto(updatedBook);

        return updatedBook1;
    }


    @Override
    public BookDto getBookById(Long bookId) {
        BookEntity bookEntity = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));

        return this.bookEntityToDto(bookEntity);
    }



    private BookEntity dtoToBookEntity(BookDto bookDto){
        BookEntity bookEntity = this.modelMapper.map(bookDto,BookEntity.class);

        return bookEntity;
    }

    private BookDto bookEntityToDto(BookEntity bookEntity){

        BookDto bookDto = this.modelMapper.map(bookEntity,BookDto.class);

        return bookDto;
    }
}


