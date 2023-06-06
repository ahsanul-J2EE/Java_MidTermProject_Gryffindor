package com.onlinebookshop.Inventory.Service.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InventoryModel {

    private Double price;
    private Long quantity;
    private Long bookId;

}
