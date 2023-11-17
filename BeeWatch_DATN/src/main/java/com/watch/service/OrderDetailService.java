package com.watch.service;

import com.watch.entity.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {

	OrderDetail getById(Integer id);

	void delete(OrderDetail entity);

	void deleteById(Integer id);

	Optional<OrderDetail> findById(Integer id);

	List<OrderDetail> findAll();

	<S extends OrderDetail> S save(S entity);

}
