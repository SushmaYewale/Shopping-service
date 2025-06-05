package com.rudraprogram.inventory_service.service;

import com.rudraprogram.inventory_service.model.Inventory;
import com.rudraprogram.inventory_service.repository.InventoryRepository;
import dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private  final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public  List<InventoryResponse> isInStock(List<String> skuCode){

        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(this::mapToInventoryResponse).toList();
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory) {

        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity()>0)
                .build();
    }
}
