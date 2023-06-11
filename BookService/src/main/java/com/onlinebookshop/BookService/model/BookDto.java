package com.onlinebookshop.BookService.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BookDto {
    private Long bookId;
    private String bookName;
    private String authorName;
    private String genre;
    private double price;
    private Long quantity;
}