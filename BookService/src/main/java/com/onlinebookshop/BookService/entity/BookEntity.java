package com.onlinebookshop.BookService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "book_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity {

    @Id
    private Long bookId;


    @Column(nullable = false, length = 100)
    private String bookName;
    private String authorName;
    private String genre;


}