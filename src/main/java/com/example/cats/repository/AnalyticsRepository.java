package com.example.cats.repository;

import com.example.cats.domain.Order;
import com.example.cats.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Order, Long> {

    @Query("""
        select p.name as name, count(o.id) as timesOrdered
        from Order o join o.items p
        group by p.name
        order by timesOrdered desc
        """)
    List<ProductSalesProjection> findMostPopularProducts();
}
