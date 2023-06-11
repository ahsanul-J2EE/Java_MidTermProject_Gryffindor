package com.onlinebookshop.BookService.service.impl;

import com.onlinebookshop.BookService.entity.BookEntity;
import com.onlinebookshop.BookService.exceptions.DatabaseException;
import com.onlinebookshop.BookService.exceptions.ResourceNotFoundException;
import com.onlinebookshop.BookService.model.*;
import com.onlinebookshop.BookService.repository.BookRepository;
import com.onlinebookshop.BookService.service.BookService;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private String baseUrl = "http://INVENTORY-SERVICE/book-inventory/";


    @Override
    @Transactional
    public ResponseEntity<ApiResponse> create(BookDto bookDto) {

        Boolean bookExists = bookRepository.existsById(bookDto.getBookId());
        if (bookExists) {
            throw new DatabaseException("Book Id already Exists");
        }

        BookEntity bookEntity = this.dtoToBookEntity(bookDto);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        BookDto newBookDto = bookEntityToDto(savedBookEntity);

        Inventory inventory = saveDataInInventoryService(bookDto);
        newBookDto.setPrice(inventory.getPrice());
        newBookDto.setQuantity(inventory.getQuantity());

        return new ResponseEntity<>(new ApiResponse("Book created Successfully!!", newBookDto , true), HttpStatus.OK);
    }


    public Inventory  saveDataInInventoryService(BookDto bookDto){
        Inventory inventoryModel = Inventory.builder()
                .bookId(bookDto.getBookId())
                .quantity(bookDto.getQuantity())
                .price(bookDto.getPrice())
                .build();
        try {
            ResponseEntity<Inventory> responseEntity = restTemplate.postForEntity(baseUrl + "/create", inventoryModel, Inventory.class);
        } catch(IllegalStateException ex) {
            throw new DatabaseException("Inventory-Service Down");
        }

        return inventoryModel;
    }

    @Override
    @Transactional
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookEntity> bookEntityList = bookRepository.findAll();
        List<Long> bookIds = bookEntityList
                .stream()
                .map(BookEntity::getBookId)
                .collect(Collectors.toList());

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(bookIds);

        ResponseEntity<List<Inventory>> inventoryResponseEntity =
                restTemplate.exchange (
                        baseUrl,
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<>() {}
                );

        if (inventoryResponseEntity.getStatusCode() == HttpStatus.OK) {
            List<Inventory> responseModel = inventoryResponseEntity.getBody();
            List<BookDto> allBookInfo = new ArrayList<>();

            for (int i = 0; i < responseModel.size(); i++){
                BookEntity bookEntity = bookEntityList.get(i);
                Inventory model = responseModel.get(i);

                BookDto bookModel = BookDto.builder()
                        .bookId(bookEntity.getBookId())
                        .bookName(bookEntity.getBookName())
                        .authorName(bookEntity.getAuthorName())
                        .genre(bookEntity.getGenre())
                        .price(model.getPrice())
                        .quantity(model.getQuantity())
                        .build();

                allBookInfo.add(bookModel);
            }
            return new ResponseEntity<>(allBookInfo, HttpStatus.OK);
        } else {
            throw new RuntimeException("Failed to retrieve book inventory details");
        }
    }


    @Override
    @Transactional
    public ResponseEntity<ApiResponse> getBookById(Long bookId) {
        BookEntity bookEntity =
                bookRepository.findById(bookId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Book", "Id", bookId)
                        );
        Inventory inventory = getBookDetailFromInventory(bookId);
        BookDto bookDto = bookEntityToDto(bookEntity);
        bookDto.setPrice(inventory.getPrice());
        bookDto.setQuantity(inventory.getQuantity());

        return new ResponseEntity(new ApiResponse("This book is in our database!!", bookDto, true), HttpStatus.OK);
    }

    private Inventory getBookDetailFromInventory(Long bookId) {
        Inventory inventory = null;

        try {
            inventory = restTemplate.getForObject(baseUrl + bookId, Inventory.class);
        } catch (Exception ex) {
            throw new DatabaseException("Inventory-Service Down");
        }
        return inventory;

    }

    @Override
    @Transactional
    public void delete(Long bookId) {

        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));
        deleteFromInventory(bookId);
        bookRepository.delete(bookEntity);

    }

    public void deleteFromInventory(Long bookId){
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    baseUrl + "/delete/" + bookId,
                    HttpMethod.DELETE,
                    null,
                    Void.class,
                    bookId
            );
        } catch(IllegalStateException ex) {
            throw new DatabaseException("Inventory-Service Down");
        }

    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updateBookEntity(Long bookId, BookDto bookDto) {
        BookEntity existingBook =
                bookRepository.findById(bookId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Book", "Id", bookDto.getBookId()));

        existingBook.setBookName(bookDto.getBookName());
        existingBook.setAuthorName(bookDto.getAuthorName());
        existingBook.setGenre(bookDto.getGenre());

        BookEntity updatedBook = bookRepository.save(existingBook);
        Inventory newInventoryModel = new Inventory(bookDto.getPrice(), bookDto.getQuantity(), bookDto.getBookId());
        updatePriceAndQuantityInInventoryService(bookId, newInventoryModel);

        return new ResponseEntity(new ApiResponse("Update Successful!!",bookDto,true),HttpStatus.OK);
    }

    private void updatePriceAndQuantityInInventoryService(Long bookId, Inventory newInventoryModel) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Inventory> requestEntity = new HttpEntity<>(newInventoryModel, headers);

        String url = baseUrl + "/update/{bookId}";
        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(bookId)
                .toUri();

        try {
            ResponseEntity<Inventory> response = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    requestEntity,
                    Inventory.class
            );
        } catch (Exception ex) {
            throw new DatabaseException("Failed To Update Inventory");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> buyBook(BuyRequest buyRequest) {

        BookEntity bookEntity =
                bookRepository.findById(buyRequest.getBookId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException ("Book", "Id", buyRequest.getBookId())
                        );

        Inventory inventory = null;
        try {
            inventory = restTemplate.getForObject(baseUrl + buyRequest.getBookId(), Inventory.class);
        } catch (Exception ex) {
            throw new DatabaseException("Inventory-Service Down");
        }

        BookDto bookDto = bookEntityToDto(bookEntity);
        bookDto.setQuantity(buyRequest.getQuantity());
        bookDto.setPrice(buyRequest.getQuantity() * inventory.getPrice());

        if (inventory.getQuantity() > buyRequest.getQuantity()){
            Long bookId = buyRequest.getBookId();
            Long currentQuantity = inventory.getQuantity() - buyRequest.getQuantity();
            Inventory newInventoryModel = Inventory.builder()
                    .bookId(bookId)
                    .price(inventory.getPrice())
                    .quantity(currentQuantity)
                    .build();

            updatePriceAndQuantityInInventoryService(bookId, newInventoryModel);

        } else {
            return new ResponseEntity(
                    new ApiResponse("Out Of Stock!!!",null,false)
                    , HttpStatus.OK);
        }

        return new ResponseEntity(new ApiResponse("Purchase Successful!!", bookDto,true), HttpStatus.OK);
    }



    private BookEntity dtoToBookEntity(BookDto bookDto){
        BookEntity bookEntity = this.modelMapper.map(bookDto, BookEntity.class);
        return bookEntity;
    }

    private BookDto bookEntityToDto(BookEntity bookEntity){
        BookDto bookDto = this.modelMapper.map(bookEntity,BookDto.class);
        return bookDto;
    }
}


