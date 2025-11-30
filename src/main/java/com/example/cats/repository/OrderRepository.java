package com.example.cats.repository;

import com.example.cats.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.hibernate.annotations.NaturalId;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);
}

