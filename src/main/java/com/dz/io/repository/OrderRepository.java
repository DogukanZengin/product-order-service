package com.dz.io.repository;

import com.dz.io.domain.SalesOrder;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository  extends CrudRepository<SalesOrder, Long> {

    List<SalesOrder> findAllByCreationDateBetween(LocalDateTime begin, LocalDateTime end);
}
