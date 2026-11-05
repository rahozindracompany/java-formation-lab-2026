package com.indra.jpa.inventory.controller;

import java.util.ConcurrentModificationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.indra.jpa.inventory.model.Product;
import com.indra.jpa.inventory.service.InventoryService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final InventoryService inventoryService;

    public ProductController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public Page<Product> search(@RequestParam(required = false) String name, Pageable pageable) {
        return inventoryService.searchProducts(name, pageable);
    }

    @PostMapping("/{productId}/stock")
    public Product increaseStock(@PathVariable Long productId, @RequestParam("quantity") int quantity) {
        return inventoryService.increaseStock(productId, quantity);
    }

    @PostMapping("/{productId}/reservations")
    public ResponseEntity<Void> reserve(@PathVariable Long productId,
            @RequestParam("quantity") int quantity) {
        inventoryService.reserveStock(productId, quantity);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<String> handleConflict(ConcurrentModificationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidReservation(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}