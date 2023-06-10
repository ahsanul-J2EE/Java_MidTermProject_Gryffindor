package com.onlinebookshop.Inventory.Service.service;

import com.onlinebookshop.Inventory.Service.entity.InventoryEntity;
import com.onlinebookshop.Inventory.Service.model.InventoryModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {

    ResponseEntity<InventoryModel> createNewInventory(InventoryEntity inventory);
    ResponseEntity< List<InventoryModel> > showAllInventory(List<Long> bookIds );
    ResponseEntity<InventoryModel> showInventoryByBookId(Long bookId);
    ResponseEntity<InventoryModel> updateInventoryByBookId(Long bookId , InventoryEntity inventory);
    ResponseEntity<InventoryModel> deleteInventoryByBookId(Long bookId);

}
