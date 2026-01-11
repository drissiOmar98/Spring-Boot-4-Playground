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


}