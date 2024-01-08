package com.watch.restController;

import com.fasterxml.jackson.databind.JsonNode;
import com.watch.dao.OrderDetailDao;
import com.watch.dao.OrdersDao;
import com.watch.dao.ProductDao;
import com.watch.dao.VoucherDao;
import com.watch.entity.OrderDetail;
import com.watch.entity.Orders;
import com.watch.entity.Product;
import com.watch.entity.Vouchers;
import com.watch.service.OrdersService;
import com.watch.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private OrdersDao ordersDao;

	@GetMapping()
	public List<Orders> getall(){
//		return ordersService.findAll();
		return ordersDao.getRealOrder();
	}
	@PostMapping()
	public Orders create(@RequestBody JsonNode orders) {
		logger.info("Log CreateOrder order: ", orders.toString());
		return ordersService.create(orders);
	}


	@GetMapping("/infororder/{id}")
	public Orders findInforOrderById(@PathVariable("id") Integer id) {
		Orders o = ordersDao.findInforOrderById(id);
		return o;
	}

	@PutMapping("/updateorder/{id}")
	public Orders updateInforOrder(@PathVariable("id") Integer id, @RequestBody Orders orders) {
		Orders o =ordersDao.findInforOrderById(id);
		o.setTenNn(orders.getTenNn());
		o.setAddress(orders.getAddress());
		o.setSdtNn(orders.getSdtNn());
		return ordersDao.save(o);
	}
	@PostMapping("/visting")
	public Orders createVisting(@RequestBody JsonNode orders) throws Exception {
		logger.info("Log CreateOrderVisiting order: ", orders.toString());
		return ordersService.createOrderVisting(orders);
	}

	@Autowired
	OrdersDao dao;
	@Autowired
	OrderDetailDao orderDetailDao;
	@Autowired
	public JavaMailSender emailSender;

	@GetMapping("/all")
	public List<Orders> getAll() {
//		return ordersDao.findByStatusNotNull();
		System.out.println(ordersDao.getRealOrder().get(0).getTthaiThanhToan());
		return ordersDao.getRealOrder();
	}


	@GetMapping("/{id}")
	public List<OrderDetail> findOrderById(@PathVariable("id") Integer id) {

		return orderDetailDao.findOrderDetailById(id);
	}

	@PutMapping("/up/{id}")
	public Orders updateStatus1(@PathVariable("id") Integer id, Principal principal) {
		Orders order = dao.getById(id);
		String email = dao.getEmail(id);
		logger.info("Get email");
		if(email == null) {
			email = dao.getEmailVisiting(id);
			logger.info("1", email);
		}
		int status = order.getStatus();
		if (status == 0) {
			order.setStatus(1);
		} else if (status == 1) {
			order.setStatus(2);
		} else if (status == 2) {
			order.setStatus(3);
			order.setDateStart(new Date());
//			order.setTthaiThanhToan(1);
		}else if( status == 3) {
			order.setDateEnd(new Date());
			order.setStatus(4);
			double tienSG =order.getTienSauGiam()+order.getTienCoc();
			order.setTienSauGiam((float) tienSG);
			order.setTthaiThanhToan(1);
		}else if( status == 5) {
			order.setStatus(6);
			order.setTthaiThanhToan(1);
		}
		sendSimpleEmail(email);
		return dao.save(order);
	}
	@Autowired
	OrderDetailDao detailDao;
	@Autowired
	ProductDao productDao;
	@Autowired
	ProductService productSV;
	@Autowired
	VoucherDao voucherDao;

	@PutMapping("/close/{id}")
	public Orders updateStatus2(@PathVariable("id") Integer id, Principal principal, Authentication authentication) {
		Orders order = dao.getById(id);
		String email = dao.getEmail(id);
		logger.info("Get email");
		if(email == null) {
			email = dao.getEmailVisiting(id);
			logger.info("1", email);
		}
//		int status = order.getStatus();
//		if (status == 1 || status == 2 || status == 3) {
//			order.setStatus(0);
//		}
//		if(order.getTthaiThanhToan() == 1) {
//			order.setTthaiThanhToan(2);
//		}
		if(order.getStatus() == 1 || order.getStatus() == 2 || order.getStatus() == 3 ) {
			order.setStatus(0);
			if(order.getTthaiThanhToan() == 1) {
				order.setTthaiThanhToan(2);
			}
			List<OrderDetail> odt = detailDao.getOdtByOd(id);
//			List<Product> listPro = productDao.getProductByOrders(orderId);

			for(OrderDetail o : odt) {
				Product pro = productDao.getProductByOrderDetail(o.getOrderDetailId());
				pro.setQuantity(pro.getQuantity() + o.getQuantity());
				productSV.save(pro);
			}
			//back lại số lượng sp
			// back lại sl voucher

			if(null != order.getVoucher()) {
				Vouchers voucher = voucherDao.getVoucherWithOrder(order.getVoucher().getVoucherName());
				voucher.setQuantity(voucher.getQuantity() + 1);
				voucherDao.save(voucher);
			}
		}
		System.out.println("Đã hủy đơn: "+id);
		if(order.getTienCoc() != 0) {
			order.setTienCoc(0);
			order.setTthaiThanhToan(3);
		}
		sendSimpleEmail(email);
		return dao.save(order);
	}
	@PutMapping("/saybyerefund/{id}")
	public Orders sayByeRefund(@PathVariable("id") Integer id , @RequestBody Orders updatedOrder) {
		Orders order = dao.getById(id);
		String email= dao.getEmail(id);
		int status = order.getStatus();
		String sendToCuscomer =updatedOrder.getSendToCustomer();
		System.out.println("/n");
		System.out.println("-----------------------------------------");
		System.out.println(sendToCuscomer);
		System.out.println("/n");
		if( status == 5) {
			order.setStatus(4);
			order.setTthaiThanhToan(1);
			order.setSendToCustomer(sendToCuscomer);


		}
		sendSimpleEmail(email);
		return dao.save(order);
	}

	@PutMapping("/down/{id}")
	public Orders updateStatus3(@PathVariable("id") Integer id, Principal principal) {
		Orders order = dao.getById(id);
		String email = dao.getEmail(id);
		logger.info("Get email");
		if(email == null) {
			email = dao.getEmailVisiting(id);
			logger.info("1", email);
		}
		int status = order.getStatus();
		if (status == 3) {
			order.setStatus(2);
		} else if (status == 2) {
			order.setStatus(1);
		}else {
			order.setStatus(0);
		}
		if(order.getTthaiThanhToan() == 1) {
			order.setTthaiThanhToan(2);
		}
		sendSimpleEmail(email);
		return dao.save(order);
	}

	@GetMapping("/searchDH/{tenTk}/{ngayTk}/{tthaiTk}")
	public List<Orders> getDashBoard(@PathVariable("tenTk") String tenTk, @PathVariable("ngayTk") String ngayTk
			,@PathVariable("tthaiTk") String tthaiTk) {

		List<Orders> a = null;
		if(!"null".equals(tenTk) && !"null".equals(ngayTk) && !"null".equals(tthaiTk)) {
			a = ordersDao.getDonHangSearch(tenTk,ngayTk,tthaiTk);
		}else if("null".equals(tenTk) && !"null".equals(ngayTk) && !"null".equals(tthaiTk)) {
			a = ordersDao.getDonHangSearch1(ngayTk,tthaiTk);
		}else if(!"null".equals(tenTk) && "null".equals(ngayTk) && !"null".equals(tthaiTk)) {
			a = ordersDao.getDonHangSearch2(tenTk,tthaiTk);
		}else if(!"null".equals(tenTk) && !"null".equals(ngayTk) && "null".equals(tthaiTk)) {
			a = ordersDao.getDonHangSearch3(tenTk,ngayTk);
		}else if("null".equals(tenTk) && "null".equals(ngayTk) && !"null".equals(tthaiTk)) {
			a = ordersDao.getDonHangSearch4(tthaiTk);
		}else if("null".equals(tenTk) && !"null".equals(ngayTk) && "null".equals(tthaiTk)) {
			a = ordersDao.getDonHangSearch5(ngayTk);
		}else if(!"null".equals(tenTk) && "null".equals(ngayTk) && "null".equals(tthaiTk)) {
			a = ordersDao.getDonHangSearch6(tenTk);
		}else if("null".equals(tenTk) && "null".equals(ngayTk) && "null".equals(tthaiTk)) {
			a = ordersDao.findByStatusNotNull();
		}
		return a;
	}

	public void sendSimpleEmail(String email) {

		// Create a Simple MailMessage.
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Website đồng hồ BEEWATCH.COM");
		message.setText("Đơn hàng đã được thay đổi trạng thái bạn vui lòng kiếm tra");
		emailSender.send(message);
	}
	@PutMapping("/coctien/{id}")
	public Orders cocTien(@PathVariable("id") Integer id, Principal principal, @RequestBody Map<String, Float> requestBody) {
//		String tien=String.valueOf(soTienCoc);
//		Float soTienC =Float.valueOf(tien);
		Float soTienCoc = requestBody.get("soTienCoc");
		Orders order = dao.getById(id);
		String email = dao.getEmail(id);
		logger.info("Get email");
		if(email == null) {
			email = dao.getEmailVisiting(id);
			logger.info("1", email);
		}
		int status = order.getStatus();
		if (status == 0) {
			order.setStatus(1);
		} else if (status == 1) {
			order.setStatus(2);
			double tienSG =order.getTienSauGiam()-soTienCoc;
			order.setTienSauGiam((float) tienSG);
			order.setTienCoc(soTienCoc);
		} else if (status == 2) {
			order.setStatus(3);
//			order.setTthaiThanhToan(1);
		}else if( status == 3) {
			order.setStatus(4);
			order.setTthaiThanhToan(1);
		}else if( status == 5) {
			order.setStatus(6);
			order.setTthaiThanhToan(1);
		}
		sendSimpleEmail(email);
		return dao.save(order);
	}
}
