package com.onlinebookshop.Inventory.Service.repository;

import com.onlinebookshop.Inventory.Service.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEntity , Long> {
    InventoryEntity findBybookId(Long BookId);
}
