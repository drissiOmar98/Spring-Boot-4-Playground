package com.omar.spring_data_aot.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OrderItemRepositoryTest {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    void findByOrderId_shouldFindItemsForOrder() {
        List<OrderItem> results = orderItemRepository.findByOrderId(1L);

        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(item -> item.orderId().equals(1L));
    }

    @Test
    void findByOrderId_shouldReturnEmptyForNonExistentOrder() {
        List<OrderItem> results = orderItemRepository.findByOrderId(999L);

        assertThat(results).isEmpty();
    }

    @Test
    void findOrderItemsWithCoffeeDetails_shouldFindItemsOrderedByCoffeeName() {
        List<OrderItem> results = orderItemRepository.findOrderItemsWithCoffeeDetails(1L);

        assertThat(results).isNotEmpty();
        assertThat(results).allMatch(item -> item.orderId().equals(1L));
    }

    @Test
    void findOrderItemsWithCoffeeDetails_shouldReturnEmptyForNonExistentOrder() {
        List<OrderItem> results = orderItemRepository.findOrderItemsWithCoffeeDetails(999L);

        assertThat(results).isEmpty();
    }
}