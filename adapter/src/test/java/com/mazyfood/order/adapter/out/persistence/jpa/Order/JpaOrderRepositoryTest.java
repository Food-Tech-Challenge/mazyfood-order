package com.mazyfood.order.adapter.out.persistence.jpa.Order;

import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class JpaOrderRepositoryTest {

    private JpaOrderSpringDataRepository springDataRepository;
    private JpaOrderRepository jpaOrderRepository;

    @BeforeEach
    void setUp() {
        springDataRepository = mock(JpaOrderSpringDataRepository.class);
        jpaOrderRepository = new JpaOrderRepository(springDataRepository);
    }

    @Test
    void testSaveCallsMapperAndRepository() {
        Order order = new Order(1);
        OrderJpaEntity jpaEntity = mock(OrderJpaEntity.class);

        try (MockedStatic<OrderMapper> mapperMock = mockStatic(OrderMapper.class)) {
            mapperMock.when(() -> OrderMapper.toJpaEntity(order)).thenReturn(jpaEntity);

            jpaOrderRepository.save(order);

            verify(springDataRepository).save(jpaEntity);
            mapperMock.verify(() -> OrderMapper.toJpaEntity(order));
            mapperMock.verify(() -> OrderMapper.updateModelEntity(order, jpaEntity));
        }
    }

    @Test
    void testFindByIdReturnsMappedOrder() {
        OrderJpaEntity jpaEntity = mock(OrderJpaEntity.class);
        Order modelOrder = mock(Order.class);
        when(springDataRepository.findById(42)).thenReturn(Optional.of(jpaEntity));

        try (MockedStatic<OrderMapper> mapperMock = mockStatic(OrderMapper.class)) {
            mapperMock.when(() -> OrderMapper.toModelEntity(jpaEntity)).thenReturn(modelOrder);

            Optional<Order> result = jpaOrderRepository.findById(new OrderId(42));

            assertTrue(result.isPresent());
            assertEquals(modelOrder, result.get());
            mapperMock.verify(() -> OrderMapper.toModelEntity(jpaEntity));
        }
    }

    @Test
    void testFindAllReturnsMappedList() {
        List<OrderJpaEntity> jpaEntities = List.of(mock(OrderJpaEntity.class));
        List<Order> modelOrders = List.of(mock(Order.class));

        when(springDataRepository.findAll()).thenReturn(jpaEntities);

        try (MockedStatic<OrderMapper> mapperMock = mockStatic(OrderMapper.class)) {
            mapperMock.when(() -> OrderMapper.toModelEntities(jpaEntities)).thenReturn(modelOrders);

            List<Order> result = jpaOrderRepository.findAll();

            assertEquals(modelOrders, result);
            mapperMock.verify(() -> OrderMapper.toModelEntities(jpaEntities));
        }
    }

    @Test
    void testGetOrderedReturnsMappedList() {
        List<OrderJpaEntity> jpaEntities = List.of(mock(OrderJpaEntity.class));
        List<Order> modelOrders = List.of(mock(Order.class));

        when(springDataRepository.getOrderedJpaEntities()).thenReturn(jpaEntities);

        try (MockedStatic<OrderMapper> mapperMock = mockStatic(OrderMapper.class)) {
            mapperMock.when(() -> OrderMapper.toModelEntities(jpaEntities)).thenReturn(modelOrders);

            List<Order> result = jpaOrderRepository.getOrdered();

            assertEquals(modelOrders, result);
            mapperMock.verify(() -> OrderMapper.toModelEntities(jpaEntities));
        }
    }
}
