package com.onlinebookshop.Inventory.Service.service.implementation;

import com.onlinebookshop.Inventory.Service.entity.InventoryEntity;
import com.onlinebookshop.Inventory.Service.model.InventoryModel;
import com.onlinebookshop.Inventory.Service.repository.InventoryRepository;
import com.onlinebookshop.Inventory.Service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class InventoryServiceImplementation implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final ModelMapper modelMapper;



    @Override
    public ResponseEntity<InventoryModel> CreateNewInventory(InventoryEntity inventory) {
        InventoryEntity newInventory = inventoryRepo.save(inventory);

        InventoryModel inventoryModel = this.modelMapper.map( newInventory , InventoryModel.class);
        return new ResponseEntity<>( inventoryModel , HttpStatus.CREATED );
    }

    @Override
    public ResponseEntity< List<InventoryModel> > ShowAllInventory() {

        List<InventoryModel> inventoryModels = inventoryRepo.findAll()
                .stream()
                .map( temp -> modelMapper.map( temp , InventoryModel.class) )
                .collect(Collectors.toList());
        return new ResponseEntity<>(inventoryModels , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InventoryModel> ShowInventoryByBookId(Long bookId) {
        InventoryEntity newInventory = inventoryRepo.findBybookId(bookId);

        InventoryModel inventoryModel = this.modelMapper.map( newInventory , InventoryModel.class);
        return new ResponseEntity<>( inventoryModel, HttpStatus.OK );
    }

    @Override
    public ResponseEntity<InventoryModel> UpdateInventoryByBookId(Long bookId, InventoryEntity inventory) {
        InventoryEntity newInventory = inventoryRepo.findBybookId(bookId);
            newInventory.setPrice(inventory.getPrice());
            newInventory.setQuantity(inventory.getQuantity());
//            newInventory.setBookId(inventory.getBookId());
            inventoryRepo.save(newInventory);

        InventoryModel inventoryModel = this.modelMapper.map( newInventory , InventoryModel.class);
        return new ResponseEntity<>( inventoryModel , HttpStatus.OK );
    }

    @Override
    public ResponseEntity<InventoryModel> DeleteInventoryByBookId(Long bookId) {
        InventoryEntity newInventory = inventoryRepo.findBybookId(bookId);
        inventoryRepo.delete(newInventory);

        InventoryModel inventoryModel = this.modelMapper.map( newInventory , InventoryModel.class );
        return new ResponseEntity<>( inventoryModel , HttpStatus.OK );
//        return new ResponseEntity<>( ConvertEntityToModel(newInventory) , HttpStatus.OK );
    }
}
