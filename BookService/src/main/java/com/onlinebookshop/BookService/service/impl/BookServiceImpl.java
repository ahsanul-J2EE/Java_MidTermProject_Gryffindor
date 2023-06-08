package com.onlinebookshop.BookService.service.impl;

import com.onlinebookshop.BookService.entity.BookEntity;
import com.onlinebookshop.BookService.exceptions.ResourceNotFoundException;
import com.onlinebookshop.BookService.model.BookDto;
import com.onlinebookshop.BookService.model.BuyRequest;
import com.onlinebookshop.BookService.model.Inventory;
import com.onlinebookshop.BookService.repository.BookRepository;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
            Long bookId = bookDto.getBookId();
            Inventory inventory = restTemplate.getForObject("http://INVENTORY-SERVICE/book-inventory/45", Inventory.class);
            System.out.println(inventory.getPrice());

            bookDto.setPrice(inventory.getPrice());
            bookDto.setQuantity(inventory.getQuantity());
        }

        return bookDtos;

    }



    @Override
    public void delete(Long bookId) {

        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://INVENTORY-SERVICE/book-inventory/delete/"+bookId,
                HttpMethod.DELETE,
                null,
                Void.class,
                bookId
        );


        bookRepository.delete(bookEntity);

    }

    @Override
    public BookDto updateBookEntity(Long bookId, BookDto bookDto) {
                BookEntity existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookDto.getBookId()));
        existingBook.setBookName(bookDto.getBookName());
        existingBook.setAuthorName(bookDto.getAuthorName());
        existingBook.setGenre(bookDto.getGenre());

        BookEntity updatedBook = bookRepository.save(existingBook);
        Inventory newInventoryModel = new Inventory(bookDto.getPrice(), bookDto.getQuantity(), bookDto.getBookId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<Inventory> requestEntity = new HttpEntity<>(newInventoryModel, headers);

        String url = "http://INVENTORY-SERVICE/book-inventory/update/{bookId}";
        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(bookId)
                .toUri();

        ResponseEntity<Inventory> response = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                requestEntity,
                Inventory.class
        );


        return bookDto;
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

    @Override
    public String buyBook(BuyRequest buyRequest) {

        BookEntity bookEntity = this.bookRepository.findById(buyRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", buyRequest.getBookId()));

        Inventory inventory = restTemplate.getForObject("http://INVENTORY-SERVICE/book-inventory/"+buyRequest.getBookId(), Inventory.class);
        logger.info("response status code: {} ",inventory);
        BookDto bookDto = this.bookEntityToDto(bookEntity);

        if(inventory.getQuantity()>buyRequest.getQuantity()){
            Long bookId = buyRequest.getBookId();
            Long currentQuantity = inventory.getQuantity()- buyRequest.getQuantity();
            Inventory newInventoryModel = new Inventory(inventory.getPrice(), currentQuantity,buyRequest.getBookId());

            System.out.println(newInventoryModel);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);


            HttpEntity<Inventory> requestEntity = new HttpEntity<>(newInventoryModel, headers);

            String url = "http://INVENTORY-SERVICE/book-inventory/update/{bookId}";

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(bookId)
                    .toUri();

            ResponseEntity<Inventory> response = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    requestEntity,
                    Inventory.class
            );
        }

        return null;
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


