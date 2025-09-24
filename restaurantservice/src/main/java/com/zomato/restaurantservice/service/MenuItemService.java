package com.zomato.restaurantservice.service;

import com.zomato.restaurantservice.model.MenuItem;
import com.zomato.restaurantservice.model.Restaurant;
import com.zomato.restaurantservice.repository.MenuItemRepository;
import com.zomato.restaurantservice.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public MenuItem addMenuItem(Long restaurantId, MenuItem menuItem) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isPresent() && restaurantOpt.get().isActive()) {
            menuItem.setRestaurant(restaurantOpt.get());
            return menuItemRepository.save(menuItem);
        } else {
            throw new RuntimeException("Restaurant not found or inactive");
        }
    }

    public List<MenuItem> getMenu(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
}
