package com.mazyfood.order.adapter.out.persistence.jpa.Order;

import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(name = "persistance", havingValue = "postgresql", matchIfMissing = true)
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final JpaOrderSpringDataRepository jpaOrderSpringDataRepository;

    public JpaOrderRepository(JpaOrderSpringDataRepository jpaOrderSpringDataRepository) {
        this.jpaOrderSpringDataRepository = jpaOrderSpringDataRepository;
    }

    @Override
    @Transactional
    public void save(Order order) {
        OrderJpaEntity orderJpaEntity = OrderMapper.toJpaEntity(order);
        jpaOrderSpringDataRepository.save(orderJpaEntity);

        OrderMapper.updateModelEntity(order, orderJpaEntity);
    }

    @Override
    @Transactional
    public Optional<Order> findById(OrderId orderId) {
        Optional<OrderJpaEntity> orderJpaEntity = jpaOrderSpringDataRepository.findById(orderId.value());
        return orderJpaEntity.map(OrderMapper::toModelEntity);
    }

    @Override
    @Transactional
    public List<Order> findAll() {
        List<OrderJpaEntity> entities = jpaOrderSpringDataRepository.findAll();
        return OrderMapper.toModelEntities(entities);
    }

    public List<Order> getOrdered() {
        List<OrderJpaEntity> entities = jpaOrderSpringDataRepository.getOrderedJpaEntities();
        return OrderMapper.toModelEntities(entities);
    }
}
