package com.watch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.watch.entity.OrderDetail;
import com.watch.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrdersService {
	List<Orders> findAll();
	Orders createOrderVisting(JsonNode orders) throws Exception;
	Orders create(JsonNode orders);

	Orders getById(Integer id);

	void delete(Orders entity);

	void deleteById(Integer id);

	boolean checkOrders(int id);

	Optional<Orders> findById(Integer id);

	<S extends Orders> S save(S entity);

	List<Orders> findByUsername(String username);

	List<Orders> findByUsername(String username, Pageable pageable);

	List<Orders> getByIdVoucher(Long accountId);

	List<Orders> findByUserId(Long id);

	Page<Orders> getOrderByUserId(Long id, Pageable pageable);

}
