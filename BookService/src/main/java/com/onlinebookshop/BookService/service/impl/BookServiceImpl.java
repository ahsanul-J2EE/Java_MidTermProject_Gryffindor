package com.onlinebookshop.BookService.service.impl;

import com.onlinebookshop.BookService.entity.BookEntity;
import com.onlinebookshop.BookService.exceptions.ResourceNotFoundException;
import com.onlinebookshop.BookService.model.*;
import com.onlinebookshop.BookService.repository.BookRepository;
import com.onlinebookshop.BookService.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
    HttpHeaders headers = new HttpHeaders();
    private Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    private String baseUrl = "http://INVENTORY-SERVICE/book-inventory/";



    @Override
    public ResponseEntity<ApiResponse> create(BookDto bookDto) {

        BookEntity bookEntity = this.dtoToBookEntity(bookDto);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);

        saveDataInInventoryService(bookDto);
        BookDto newBookDto=bookEntityToDto(savedBookEntity);

        Inventory inventory = getBookDetailFromInventory(newBookDto.getBookId());

        newBookDto.setPrice(inventory.getPrice());
        newBookDto.setQuantity(inventory.getQuantity());


        return new ResponseEntity(new ApiResponse("Book created Successfully!!",bookDto,true),HttpStatus.OK);

    }

    public Inventory saveDataInInventoryService(BookDto bookDto){

        Inventory inventory = new Inventory();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        inventory.setBookId(bookDto.getBookId());
        inventory.setPrice(bookDto.getPrice());
        inventory.setQuantity(bookDto.getQuantity());
        ResponseEntity<Inventory> responseEntity = restTemplate.postForEntity(baseUrl+"/create",inventory,Inventory.class);

        return inventory;
    }

    @Override
    public List<BookDto> getAllBooks() {

        List<BookEntity> bookEntities =  this.bookRepository.findAll();
        List<BookDto> bookDtos = bookEntities.stream().map(book -> this.bookEntityToDto(book)).collect(Collectors.toList());
//        for(BookDto bookDto : bookDtos) {
//            System.out.println(bookDto.getBookId());
//            Long bookId = bookDto.getBookId();
//            Inventory inventory = restTemplate.getForObject("http://INVENTORY-SERVICE/book-inventory/45", Inventory.class);
//            System.out.println(inventory.getPrice());
//
//            bookDto.setPrice(inventory.getPrice());
//            bookDto.setQuantity(inventory.getQuantity());
//        }
        return bookDtos;

    }

    @Override
    public ResponseEntity<ApiResponse> getBookById(Long bookId) {
        BookEntity bookEntity = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));

        Inventory inventory = getBookDetailFromInventory(bookId);

        BookDto bookDto = this.bookEntityToDto(bookEntity);
        bookDto.setPrice(inventory.getPrice());
        bookDto.setQuantity(inventory.getQuantity());
        return new ResponseEntity(new ApiResponse("This book is in our database!!",bookDto,true),HttpStatus.OK);
    }

    private Inventory getBookDetailFromInventory(Long bookId) {
        Inventory inventory = restTemplate.getForObject(baseUrl+bookId, Inventory.class);

        return inventory;

    }



    @Override
    public void delete(Long bookId) {

        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookId));

        deleteFromInventory(bookId);
        bookRepository.delete(bookEntity);

    }


    public void deleteFromInventory(Long bookId){
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl+"/delete/"+bookId,
                HttpMethod.DELETE,
                null,
                Void.class,
                bookId
        );
    }

    @Override
    public ResponseEntity<ApiResponse> updateBookEntity(Long bookId, BookDto bookDto) {
                BookEntity existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", bookDto.getBookId()));
        existingBook.setBookName(bookDto.getBookName());
        existingBook.setAuthorName(bookDto.getAuthorName());
        existingBook.setGenre(bookDto.getGenre());

        BookEntity updatedBook = bookRepository.save(existingBook);

        Inventory newInventoryModel = new Inventory(bookDto.getPrice(), bookDto.getQuantity(), bookDto.getBookId());

        updatePriceAndQuantityInInventoryService(bookId,newInventoryModel);

        return new ResponseEntity(new ApiResponse("Update Successful!!",bookDto,true),HttpStatus.OK);
    }

    private void updatePriceAndQuantityInInventoryService(Long bookId,Inventory newInventoryModel) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Inventory> requestEntity = new HttpEntity<>(newInventoryModel, headers);

        String url = baseUrl+"/update/{bookId}";
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

    @Override
    public ResponseEntity<ApiResponse> buyBook(BuyRequest buyRequest) {

        BookEntity bookEntity = this.bookRepository.findById(buyRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", buyRequest.getBookId()));

        Inventory inventory = restTemplate.getForObject(baseUrl+buyRequest.getBookId(), Inventory.class);

        logger.info("response status code: {} ",inventory);
        BookDto bookDto = this.bookEntityToDto(bookEntity);
        bookDto.setQuantity(buyRequest.getQuantity());
        bookDto.setPrice(buyRequest.getQuantity()*inventory.getPrice());

        if(inventory.getQuantity()>buyRequest.getQuantity()){
            Long bookId = buyRequest.getBookId();
            Long currentQuantity = inventory.getQuantity()- buyRequest.getQuantity();
            Inventory newInventoryModel = new Inventory(inventory.getPrice(), currentQuantity,buyRequest.getBookId());

            System.out.println(newInventoryModel);

            updatePriceAndQuantityInInventoryService(bookId,newInventoryModel);

        }else{

            return new ResponseEntity(new ApiResponse("Out Of Stock!!!",null,false),HttpStatus.OK);

        }

        return new ResponseEntity(new ApiResponse("Purchase Successful!!",bookDto,true),HttpStatus.OK);
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


