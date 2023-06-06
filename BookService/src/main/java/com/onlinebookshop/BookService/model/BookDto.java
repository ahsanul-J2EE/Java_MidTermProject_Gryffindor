package com.onlinebookshop.BookService.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BookDto {

    @NotEmpty
    private String bookName;
    @NotEmpty
    private String authorName;
    @NotEmpty
    private String genre;
    @NotEmpty
    private double price;
    @NotEmpty
    private Integer quantity;


}