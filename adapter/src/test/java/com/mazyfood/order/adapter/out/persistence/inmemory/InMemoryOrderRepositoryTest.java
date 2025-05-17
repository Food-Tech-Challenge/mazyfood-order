package com.mazyfood.order.adapter.out.persistence.inmemory;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
    }

    @Test
    void testSaveAssignsIdAndStoresOrder() {
        Order order = new Order(123);

        repository.save(order);

        assertNotNull(order.getId());
        Optional<Order> saved = repository.findById(order.getId());
        assertTrue(saved.isPresent());
        assertEquals(123, saved.get().getCustomerId());
    }

    @Test
    void testFindByIdReturnsEmptyIfNotFound() {
        Optional<Order> result = repository.findById(new OrderId(999));
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllReturnsAllOrders() {
        Order o1 = new Order(1);
        repository.save(o1);
        Order o2 = new Order(2);
        repository.save(o2);

        List<Order> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void testGetOrderedFiltersAndSortsCorrectly() {
        Order o1 = new Order(1);
        o1.setStatus(OrderStatus.RECEBIDO);
        Order o2 = new Order(2);
        o2.setStatus(OrderStatus.EM_PREPARO);
        Order o3 = new Order(3);
        o3.setStatus(OrderStatus.PRONTO);
        Order o4 = new Order(4);
        o4.setStatus(OrderStatus.FINALIZADO);
        Order o5 = new Order(5);
        o5.setStatus(OrderStatus.INICIADO);

        repository.save(o1);
        repository.save(o2);
        repository.save(o3);
        repository.save(o4);
        repository.save(o5);

        List<Order> ordered = repository.getOrdered();

        assertEquals(3, ordered.size());
        assertEquals(o3.getOrderId(), ordered.get(0).getOrderId());
        assertEquals(o2.getOrderId(), ordered.get(1).getOrderId());
        assertEquals(o1.getOrderId(), ordered.get(2).getOrderId());
    }

    @Test
    void testStatusComparatorCoversDefault() {
        InMemoryOrderRepository repo = new InMemoryOrderRepository();
        Comparator<OrderStatus> comparator = repo.getStatusComparator();

        int result = comparator.compare(OrderStatus.FINALIZADO, OrderStatus.INICIADO);

        assertEquals(0, result);
    }

    @Test
    void testSaveWithPredefinedIdDoesNotChangeIt() {
        Order order = new Order(123);
        order.setId(new OrderId(99));

        repository.save(order);

        assertEquals(99, order.getOrderId());
        Optional<Order> saved = repository.findById(new OrderId(99));
        assertTrue(saved.isPresent());
    }
}
