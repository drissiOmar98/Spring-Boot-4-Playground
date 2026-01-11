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


    /**
     * Loads the Spring Data AOT metadata file generated at build time.
     *
     * <p>
     * In Spring Boot 4, repository metadata is generated under
     * {@code target/classes} as part of AOT processing.
     * </p>
     *
     * @param relativePath classpath-relative location of the metadata file
     * @return parsed JSON metadata
     *
     * @throws IOException if the file cannot be read
     */
    private JsonNode loadMetadata(String relativePath) throws IOException {
        Path path = Paths.get("target/classes", relativePath);
        assertTrue(Files.exists(path), "AOT metadata not found: " + path);
        return mapper.readTree(path.toFile());
    }


    /**
     * Extracts method signatures from the AOT metadata JSON.
     *
     * <p>
     * Spring Data AOT stores both:
     * </p>
     * <ul>
     *   <li>Method name</li>
     *   <li>Full JVM method signature</li>
     * </ul>
     *
     * <p>
     * These are normalized into a comparable {@code methodName(param1,param2)} form.
     * </p>
     *
     * @param metadata parsed AOT metadata
     * @return set of processed method signatures
     */
    private Set<String> extractMethodSignatures(JsonNode metadata) {
        return StreamSupport.stream(metadata.get("methods").spliterator(), false)
                .map(method -> {
                    String name = method.get("name").asText();
                    String signature = method.get("signature").asText();
                    return buildSignatureKey(name, signature);
                })
                .collect(Collectors.toSet());
    }


    /**
     * Extracts all <strong>custom</strong> method signatures declared in a repository.
     *
     * <p>
     * CRUD methods inherited from {@link CrudRepository} are ignored, since they
     * are always handled by Spring Data automatically.
     * </p>
     *
     * @param repositoryClass the repository interface
     * @return set of custom method signatures
     */
    private Set<String> getCustomMethodSignatures(Class<?> repositoryClass) {
        return Arrays.stream(repositoryClass.getDeclaredMethods())
                .filter(method -> !isInheritedCrudMethod(method))
                .map(this::buildSignatureKey)
                .collect(Collectors.toSet());
    }


    /**
     * Builds a normalized signature key from a Java {@link Method}.
     *
     * <p>
     * Example:
     * </p>
     * <pre>
     * findByStatusAndDate(Status, LocalDate)
     * </pre>
     *
     * @param method repository method
     * @return normalized signature string
     */
    private String buildSignatureKey(Method method) {
        String paramTypes = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));
        return "%s(%s)".formatted(method.getName(), paramTypes);
    }

    /**
     * Builds a normalized signature key from AOT metadata values.
     *
     * <p>
     * The full signature provided by Spring Data AOT looks like:
     * </p>
     * <pre>
     * public abstract ReturnType ClassName.methodName(ParamType1,ParamType2)
     * </pre>
     *
     * <p>
     * This method extracts parameter types and converts them to simple class names
     * to match reflection-based signatures.
     * </p>
     *
     * @param methodName   repository method name
     * @param fullSignature JVM-level method signature
     * @return normalized signature string
     */
    private String buildSignatureKey(String methodName, String fullSignature) {
        // Parse parameter types from full signature
        // Format: "public abstract ReturnType ClassName.methodName(ParamType1,ParamType2)"
        int paramsStart = fullSignature.indexOf('(');
        if (paramsStart == -1) {
            return "%s()".formatted(methodName);
        }

        int paramsEnd = fullSignature.indexOf(')', paramsStart);
        String params = fullSignature.substring(paramsStart + 1, paramsEnd);

        if (params.isEmpty()) {
            return "%s()".formatted(methodName);
        }

        // Extract simple class names from fully qualified parameter types
        String simplifiedParams = Arrays.stream(params.split(","))
                .map(String::trim)
                .map(this::extractSimpleClassName)
                .collect(Collectors.joining(","));

        return "%s(%s)".formatted(methodName, simplifiedParams);
    }

    /**
     * Extracts the simple class name from a fully qualified name.
     *
     * @param fullyQualifiedName e.g. {@code java.time.LocalDate}
     * @return simple class name e.g. {@code LocalDate}
     */
    private String extractSimpleClassName(String fullyQualifiedName) {
        int lastDot = fullyQualifiedName.lastIndexOf('.');
        return lastDot == -1 ? fullyQualifiedName : fullyQualifiedName.substring(lastDot + 1);
    }

    /**
     * Determines whether a method is inherited from {@link CrudRepository}.
     *
     * <p>
     * These methods are excluded from validation because they are always
     * supported by Spring Data and not part of custom query derivation.
     * </p>
     *
     * @param method repository method
     * @return {@code true} if inherited from CrudRepository
     */
    private boolean isInheritedCrudMethod(Method method) {
        try {
            CrudRepository.class.getMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}