package com.onlinebookshop.BookService.service.impl;

import com.onlinebookshop.BookService.entity.BookEntity;
import com.onlinebookshop.BookService.exceptions.ResourceNotFoundException;
import com.onlinebookshop.BookService.model.BookDto;
import com.onlinebookshop.BookService.model.Inventory;
import com.onlinebookshop.BookService.repository.BookRepository;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    private Inventory inventory = new Inventory();

    HttpHeaders headers = new HttpHeaders();

    private Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);


    @Override
    public BookDto create(BookDto bookDto) {

        BookEntity bookEntity = this.dtoToBookEntity(bookDto);

        BookEntity savedBookEntity = bookRepository.save(bookEntity);

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        inventory.setBookId(bookDto.getBookId());
        inventory.setPrice(bookDto.getPrice());
        inventory.setQuantity(bookDto.getQuantity());

        ResponseEntity<Inventory> responseEntity = restTemplate.postForEntity("http://INVENTORY-SERVICE/book-inventory/create",inventory,Inventory.class);


        return this.bookEntityToDto(savedBookEntity);


    }

    @Override
    public List<BookDto> getAllBooks() {

        List<BookEntity> bookEntities =  this.bookRepository.findAll();
        List<BookDto> bookDtos = bookEntities.stream().map(book -> this.bookEntityToDto(book)).collect(Collectors.toList());
        for(BookDto bookDto : bookDtos) {
            System.out.println(bookDto.getBookId());
            Integer bookId = bookDto.getBookId();
            Inventory inventory = restTemplate.getForObject("http://INVENTORY-SERVICE/book-inventory/45", Inventory.class);
            System.out.println(inventory.getPrice());

            bookDto.setPrice(inventory.getPrice());
            bookDto.setQuantity(inventory.getQuantity());
        }
//            logger.info("response status code: {} ",inventory);
//            System.out.println(inventory.getPrice());

        return bookDtos;

    }



    @Override
    public void delete(Long bookId) {

        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));

//        Inventory inventory = restTemplate.getForObject("http://INVENTORY-SERVICE/book-inventory/delete/450", Inventory.class);

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


        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        inventory.setBookId(bookDto.getBookId());
        inventory.setPrice(bookDto.getPrice());
        inventory.setQuantity(bookDto.getQuantity());

        ResponseEntity<Inventory> responseEntity = restTemplate.postForEntity("http://INVENTORY-SERVICE/book-inventory/create",inventory,Inventory.class);
        return this.bookEntityToDto(updatedBook);
    }


    @Override
    public BookDto getBookById(Long bookId) {
        BookEntity bookEntity = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));



        Inventory inventory = restTemplate.getForObject("http://INVENTORY-SERVICE/book-inventory/"+bookId, Inventory.class);
        logger.info("response status code: {} ",inventory);
        BookDto bookDto = this.bookEntityToDto(bookEntity);
        bookDto.setPrice(inventory.getPrice());
        bookDto.setQuantity(inventory.getQuantity());


        return bookDto;
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


