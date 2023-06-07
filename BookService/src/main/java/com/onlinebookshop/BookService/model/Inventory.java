package com.onlinebookshop.BookService.model;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Inventory {

    private Double price;
    private Long quantity;
    private Integer bookId;


}
