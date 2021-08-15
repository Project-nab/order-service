package com.icomerce.shopping.order.repositories;

import com.icomerce.shopping.order.entities.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepo extends CrudRepository<Shipment, Long> {
}
