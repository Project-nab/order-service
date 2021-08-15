package com.icomerce.shopping.order.repositories;

import com.icomerce.shopping.order.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepo extends CrudRepository<Order, Long> {
}
