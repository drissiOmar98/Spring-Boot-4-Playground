package com.omar.jackson3_json_mapping.loader;

import com.omar.jackson3_json_mapping.model.Donut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * 🍩 DataLoader for the Donut Shop Demo.
 *
 * <p>This component demonstrates how to load JSON data using Jackson 3
 * and Spring Boot 4's {@link ResourceLoader}. It runs automatically
 * at application startup because it implements {@link CommandLineRunner}.</p>
 *
 * <p>Key features showcased:</p>
 * <ul>
 *     <li>📦 Loading resources from classpath</li>
 *     <li>🧵 Using immutable {@link JsonMapper} for deserialization</li>
 *     <li>👁️ JSON Views respected when deserializing and serializing</li>
 *     <li>🔥 Handling Jackson 3 unchecked exceptions</li>
 *     <li>✅ Validation of serialization via logging</li>
 * </ul>
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private static final String DONUTS_JSON_PATH = "classpath:data/donuts-menu.json";

    // Jackson 3 immutable mapper
    private final JsonMapper jsonMapper;
    private final ResourceLoader resourceLoader;
    private List<Donut> donuts;

    public DataLoader(JsonMapper jsonMapper, ResourceLoader resourceLoader) {
        this.jsonMapper = jsonMapper;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Returns the loaded list of Donuts
     *
     * @return list of {@link Donut}
     */
    public List<Donut> getDonuts() {
        return donuts;
    }

    /**
     * 🔑 Application startup entry point
     *
     * @param args startup args (ignored)
     * @throws Exception any exception during JSON loading
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Loading Donuts \uD83C\uDF69");

        try {
            Resource resource = resourceLoader.getResource(DONUTS_JSON_PATH);

            if(!resource.exists()) {
                log.error("Donut menu file not found at: {}", DONUTS_JSON_PATH);
                return;
            }

            // Deserialize JSON into List<Donut> using Jackson 3
            this.donuts = jsonMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {}
            );

            // Log donuts for debugging/demo purposes
            donuts.forEach(System.out::println);

            // Demonstrate serialization back to JSON (using the configured JsonMapper)
            validateSerialization(donuts);

        } catch (JacksonException e) {
            // Jackson 3: All exceptions extend JacksonException (RuntimeException)
            // This is an unchecked exception, making it easier to use in lambdas
            log.error("Failed to load donut data from JSON file: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // Handle other potential exceptions (e.g., resource loading)
            log.error("Unexpected error loading donut data", e);
            throw e;
        }
    }

    /**
     * ✅ Serialize the first Donut as a demo to validate mapping
     *
     * @param donuts list of Donut
     */
    private void validateSerialization(List<Donut> donuts) {
        log.info("Serializing Donuts \uD83C\uDF69");

        if(!donuts.isEmpty()) {
            try {
                // Get the first donut and serialize it
                String json = jsonMapper.writeValueAsString(donuts.getFirst());

                // NOTE: How are properties in the JSON sorted?
                log.info("\n{}", json);

            } catch (JacksonException e) {
                // Jackson 3: Unchecked exception for serialization errors
                log.error("Failed to serialize donut to JSON: {}", e.getMessage(), e);
            }
        }
    }


}