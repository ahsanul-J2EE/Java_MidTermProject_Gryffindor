package com.onlinebookshop.BookService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseModel {


    private String bookName;
    private String authorName;
    private String genre;


}
