package com.omar.spring_data_aot;


import com.omar.spring_data_aot.coffee.CoffeeRepository;
import com.omar.spring_data_aot.order.OrderItemRepository;
import com.omar.spring_data_aot.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <h2>AOT Repository Validation Test</h2>
 *
 * <p>
 * This test verifies that <strong>Spring Data AOT (Ahead-Of-Time) processing</strong>
 * in <strong>Spring Boot 4.x</strong> correctly detects and generates metadata for
 * <em>all custom repository methods</em>.
 * </p>
 *
 * <h3>Why this test exists</h3>
 * <ul>
 *   <li>
 *     Spring Boot 4 relies heavily on <strong>AOT-generated metadata</strong> to
 *     enable fast startup, reduced reflection, and GraalVM native image support.
 *   </li>
 *   <li>
 *     Spring Data repositories are analyzed at build time, and their derived query
 *     methods are translated into static metadata (JSON).
 *   </li>
 *   <li>
 *     A typo in a method name (e.g. {@code findByStatuz}) or an invalid property
 *     reference may cause Spring Data AOT to silently skip that method.
 *   </li>
 * </ul>
 *
 * <p>
 * This test prevents such issues by <strong>failing the build</strong> when a declared
 * repository method is missing from the generated AOT metadata.
 * </p>
 *
 * <h3>Key Spring Boot 4 / AOT concepts covered</h3>
 * <ul>
 *   <li>Build-time repository analysis</li>
 *   <li>AOT metadata generation under {@code target/classes}</li>
 *   <li>Signature-based method matching (not just method names)</li>
 *   <li>Early feedback during CI instead of runtime failures</li>
 * </ul>
 */
class AotRepositoryValidationTest {

    private static final JsonMapper mapper = JsonMapper.builder().build();

    /**
     * Validates that all custom methods declared in {@link CoffeeRepository}
     * are present in the generated Spring Data AOT metadata.
     */
    @Test
    void coffeeRepositoryMethodsAreAotProcessed() throws IOException {
        validateRepository(CoffeeRepository.class, "com/omar/spring_data_aot/coffee/CoffeeRepository.json");
    }

    /**
     * Validates Spring Data AOT processing for {@link OrderRepository}.
     */
    @Test
    void orderRepositoryMethodsAreAotProcessed() throws IOException {
        validateRepository(OrderRepository.class, "com/omar/spring_data_aot/order/OrderRepository.json");
    }

    /**
     * Validates Spring Data AOT processing for {@link OrderItemRepository}.
     */
    @Test
    void orderItemRepositoryMethodsAreAotProcessed() throws IOException {
        validateRepository(OrderItemRepository.class, "com/omar/spring_data_aot/order/OrderItemRepository.json");
    }


    /**
     * Core validation logic shared by all repository tests.
     *
     * <p>
     * This method compares:
     * </p>
     * <ol>
     *   <li>Custom methods declared directly on the repository interface</li>
     *   <li>Methods listed in the Spring Data AOT-generated metadata</li>
     * </ol>
     *
     * <p>
     * The comparison is done using <strong>method signatures</strong>
     * (method name + parameter types) to correctly handle overloaded methods.
     * </p>
     *
     * @param repositoryClass the Spring Data repository interface
     * @param metadataPath    relative path to the generated AOT metadata JSON file
     *
     * @throws IOException if the metadata file cannot be read
     */
    private void validateRepository(Class<?> repositoryClass, String metadataPath) throws IOException {
        JsonNode metadata = loadMetadata(metadataPath);
        Set<String> declaredSignatures = getCustomMethodSignatures(repositoryClass);
        Set<String> aotProcessedSignatures = extractMethodSignatures(metadata);

        Set<String> skippedSignatures = new HashSet<>(declaredSignatures);
        skippedSignatures.removeAll(aotProcessedSignatures);

        assertTrue(skippedSignatures.isEmpty(),
                "AOT skipped methods in %s: %s".formatted(repositoryClass.getSimpleName(), skippedSignatures));
    }



}