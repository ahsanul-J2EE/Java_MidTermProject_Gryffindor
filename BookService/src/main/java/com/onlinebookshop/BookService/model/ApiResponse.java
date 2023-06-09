package com.onlinebookshop.BookService.model;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String message;
    private Object Data;
    private boolean  success;


}
