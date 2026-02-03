package com.dairyedge.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairyedge.backend.entities.Order;
import com.dairyedge.backend.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
