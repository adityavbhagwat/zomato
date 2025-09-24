package com.zomato.restaurantservice.controller;

import com.zomato.restaurantservice.model.MenuItem;
import com.zomato.restaurantservice.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menu")
public class MenuItemController {

    private final MenuItemService menuService;

    public MenuItemController(MenuItemService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@PathVariable Long restaurantId,
                                                @RequestBody MenuItem menuItem) {
        MenuItem saved = menuService.addMenuItem(restaurantId, menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getMenu(@PathVariable Long restaurantId) {
        List<MenuItem> menu = menuService.getMenu(restaurantId);
        return ResponseEntity.ok(menu);
    }
}
