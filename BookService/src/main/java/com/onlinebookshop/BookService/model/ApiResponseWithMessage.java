package com.onlinebookshop.BookService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseWithMessage {
    private String message;
    private boolean  success;
}

