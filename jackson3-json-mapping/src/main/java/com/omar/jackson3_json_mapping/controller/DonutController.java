package com.omar.jackson3_json_mapping.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.omar.jackson3_json_mapping.loader.DataLoader;
import com.omar.jackson3_json_mapping.model.Donut;
import com.omar.jackson3_json_mapping.model.Views;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🍩 DonutController exposes REST endpoints demonstrating
 * hierarchical JSON Views in Jackson 3 with Spring Boot 4.
 *
 * <p>Endpoints show how different API consumers can receive
 * different views of the same Donut object.</p>
 *
 * <p>Also includes a POST endpoint demonstrating deserialization
 * using {@link JsonView} to restrict client input to summary fields.</p>
 */
@RestController
@RequestMapping("/api/donuts")
public class DonutController {

    private static final Logger log = LoggerFactory.getLogger(DonutController.class);
    private final DataLoader dataLoader;

    public DonutController(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    // ---------------------------------------------
    // GET endpoints demonstrating JSON Views
    // ---------------------------------------------

    /**
     * 📄 Summary view endpoint - minimal info for listings
     */

    @GetMapping("/summary")
    @JsonView(Views.Summary.class)
    public List<Donut> getSummary() {
        log.debug("Fetching donuts with Summary view");
        return dataLoader.getDonuts();
    }

    /**
     * 🌐 Public view endpoint - includes summary + public fields
     */
    @GetMapping("/public")
    @JsonView(Views.Public.class)
    public List<Donut> getPublic() {
        log.debug("Fetching donuts with Public view");
        return dataLoader.getDonuts();
    }

    /**
     * 🔒 Internal view endpoint - includes public + internal fields
     */
    @GetMapping("/internal")
    @JsonView(Views.Internal.class)
    public List<Donut> getInternal() {
        log.debug("Fetching donuts with Internal view");
        return dataLoader.getDonuts();
    }

    /**
     * 🛠️ Admin view endpoint - full object for administrative use
     */
    @GetMapping("/admin")
    @JsonView(Views.Admin.class)
    public List<Donut> getAdmin() {
        log.debug("Fetching donuts with Admin view");
        return dataLoader.getDonuts();
    }

    // POST endpoint demonstrating @JsonView for deserialization
    // Only accepts 'type' and 'price' from client (Summary view)
    // Server generates the rest (glaze, toppings, calories, bakedAt, etc.)
    /**
     * 📝 Create a new Donut
     *
     * <p>Client can only send Summary fields (type, price).
     * Server generates the rest of the fields.</p>
     *
     * @param donut incoming donut with limited Summary view fields
     * @return donut with server-generated fields populated
     */
    @PostMapping
    @JsonView(Views.Summary.class)
    public Donut createDonut(@RequestBody @JsonView(Views.Summary.class) Donut donut) {
        log.info("Received donut creation request");
        log.info("  Type: {}", donut.type());
        log.info("  Price: {}", donut.price());
        log.info("  Glaze: {} (should be null - not in Summary view)", donut.glaze());
        log.info("  Toppings: {} (should be null - not in Summary view)", donut.toppings());
        log.info("  IsVegan: {} (should be false - not in Summary view)", donut.isVegan());
        log.info("  Calories: {} (should be null - not in Summary view)", donut.calories());
        log.info("  BakedAt: {} (should be null - not in Summary view)", donut.bakedAt());

        // Server generates the missing fields
        Donut createdDonut = new Donut(
                donut.type(),
                Donut.Glaze.CHOCOLATE,  // Server default
                List.of("sprinkles"),    // Server default
                donut.price(),
                false,                   // Server calculates
                300,                     // Server calculates
                LocalDateTime.now()      // Server sets timestamp
        );

        log.info("Returning created donut with server-generated fields");
        return createdDonut;
    }
}