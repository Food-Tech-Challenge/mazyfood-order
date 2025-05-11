package com.mazyfood.order.adapter.out.persistence.jpa.Order;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOrderSpringDataRepository extends JpaRepository<OrderJpaEntity, Integer> {

    @Query("""
                SELECT o
                FROM OrderJpaEntity o
                WHERE o.status NOT IN ('INICIADO', 'FINALIZADO')
                ORDER BY
                  CASE o.status
                    WHEN 'PRONTO' THEN 1
                    WHEN 'EM_PREPARACAO' THEN 2
                    WHEN 'RECEBIDO' THEN 3
                    ELSE 4
                  END,
                  o.id ASC
            """)
    List<OrderJpaEntity> getOrderedJpaEntities();
}
