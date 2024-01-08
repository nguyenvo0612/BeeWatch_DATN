package com.watch.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watch.dao.CartDao;
import com.watch.dao.OrderDetailDao;
import com.watch.dao.OrdersDao;
import com.watch.dao.VistingOrderDao;
import com.watch.dto.CartDTO;
import com.watch.entity.CartDetail;
import com.watch.entity.OrderDetail;
import com.watch.entity.Orders;
import com.watch.entity.VistingGuest;
import com.watch.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrdersService{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	OrdersDao odao;
	@Autowired
	OrderDetailDao ddao;
	@Autowired
	CartDao cartDao;
	@Autowired
	OrderDetailDao orderDetailDao;
	 @Autowired 
	public  HttpSession session;

	 @Autowired
	 VistingOrderDao vistingOrderDao;

	@Override
	public List<Orders> findByUsername(String username,Pageable pageable) {
		return odao.findByUsername(username);
	}

	@Override
	public List<Orders> findAll() {
		// TODO Auto-generated method stub
		return odao.findAll();
	}

	@Override
	public Orders create(JsonNode orders) {
		ObjectMapper mapper=new ObjectMapper();
		Orders order=mapper.convertValue(orders,Orders.class);
		order.setVoucher(null);
		order.setStatus(1);
		odao.save(order);
		TypeReference<List<OrderDetail>> type=new TypeReference<List<OrderDetail>>() {};
		List<OrderDetail> details= mapper.convertValue(orders.get("orderDetails"),type)
				.stream().peek(d->d.setOrder(order)).collect(Collectors.toList());
		List<CartDetail> listToPay = cartDao.cartDetailForPay(order.getAccount().getAccountId());
		List<OrderDetail> listLoginToPay = new ArrayList<>();
		for(CartDetail dto:listToPay){
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setProduct(dto.getProductCartDetail());
			orderDetail.setQuantity(dto.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setPrice(dto.getProductCartDetail().getPrice());
			listLoginToPay.add(orderDetail);
		}
		System.out.println(listLoginToPay);
		ddao.saveAll(listLoginToPay);
		session.setAttribute("listOrderDetail", listLoginToPay);
		Orders order1 = odao.getGanNhat(order.getAccount().getAccountId());
		if(order1.equals(null)) {
			return order;
		}
		int maOder = order1.getOrderId();
		session.setAttribute("OrderganNhat", order1);
		return order1;
	}

	public boolean validateOrder() {


		return true;
	}

	@Override
	public Orders createOrderVisting(JsonNode orders) throws Exception {
		ObjectMapper mapper=new ObjectMapper();
		VistingGuest vistingGuest = new VistingGuest();
		vistingOrderDao.save(vistingGuest);
		Orders order=mapper.convertValue(orders,Orders.class);
		VistingGuest vistingGuest1 = vistingOrderDao.getVistingGuest();
		if(vistingGuest1 == null) {
			throw new Exception("Loi SQL");
		}
		System.out.println(vistingGuest1.toString());
		order.setVoucher(null);
		order.setStatus(1);
		order.setVistingGuest(vistingGuest1);
		odao.save(order);
		List<OrderDetail> orderDetails = new ArrayList<>();
		TypeReference<List<OrderDetail>> type=new TypeReference<List<OrderDetail>>() {};
		List<OrderDetail> details= mapper.convertValue(orders.get("orderDetails"),type)
				.stream().peek(d->d.setOrder(order)).collect(Collectors.toList());
		for(OrderDetail od : details){
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setProduct(od.getProduct());
			orderDetail.setQuantity(od.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setPrice(od.getProduct().getPrice());
			orderDetails.add(orderDetail);
		}
		logger.info(String.valueOf(orderDetails.size()));
		for (OrderDetail od : orderDetails) {
			logger.warn(od.toString());
		}
//		ddao.saveAll(orderDetails);
		session.setAttribute("listOrderDetail", details);
//		Orders order1 = odao.getGanNhat(order.getAccount().getAccountId());
//		if(order1.equals(null)) {
//			return order;
//		}
		int maOder = order.getOrderId();
		session.setAttribute("OrderganNhat", order);
		return order;
	}

	@Override
	public <S extends Orders> S save(S entity) {
		return odao.save(entity);
	}

	@Override
	public Optional<Orders> findById(Integer id) {
		return odao.findById(id);
	}

	@Override
	public void deleteById(Integer id) {
		odao.deleteById(id);
	}

	@Override
	public boolean checkOrders(int id) {
		if(odao.checkProductBeforePay(id) > 0) {
		return false;
		}
		return true;
	}

	@Override
	public void delete(Orders entity) {
		odao.delete(entity);
	}

	@Override
	public Orders getById(Integer id) {
		return odao.getById(id);
	}

	@Override
	public List<Orders> findByUsername(String username) {
		// TODO Auto-generated method stub
		return odao.findByUsername(username);
	}

	@Override
	public List<Orders> getByIdVoucher(Long accountId) {
		// TODO Auto-generated method stub
		return odao.getByIdVoucher(accountId);
	}

	@Override
	public List<Orders> findByUserId(Long id) {
		// TODO Auto-generated method stub
		return odao.findByUserId(id);
	}

	@Override
	public Page<Orders> getOrderByUserId(Long id, Pageable pageable) {
		// TODO Auto-generated method stub
		return odao.findByUserId2(id,pageable);
	}
}