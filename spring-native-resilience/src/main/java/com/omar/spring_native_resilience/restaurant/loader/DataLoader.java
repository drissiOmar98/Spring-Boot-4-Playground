package com.omar.spring_native_resilience.restaurant.loader;

import com.omar.spring_native_resilience.restaurant.domain.MenuItem;
import com.omar.spring_native_resilience.restaurant.domain.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final JsonMapper jsonMapper;

    private final Map<String, Restaurant> restaurants = new ConcurrentHashMap<>();
    private final Map<String, MenuItem> menuItems = new ConcurrentHashMap<>();

    public DataLoader(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 Loading Restaurant Data from JSON files...");
        loadData();
        log.info("✅ Data loading completed successfully");
    }

    private void loadData() {
        try {
            // Load restaurants using Jackson 3's JsonMapper
            var restaurantsResource = new ClassPathResource("data/restaurants.json");
            List<Restaurant> restaurantList = jsonMapper.readValue(restaurantsResource.getInputStream(), new TypeReference<>() {});
            restaurantList.forEach(r -> restaurants.put(r.id(), r));
            log.info("  → Loaded {} restaurants", restaurants.size());

            // Load menu items using Jackson 3's JsonMapper
            ClassPathResource menuItemsResource = new ClassPathResource("data/menu-items.json");
            List<MenuItem> menuItemList = jsonMapper.readValue(menuItemsResource.getInputStream(), new TypeReference<>() {});
            menuItemList.forEach(m -> menuItems.put(m.id(), m));
            log.info("  → Loaded {} menu items", menuItems.size());

        } catch (JacksonException e) {
            log.error("Failed to load data from JSON files", e);
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // helper methods for outside classes to access data
    public Map<String, Restaurant> getRestaurants() {
        return restaurants;
    }

    public Map<String, MenuItem> getMenuItems() {
        return menuItems;
    }

    public Restaurant getRestaurant(String id) {
        return restaurants.get(id);
    }

    public MenuItem getMenuItem(String id) {
        return menuItems.get(id);
    }
}