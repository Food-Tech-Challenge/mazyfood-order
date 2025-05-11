package com.mazyfood.order.adapter.out.persistence.inmemory;

import com.mazyfood.order.application.port.out.persistence.OrderRepository;
import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;
import com.mazyfood.order.model.order.OrderStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "persistence", havingValue = "inmemory", matchIfMissing = true)
@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<OrderId, Order> orders = new ConcurrentHashMap<>();

    private int idSequence = 1;

    public InMemoryOrderRepository() {
    }

    @Override
    public void save(Order order) {
        if (order.getId() == null) {
            order.setId(new OrderId(idSequence));
            idSequence += 1;
        }
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public List<Order> findAll() {
        return orders.values().stream().toList();
    }

    private Comparator<OrderStatus> getStatusComparator() {
        return Comparator.comparingInt(status -> switch (status) {
            case PRONTO -> 1;
            case EM_PREPARO -> 2;
            case RECEBIDO -> 3;
            default -> Integer.MAX_VALUE;
        });
    }

    @Override
    public List<Order> getOrdered() {
        List<Order> orders = this.findAll();
        orders = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.INICIADO && order.getStatus() != OrderStatus.FINALIZADO)
                .sorted(Comparator
                        .comparing(Order::getStatus, getStatusComparator())
                        .thenComparing(Order::getOrderId))
                .toList();
        return orders;
    }
}
