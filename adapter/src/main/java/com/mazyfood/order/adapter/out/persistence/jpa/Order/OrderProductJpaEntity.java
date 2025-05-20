package com.mazyfood.order.adapter.out.persistence.jpa.Order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_products")
public class OrderProductJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpaEntity order;

    private int productId;

    private String productName;

    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    public boolean isQuantityPositive() {
        return quantity > 0;
    }

    public boolean nonCoveredMethod() {
        return quantity >= 0;
    }
}
