package com.onlinebookshop.BookService.model;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class  BuyRequest {

    private Long bookId;
    private Long quantity;

}
