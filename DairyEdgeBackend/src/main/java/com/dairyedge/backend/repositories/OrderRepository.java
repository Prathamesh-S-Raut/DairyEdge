package com.dairyedge.backend.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairyedge.backend.entities.Order;
import com.dairyedge.backend.entities.OrderItem;
import com.dairyedge.backend.entities.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

	Collection<Order> findByUserId(Long userId);
}
