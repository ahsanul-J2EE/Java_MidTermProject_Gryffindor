package com.onlinebookshop.Inventory.Service.controller;

import com.onlinebookshop.Inventory.Service.entity.InventoryEntity;
import com.onlinebookshop.Inventory.Service.model.InventoryModel;
import com.onlinebookshop.Inventory.Service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book-inventory")

public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/create")
    public ResponseEntity<InventoryModel> CreateNewInventory(@RequestBody InventoryEntity inventory ){
        return inventoryService.CreateNewInventory(inventory);
    }

    @PostMapping("")
    public ResponseEntity<List<InventoryModel>> ShowAllInventory(@RequestBody List<Long> bookIds ){
        return inventoryService.ShowAllInventory( bookIds );
    }


    @GetMapping("/{bookId}")
    public ResponseEntity<InventoryModel> ShowInventoryByBookId(@PathVariable Long bookId){
        return inventoryService.ShowInventoryByBookId(bookId);
    }


    @PutMapping("/update/{bookId}")
    public ResponseEntity<InventoryModel> UpdateInventoryByBookId(@PathVariable Long bookId , @RequestBody InventoryEntity inventory){
        return inventoryService.UpdateInventoryByBookId(bookId, inventory);
    }


    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<InventoryModel> DeleteInventoryById(@PathVariable Long bookId){
        return inventoryService.DeleteInventoryByBookId(bookId);
    }
}
