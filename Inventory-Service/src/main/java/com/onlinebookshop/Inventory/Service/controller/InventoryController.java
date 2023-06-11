package com.onlinebookshop.Inventory.Service.controller;

import com.onlinebookshop.Inventory.Service.entity.InventoryEntity;
import com.onlinebookshop.Inventory.Service.model.InventoryModel;
import com.onlinebookshop.Inventory.Service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book-inventory")

public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/create")
    public ResponseEntity<InventoryModel> createNewInventory(@RequestBody InventoryEntity inventory ){
        return inventoryService.createNewInventory(inventory);
    }

    @PostMapping("")
    public ResponseEntity<List<InventoryModel>> showAllInventory(@RequestBody List<Long> bookIds ){
        return inventoryService.showAllInventory( bookIds );
    }


    @GetMapping("/{bookId}")
    public ResponseEntity<InventoryModel> showInventoryByBookId(@PathVariable Long bookId){
        return inventoryService.showInventoryByBookId(bookId);
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<InventoryModel> updateInventoryByBookId(@PathVariable Long bookId , @RequestBody InventoryEntity inventory){
        return inventoryService.updateInventoryByBookId(bookId, inventory);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<InventoryModel> deleteInventoryById(@PathVariable Long bookId){
        return inventoryService.deleteInventoryByBookId(bookId);
    }
}
