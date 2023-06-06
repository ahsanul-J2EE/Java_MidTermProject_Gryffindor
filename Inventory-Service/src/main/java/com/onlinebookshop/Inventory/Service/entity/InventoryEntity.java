package com.onlinebookshop.Inventory.Service.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "BookInventory")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InventoryEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name = "Inventory_Id")
    private Long inventoryId;

    @Column(name = "Price")
    private Double price;

    @Column(name = "Quantity")
    private Long quantity;

    @Column(name = "Book_Id")
    private Long bookId;

}
