package com.mazyfood.order.application.port.out.persistence;


import com.mazyfood.order.model.order.Order;
import com.mazyfood.order.model.order.OrderId;

import java.util.List;
import java.util.Optional;


public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(OrderId orderId);

    List<Order> findAll();

    List<Order> getOrdered();

}
