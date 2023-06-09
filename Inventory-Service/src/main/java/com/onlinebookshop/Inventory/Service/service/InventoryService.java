package com.onlinebookshop.Inventory.Service.service;

import com.onlinebookshop.Inventory.Service.entity.InventoryEntity;
import com.onlinebookshop.Inventory.Service.model.InventoryModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {

    ResponseEntity<InventoryModel> CreateNewInventory(InventoryEntity inventory);
    ResponseEntity< List<InventoryModel> > ShowAllInventory( List<Long> bookIds );
    ResponseEntity<InventoryModel> ShowInventoryByBookId(Long bookId);
    ResponseEntity<InventoryModel> UpdateInventoryByBookId(Long bookId , InventoryEntity inventory);
    ResponseEntity<InventoryModel> DeleteInventoryByBookId(Long bookId);

}
