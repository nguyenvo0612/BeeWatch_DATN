package com.watch.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.watch.dao.*;
import com.watch.dto.CartDTO;
import com.watch.entity.*;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.watch.config.VnpayConfig;
import com.watch.service.AccountService;
import com.watch.service.BrandService;
import com.watch.service.OrdersService;
import com.watch.service.ProductService;
import com.watch.service.SizeService;
import com.watch.service.StrapService;
import com.watch.service.VoucherService;

@Controller
@EnableAsync
public class OrderController {

	@Autowired
	OrdersService  ordersService;
	@Autowired
	VoucherService voucherService;
	@Autowired
	AccountService accountService;
	
	@Autowired
	ProductService  productService;
	
	@Autowired
    public JavaMailSender emailSender;
	
	@Autowired
    HttpServletRequest req;

    @Autowired
    HttpServletResponse resp;
	
    @Autowired 
	public  HttpSession session;
    @Autowired
    UserAcounts useAcc;
    @Autowired
    OrdersService orderService;
    @Autowired
    VoucherDao voucherDao;
    @Autowired
    OrdersDao orderDao;
    @Autowired
	OrdersDao odao;
	@Autowired
	CartDao cartDao;
	@Autowired
	CartDetailDao cartDetailDao;
    @Autowired
	SizeService sizeSV;
	@Autowired
	StrapService strapSv;
	@Autowired
	BrandService brandService;
    Accounts account;
    @Autowired
	VistingOrderDao vistingOrderDao;

    @Autowired
    OrderDetailDao orderDetailDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/beestore/order/checkout")
	public String checkout(Model model, Principal principal) {

		if(principal !=null) {
			if(useAcc.User()==null) {
				return "redirect:/login";
			}else {
				String username = principal.getName();
				Optional<Accounts> user = accountService.findByUsername(username);
				List<CartDTO> cartItems = cartDao.cartDTO(username);
				Long cartTotal = cartDao.cartQuantity(username);
				Long totalPrice = cartDao.totalPrice(username);
				model.addAttribute("cartItems", cartItems);
				model.addAttribute("cartTotal", cartTotal);
				model.addAttribute("totalPrice", totalPrice);
				model.addAttribute("account",account);
				model.addAttribute("isAccount", 1);

				List<Product> list = productService.findTop6Img();
				model.addAttribute("items", list);
				List<Strap_material> straps = strapSv.findAll();
				model.addAttribute("straps", straps);
				List<Size> sizes = sizeSV.findAll();
				model.addAttribute("sizes",sizes);

				List<Brand> listBrand = brandService.findAll();
				model.addAttribute("brands", listBrand);
//			List<Vouchers> voucher=voucherService.findAll();
				account = useAcc.User();
				Accounts account1 = new Accounts();
				account1 = accountService.getById(account.getAccountId());
				List<Vouchers> voucher = voucherDao.getVoucherWithAcc(account.getAccountId());

				model.addAttribute("account", account1);
				model.addAttribute("cates", voucher);
				model.addAttribute("isAccount", 1);
				return "/user/vnp/ThanhToan";
			}
		}
		model.addAttribute("isAccount", 0);
		List<Product> list = productService.findTop6Img();
		model.addAttribute("items", list);
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		model.addAttribute("isAccount", 0);
//			List<Vouchers> voucher=voucherService.findAll();
//		account = useAcc.User();
		VistingGuest visting = new VistingGuest();
		model.addAttribute("visting", visting);
//		account1 = accountService.getById(account.getAccountId());
//		List<Vouchers> voucher = voucherDao.getVoucherWithAcc(account.getAccountId());
		model.addAttribute("cates", "voucher");
		return "/user/vnp/test";

	}

	@PostMapping("/beestore/order/checkout")
	@ResponseBody
	public String checkoutPost(Model model, @ModelAttribute("account") Accounts accountKh,Principal principal) throws IOException{
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		if(useAcc.User()==null) {
			return "redirect:/login";
		}else {
			System.out.println("acount: "+ accountKh);
			/*
			 * accounts.setAccountId(account.getAccountId());
			 * accounts.setAddress(accountKh.getAddress());
			 * accounts.setEmail(accountKh.getEmail());
			 * accounts.setFullname(accountKh.getFullname());
			 * accounts.setPhone(accountKh.getPhone());
			 * accounts.setImage(account.getImage());
			 * accounts.setPassword(account.getPassword());
			 * accounts.setUsername(account.getUsername());
			 * accounts.setBirthdate(account.getBirthdate());
			 */
			//accountService.save(accounts);
			session.setAttribute("accountKh", accountKh);

			String pttt = req.getParameter("httt_ma");
			session.setAttribute("pttt", pttt);

			System.out.println("pttt: "+ pttt);
			String maVoucher = req.getParameter("rdoResult");
			session.setAttribute("maVoucher", maVoucher);
			System.out.println("maVoucher: "+ maVoucher);



			String vnp_Version = "2.1.0";
			String vnp_Command = "pay";
			String orderType = req.getParameter("ordertype");
			long amount = Long.parseLong(String.valueOf(Math.round(Double.parseDouble(req.getParameter("amount")))))*100;
			String bankCode = req.getParameter("bankCode");

			String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
			String vnp_IpAddr = VnpayConfig.getIpAddress(req);
			String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

			Map<String, String> vnp_Params = new HashMap<>();
			vnp_Params.put("vnp_Version", vnp_Version);
			vnp_Params.put("vnp_Command", vnp_Command);
			vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
			vnp_Params.put("vnp_Amount", String.valueOf(amount));
			vnp_Params.put("vnp_CurrCode", "VND");
			System.out.println("tong tien: "+vnp_Params.get("vnp_Amount"));
			if (bankCode != null && !bankCode.isEmpty()) {
				vnp_Params.put("vnp_BankCode", bankCode);
			}
			vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
			vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
			vnp_Params.put("vnp_OrderType", orderType);

			String locate = req.getParameter("language");
			if (locate != null && !locate.isEmpty()) {
				vnp_Params.put("vnp_Locale", locate);
			} else {
				vnp_Params.put("vnp_Locale", "vn");
			}
			vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_Returnurl);
			vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

			Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String vnp_CreateDate = formatter.format(cld.getTime());
			vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

			cld.add(Calendar.MINUTE, 15);
			String vnp_ExpireDate = formatter.format(cld.getTime());
			vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

			List fieldNames = new ArrayList(vnp_Params.keySet());
			Collections.sort(fieldNames);
			StringBuilder hashData = new StringBuilder();
			StringBuilder query = new StringBuilder();
			Iterator itr = fieldNames.iterator();
			while (itr.hasNext()) {
				String fieldName = (String) itr.next();
				String fieldValue = (String) vnp_Params.get(fieldName);
				if ((fieldValue != null) && (fieldValue.length() > 0)) {
					//Build hash data
					hashData.append(fieldName);
					hashData.append('=');
					hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
					//Build query
					query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
					query.append('=');
					query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
					if (itr.hasNext()) {
						query.append('&');
						hashData.append('&');
					}
				}
			}
			com.google.gson.JsonObject job = new JsonObject();
			if(pttt.equals("1")) {
				logger.info("Log thanh toán phương thức thanh toán 1");
				Orders order = (Orders) session.getAttribute("OrderganNhat");
				System.out.println(order.toString());
				Accounts ttkh = (Accounts) session.getAttribute("accountKh");
				String address = ttkh.getAddress();
				String maVoucher2 = (String) session.getAttribute("maVoucher");
				//int amount = (int) session.getAttribute("amount");
				order.setAddress(address);

				if(maVoucher2== null || maVoucher2.equals("")) {
					order.setVoucher(null);
				}else {
					Vouchers vouchers = voucherService.getById(maVoucher2);
					if(order.getVoucher() != vouchers || order.getVoucher() == null) {
						/* check xem đã có voucher chưa */
						List<Orders> lst = orderService.getByIdVoucher(order.getAccount().getAccountId());
						if(lst.isEmpty()) {
							int slVouchertru = vouchers.getQuantity() - 1;
							vouchers.setQuantity(slVouchertru);

//								Long amountTongg = (long) (order.getTotal() - ((order.getTotal()*vouchers.getValued())/100));
							Long amountTongg = (long) (order.getTotal() - vouchers.getValued());
							order.setTotal(amountTongg);
							if(slVouchertru <= 0) {
								vouchers.setStatustt(false);
							}
							voucherService.save(vouchers);
							order.setVoucher(vouchers);
						}else {
							int stt=0;
							for (int i = 0; i < lst.size(); i++) {
								String voucherName = lst.get(i).getVoucher().getVoucherName();
								if(voucherName.equals(maVoucher2)) {
									stt += 1;
								}
							}
							if(stt == 0) {
								int slVouchertru = vouchers.getQuantity() - 1;
								vouchers.setQuantity(slVouchertru);

//									Long amountTongg = (long) (order.getTotal() - ((order.getTotal()*vouchers.getValued())/100));
								Long amountTongg = (long) (order.getTotal() - vouchers.getValued());
								order.setTotal(amountTongg);
								if(slVouchertru <= 0) {
									vouchers.setStatustt(false);
								}

								voucherService.save(vouchers);
								order.setVoucher(vouchers);
							}else {
								order.setVoucher(null);
							}
						}
					}else {
						order.setVoucher(null);
					}
					//order.setTotal(amount);
				}
				order.setStatus(1);
				order.setCreateDate(new Date());
				String username = principal.getName();
				Long totalPrice = cartDao.totalPrice(username);
				model.addAttribute("totalPrice", totalPrice);
				order.setTenNn(accountKh.getFullname());
				order.setSdtNn(accountKh.getPhone());
//					order.setEmailNn(accountKh.getEmail());
//					order.setDiaChiNn(accountKh.getAddress());
				//order.setTotal(amount1);
//				order.setOrderId(order.getOrderId());
				System.out.println(order);
				order.setTotal(totalPrice);
				order.setTthaiThanhToan(0);
				orderService.save(order);
				if(order.getTenNn().equals(null) || order.getSdtNn().equals(null) || order.getAddress().equals(null)) {
					//chỗ này chỉnh sửa đơn ảo
				}
				String paymentUrl = VnpayConfig.abc;
				job.addProperty("code", "00");
				job.addProperty("message", "success");
				job.addProperty("data", paymentUrl);
			}else {
				String queryUrl = query.toString();
				String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.vnp_HashSecret, hashData.toString());
				queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
				String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
				job.addProperty("code", "00");
				job.addProperty("message", "success");
				job.addProperty("data", paymentUrl);
			}


			Gson gson = new Gson();
//		        resp.getWriter().write(gson.toJson(job));

			return gson.toJson(job);
		}

	}

	@PostMapping("/beestore/order/checkoutVisting")
	@ResponseBody
	public String checkoutPostVisting(Model model, @ModelAttribute("visiting") VistingGuest visiting) throws IOException{
		logger.info("Log check out order visiting: ", visiting);
		VistingGuest vistingOrder = vistingOrderDao.getVistingGuestOrders();
		vistingOrder.setEmail(visiting.getEmail());
		vistingOrder.setFullname(visiting.getFullname());
		vistingOrder.setPhone(visiting.getPhone());
		vistingOrder.setAddress(visiting.getAddress());
		vistingOrderDao.save(vistingOrder);
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		logger.info("Log account thanh toán");
		System.out.println("acount: "+ visiting);
		logger.info("Log lấy order visiting:");
		Orders orders = (Orders) session.getAttribute("OrderganNhat");
			logger.info("Order visiting is null");
			logger.info("Order visiting : ", orders.toString());
			logger.info(orders.toString());
			logger.info("Set info order");
			orders.setTenNn(visiting.getFullname());
			orders.setSdtNn(visiting.getPhone());
		/*
		 * accounts.setAccountId(account.getAccountId());
		 * accounts.setAddress(accountKh.getAddress());
		 * accounts.setEmail(accountKh.getEmail());
		 * accounts.setFullname(accountKh.getFullname());
		 * accounts.setPhone(accountKh.getPhone());
		 * accounts.setImage(account.getImage());
		 * accounts.setPassword(account.getPassword());
		 * accounts.setUsername(account.getUsername());
		 * accounts.setBirthdate(account.getBirthdate());
		 */
		//accountService.save(accounts);
		session.setAttribute("accountKh", visiting);

		String pttt = req.getParameter("httt_ma");
		session.setAttribute("pttt", pttt);

		logger.info("Log thông tin thanh toán");
		System.out.println("pttt: "+ pttt);
		String maVoucher = req.getParameter("rdoResult");
		session.setAttribute("maVoucher", maVoucher);
		System.out.println("maVoucher: "+ maVoucher);


		logger.info("Log VNPay");
		String vnp_Version = "2.1.0";
		String vnp_Command = "pay";
		String orderType = req.getParameter("ordertype");
		long amount = Long.parseLong(String.valueOf(Math.round(Double.parseDouble(req.getParameter("amount")))))*100;
		String bankCode = req.getParameter("bankCode");

		String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
		String vnp_IpAddr = VnpayConfig.getIpAddress(req);
		String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");
		System.out.println("tong tien: "+vnp_Params.get("vnp_Amount"));
		if (bankCode != null && !bankCode.isEmpty()) {
			vnp_Params.put("vnp_BankCode", bankCode);
		}
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
		vnp_Params.put("vnp_OrderType", orderType);

		String locate = req.getParameter("language");
		if (locate != null && !locate.isEmpty()) {
			vnp_Params.put("vnp_Locale", locate);
		} else {
			vnp_Params.put("vnp_Locale", "vn");
		}
		vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_Returnurl);
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				//Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				//Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		com.google.gson.JsonObject job = new JsonObject();
		if(pttt.equals("1")) {
			logger.info("Log phương thức thanh toán 1");
			Orders order = (Orders) session.getAttribute("OrderganNhat");
			logger.info(order.toString());
			VistingGuest ttkh = (VistingGuest) session.getAttribute("accountKh");
			String address = ttkh.getAddress();
			String maVoucher2 = (String) session.getAttribute("maVoucher");
			//int amount = (int) session.getAttribute("amount");
			order.setAddress(visiting.getAddress());
			order.setTenNn(visiting.getFullname());
			order.setSdtNn(visiting.getPhone());

			logger.info("Log set voucher");
			if(maVoucher2== null || maVoucher2.equals("")) {
				order.setVoucher(null);
			}else {
				Vouchers vouchers = voucherService.getById(maVoucher2);
				if(order.getVoucher() != vouchers || order.getVoucher() == null) {
					/* check xem đã có voucher chưa */
					List<Orders> lst = orderService.getByIdVoucher(order.getAccount().getAccountId());
					if(lst.isEmpty()) {
						int slVouchertru = vouchers.getQuantity() - 1;
						vouchers.setQuantity(slVouchertru);

//								Long amountTongg = (long) (order.getTotal() - ((order.getTotal()*vouchers.getValued())/100));
						Long amountTongg = (long) (order.getTotal() - vouchers.getValued());
						order.setTotal(amountTongg);
						if(slVouchertru <= 0) {
							vouchers.setStatustt(false);
						}
						voucherService.save(vouchers);
						order.setVoucher(vouchers);
					}else {
						int stt=0;
						for (int i = 0; i < lst.size(); i++) {
							String voucherName = lst.get(i).getVoucher().getVoucherName();
							if(voucherName.equals(maVoucher2)) {
								stt += 1;
							}
						}
						if(stt == 0) {
							int slVouchertru = vouchers.getQuantity() - 1;
							vouchers.setQuantity(slVouchertru);

//									Long amountTongg = (long) (order.getTotal() - ((order.getTotal()*vouchers.getValued())/100));
							Long amountTongg = (long) (order.getTotal() - vouchers.getValued());
							order.setTotal(amountTongg);
							if(slVouchertru <= 0) {
								vouchers.setStatustt(false);
							}

							voucherService.save(vouchers);
							order.setVoucher(vouchers);
						}else {
							order.setVoucher(null);
						}
					}
				}else {
					order.setVoucher(null);
				}
				//order.setTotal(amount);
			}
			logger.info("Log set order");
			order.setStatus(1);
			order.setCreateDate(new Date());

			order.setTenNn(visiting.getFullname());
			order.setSdtNn(visiting.getPhone());
//					order.setEmailNn(accountKh.getEmail());
//					order.setDiaChiNn(accountKh.getAddress());
			//order.setTotal(amount1);
			order.setTthaiThanhToan(0);
			orderService.save(order);
			orderDao.updateTongTienKoKhuyenMai();
			orderDao.updateTienSauGiam();
			if(order.getTenNn().equals(null) || order.getSdtNn().equals(null) || order.getAddress().equals(null)) {
				//chỗ này chỉnh sửa đơn ảo
			}
			String paymentUrl = VnpayConfig.abc;
			job.addProperty("code", "00");
			job.addProperty("message", "success");
			job.addProperty("data", paymentUrl);
		}
		else {
			logger.info("Log phương thức thanh toán 2");
			orderDao.updateTongTienKoKhuyenMai();
			orderDao.updateTienSauGiam();
			String queryUrl = query.toString();
			String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.vnp_HashSecret, hashData.toString());
			queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
			String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
			job.addProperty("code", "00");
			job.addProperty("message", "success");
			job.addProperty("data", paymentUrl);
		}


		Gson gson = new Gson();
//		        resp.getWriter().write(gson.toJson(job));

		return gson.toJson(job);
	}

	@PostMapping("/beestore/order/checkoutVistingOrder")
	public String checkOutVisitingOrder(@RequestBody VistingGuest vistingGuest) {
		System.out.println(vistingGuest.getEmail());
		String mail = vistingGuest.getEmail();
		return mail;
	}

	@GetMapping("/thanh-toan-thanh-cong")
	public String thanhcong(Model model, Principal principal) {
		String address = "";
		String tenNN = "";
		logger.info("Log Đặt hàng thành công");
		logger.info("Log lấy order");
		Orders orderCheck = (Orders) session.getAttribute("OrderganNhat");


		String vnp_ResponseCode = req.getParameter("vnp_ResponseCode");

		String vnp_BankCode = req.getParameter("vnp_BankCode");
		String vnp_BankTranNo = req.getParameter("vnp_BankTranNo");
		String vnp_CardType = req.getParameter("vnp_CardType");
		String vnp_OrderInfo = "Thanh toán mua hàng";
		/* Format ngày tháng */
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String strDate = formatter.format(date);
		System.out.println("Date Format with dd MMMM yyyy: " + strDate);
		String vnp_TransactionNo = req.getParameter("vnp_TransactionNo");

		model.addAttribute("vnp_BankCode", vnp_BankCode);
		model.addAttribute("vnp_BankTranNo", vnp_BankTranNo);
		model.addAttribute("vnp_CardType", vnp_CardType);

		model.addAttribute("vnp_OrderInfo", vnp_OrderInfo);
		model.addAttribute("strDate", strDate);
		model.addAttribute("vnp_CardType", vnp_CardType);


		System.out.println("mã: "+vnp_ResponseCode);
		if(vnp_ResponseCode.equals("00")) {
			logger.info("Log lấy order");
			Orders order = (Orders) session.getAttribute("OrderganNhat");
			Accounts accountKh = new Accounts();
			VistingGuest vistingGuest = new VistingGuest();
			if(principal != null) {
				accountKh = (Accounts) session.getAttribute("accountKh");
				logger.info(accountKh.toString());
			}else {
				vistingGuest = (VistingGuest) session.getAttribute("accountKh");
				logger.info(vistingGuest.toString());
			}


			String maVoucher = (String) session.getAttribute("maVoucher");

			System.out.println(address);


			//int amount = (int) session.getAttribute("amount");
			logger.info("Log set voucher");
			if(maVoucher== null || maVoucher.equals("")) {
				order.setVoucher(null);
			}else {
				/* chỉnh lại số lượng voucher */

				Vouchers vouchers = voucherService.getById(maVoucher);
				if(order.getVoucher() != vouchers || order.getVoucher() == null) {
					/* check xem đã có voucher chưa */
					List<Orders> lst = orderService.getByIdVoucher(order.getAccount().getAccountId());
					if(lst.isEmpty()) {
						int slVouchertru = vouchers.getQuantity() - 1;
						vouchers.setQuantity(slVouchertru);

//						Long amountTongg = (long) (order.getTotal() - ((order.getTotal()*vouchers.getValued())/100));
						Long amountTongg = (long) (order.getTotal() - vouchers.getValued());
						order.setTotal(amountTongg);
						voucherService.save(vouchers);
						order.setVoucher(vouchers);
					}else {
						int stt=0;
						for (int i = 0; i < lst.size(); i++) {
							String voucherName = lst.get(i).getVoucher().getVoucherName();
							if(voucherName.equals(maVoucher)) {
								stt += 1;
							}
						}
						if(stt == 0) {
							int slVouchertru = vouchers.getQuantity() - 1;
							vouchers.setQuantity(slVouchertru);
//							Long amountTongg = (long) (order.getTotal() - ((order.getTotal()*vouchers.getValued())/100));
							Long amountTongg = (long) (order.getTotal() - vouchers.getValued());
							order.setTotal(amountTongg);
							voucherService.save(vouchers);
							order.setVoucher(vouchers);
						}else {
							order.setVoucher(null);
							System.out.println("Bạn đã sử dụng voucher này!");
						}
					}
				}else {
					order.setVoucher(null);
				}


				//order.setTotal(amount);
			}
//			order.setEmailNn(accountKh.getEmail());
//			order.setDiaChiNn(accountKh.getAddress());
			if(principal != null) {
 				address = accountKh.getAddress();
 				tenNN = accountKh.getFullname();
			}else {
				address = vistingGuest.getAddress();
				tenNN = vistingGuest.getFullname();
				order.setSdtNn(vistingGuest.getPhone());
			}
			order.setAddress(address);
			order.setStatus(1);
			order.setTthaiThanhToan(1);
			order.setCreateDate(new Date());
			logger.info("Order thanh toán", order.toString());
			logger.info(order.toString());
			orderService.save(order);

			List<OrderDetail> orderDetail = (List<OrderDetail>) session.getAttribute("listOrderDetail");
			if(orderDetail.size() ==0 || orderDetail.equals(null)) {
				return "success";
			}
			//chỉnh sửa lại số lượng thanh toán
			for (int i = 0; i < orderDetail.size(); i++) {
				int productId = orderDetail.get(i).getProduct().getProductId();
				int slgmua = orderDetail.get(i).getQuantity();
				Product product = productService.getById(productId);
				int slgProduct = product.getQuantity();
				int slgMoi = slgProduct - slgmua;
				if(slgMoi < 0) {
					model.addAttribute("message", "Số lượng sản phẩm không đủ để giao tới bạn");
					return "redirect:/thanh-toan-khong-thanh-cong";
				}
				product.setQuantity(slgMoi);
				productService.save(product);
			}

			double vn = order.getTotal();
			float vn2 = (float) vn;
			long vnd = (long) vn2;
			Locale localeVN = new Locale("vi", "VN");
			NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
			String str1 = currencyVN.format(vnd);

			model.addAttribute("order", order);
			model.addAttribute("str1", str1);
			String email = "";
			if(principal != null) {

				email = orderDao.getEmail(order.getOrderId());
				String username = principal.getName();
				Optional<Accounts> user = accountService.findByUsername(username);
				List<CartDTO> cartItems = cartDao.cartDTO(username);
				Long cartTotal = cartDao.cartQuantity(username);
				Long totalPrice = cartDao.totalPrice(username);
				model.addAttribute("cartItems", cartItems);
				model.addAttribute("cartTotal", cartTotal);
				model.addAttribute("totalPrice", totalPrice);
				model.addAttribute("isAccount", 1);

			}else  {
				model.addAttribute("isAccount", 0);
				email = orderDao.getEmailVisiting();
			}

			logger.info("Log update tong tien khong khuyen mai");
			orderDao.updateTongTienKoKhuyenMai();
			logger.info("Log update tien sau giam");
			orderDao.updateTienSauGiam();
			logger.info("Log lấy tiền giảm");
			Double tienGiam = orderDao.getTienSauGiamKhachVangLai((order.getOrderId()));
			model.addAttribute("tienSauGiam", tienGiam);
			logger.info("Log update Orders NUll");
			orderDao.updateOrdersNull();
			logger.info("Log delete Orders");
			orderDao.deleteOrders();
			logger.info("Log gửi mail");
			System.out.println(email);
			sendSimpleEmail(email, order);
			return "success1";
		}else {
			return "redirect:/thanh-toan-khong-thanh-cong";
		}

	}

	@GetMapping("/thanh-toan-khong-thanh-cong")
	public String khongthanhcong(Model model, Principal principal) {
		Orders orderCheck = new Orders();
		if(principal != null) {
			logger.info("Lấy order");
			orderCheck = odao.getGanNhat(account.getAccountId());
			logger.info(orderCheck.toString());
		}else {
			logger.info("Log lấy orders");
			orderCheck = (Orders) session.getAttribute("OrderganNhat");
			logger.info(orderCheck.toString());
		}
		if(!orderService.checkOrders(orderCheck.getOrderId())) {
			logger.info("Delete Order Err");
			logger.info(orderCheck.toString());
			orderDao.updateOrderErr(orderCheck.getOrderId());
			orderDetailDao.updateOrderDetailErr(orderCheck.getOrderId());
			orderDao.deleteOrderErr(orderCheck.getOrderId());
			orderDetailDao.deleteOrderDetailErr(orderCheck.getOrderId());
		}

		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		return "unsuccess";
	}

	@GetMapping("/dat-hang-thanh-cong")
	public String okCOD(Model model, Principal principal) {
		logger.info("Check thanh toan");
		Orders orderCheck = new Orders();
		if(principal != null) {
			logger.info("Lấy order");
			orderCheck = odao.getGanNhat(account.getAccountId());
			logger.info(orderCheck.toString());
		}else {
			logger.info("Log lấy orders");
			orderCheck = (Orders) session.getAttribute("OrderganNhat");
			logger.info(orderCheck.toString());
		}
		if(!orderService.checkOrders(orderCheck.getOrderId())) {
			logger.info("Delete Order Err");
			logger.info(orderCheck.toString());
			orderDao.updateOrderErr(orderCheck.getOrderId());
			orderDetailDao.updateOrderDetailErr(orderCheck.getOrderId());
			orderDao.deleteOrderErr(orderCheck.getOrderId());
			orderDetailDao.deleteOrderDetailErr(orderCheck.getOrderId());
			return "redirect:/thanh-toan-khong-thanh-cong";
		}
		logger.info("Đặt hàng thành công");
		if(principal != null) {
			logger.info("Khách đăng nhập");
			model.addAttribute("isAccount", 1);
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes",sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
//		Orders order = (Orders) session.getAttribute("OrderganNhat");
			account = useAcc.User();
			logger.info("Lấy order");
			Orders order1 = odao.getGanNhat(account.getAccountId());
			logger.info("Update tổng tiền ko khuyến mãi");
			orderDao.updateTongTienKoKhuyenMai();
			logger.info("Update tiền sau giảm");
			orderDao.updateTienSauGiam();


			/* Format ngày tháng */
			Date date = new Date();
			date = order1.getCreateDate();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String strDate = formatter.format(date);
			System.out.println("Date Format with dd MMMM yyyy: " + strDate);
			logger.info("Lấy tiền sau giảm");
			Double tienGiam = orderDao.getTienSauGiam(account.getAccountId());
			logger.info(String.valueOf(tienGiam));
			/* Format tiền */
			double vn = order1.getTotal();
			float vn2 = (float) vn;
			long vnd = (long) vn2;
			Locale localeVN = new Locale("vi", "VN");
			NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
			String str1 = currencyVN.format(vnd);
			System.out.println(str1);


			// load lai sl sp
			List<OrderDetail> orderDetail = (List<OrderDetail>) session.getAttribute("listOrderDetail");
			if(orderDetail.size() ==0 || orderDetail.equals(null)) {
				return "success";
			}
			//chỉnh sửa lại số lượng thanh toán
			logger.info("Chỉnh lại số lượng sản phẩm");
			for (int i = 0; i < orderDetail.size(); i++) {
				int productId = orderDetail.get(i).getProduct().getProductId();
				int slgmua = orderDetail.get(i).getQuantity();
				Product product = productService.getById(productId);
				int slgProduct = product.getQuantity();
				int slgMoi = slgProduct - slgmua;
				if(slgMoi < 0) {
					return "redirect:/thanh-toan-khong-thanh-cong";
				}
				product.setQuantity(slgMoi);
				productService.save(product);
			}
			logger.info("Lấy mail");
			String mail = orderDao.getEmail(order1.getOrderId());
			logger.info("Add attribute");
			model.addAttribute("vnd", str1);
			model.addAttribute("date", strDate);
			model.addAttribute("order", order1);
			model.addAttribute("tienSauGiam", tienGiam);
			logger.info("Gủi mail đến", mail);
			sendSimpleEmail(mail, order1);
			logger.info("xóa cart detail");
			cartDetailDao.deleteCartDetails(account.getAccountId());
			logger.info("update order null");
			orderDao.updateOrdersNull();
			logger.info("delete orders");
			orderDao.deleteOrders();
			return "success1";
		} else {
			logger.info("Khách không đăng nhập");
			model.addAttribute("isAccount", 0);
			logger.info("Log add attribute");
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes", sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			logger.info("Log lấy orders");
			Orders order = (Orders) session.getAttribute("OrderganNhat");
			logger.info(order.toString());
			logger.info("Log lấy order1 ");
			/* Format ngày tháng */
			Date date = new Date();
			date = order.getCreateDate();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			String strDate = formatter.format(date);
			System.out.println("Date Format with dd MMMM yyyy: " + strDate);

			/* Format tiền */
			double vn = order.getTotal();
			float vn2 = (float) vn;
			long vnd = (long) vn2;
			Locale localeVN = new Locale("vi", "VN");
			NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
			String str1 = currencyVN.format(vnd);
			System.out.println(str1);


			// load lai sl sp
			List<OrderDetail> orderDetail = (List<OrderDetail>) session.getAttribute("listOrderDetail");
			if (orderDetail.size() == 0 || orderDetail.equals(null)) {
				return "success";
			}
			//chỉnh sửa lại số lượng thanh toán
			for (int i = 0; i < orderDetail.size(); i++) {
				int productId = orderDetail.get(i).getProduct().getProductId();
				int slgmua = orderDetail.get(i).getQuantity();
				Product product = productService.getById(productId);
				int slgProduct = product.getQuantity();
				int slgMoi = slgProduct - slgmua;
				if (slgMoi < 0) {
					return "redirect:/thanh-toan-khong-thanh-cong";
				}
				product.setQuantity(slgMoi);
				productService.save(product);
			}
			logger.info("Log update tong tien khong khuyen mai");
			orderDao.updateTongTienKoKhuyenMai();
			logger.info("Log update tien sau giam");
			orderDao.updateTienSauGiam();
			logger.info("Log lấy tiền giảm");
			Double tienGiam = orderDao.getTienSauGiamKhachVangLai((order.getOrderId()));
			logger.info("Log update Orders NUll");
			orderDao.updateOrdersNull();
			logger.info("Log delete Orders");
			orderDao.deleteOrders();
			logger.info("Log gửi mail");
			logger.info("Log lấy mail");
			String mail = orderDao.getEmailVisiting();
			model.addAttribute("vnd", str1);
			model.addAttribute("date", strDate);
			model.addAttribute("order", order);
			model.addAttribute("tienSauGiam", tienGiam);
			model.addAttribute("email", mail);
			if (mail == null) {
				System.out.println("Ko co mail");
			} else {
				System.out.println(mail);
			}
			logger.info("Log update Orders NUll");
			orderDao.updateOrdersNull();
			logger.info("Log delete Orders");
			orderDao.deleteOrders();
			logger.info("Log gửi mail");
			try {
				sendSimpleEmail(mail, order);
				return "success1";
			}catch (Exception e) {
				logger.info(e.toString());
				return "redirect:/thanh-toan-khong-thanh-cong";
			}
		}
	}



	@RequestMapping("/order/detail/{id}")
	public String detail(@PathVariable("id") Integer id,Model model, Principal principal){
		List<Product> list = productService.findTop6Img();
		model.addAttribute("items", list);
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		if(principal != null) {
			model.addAttribute("order", ordersService.getById(id));
		}
		model.addAttribute("order", orderDao.getVistingOrder());

		System.out.println("order " + ordersService.getById(id).getClass());
		return "/user/DonHang1";
	}

	@Async
	public void sendSimpleEmail(String email,Orders order) {

		// Create a Simple MailMessage.
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Đặt hàng thành công tại beestore.COM");
		message.setText("Cảm ơn bạn đã mua hàng tại beestore.COM.\r\n"
				+ "Mã hóa đơn của bạn là: "+order.getOrderId()+"\r\n"
				+ "Vui lòng click vào đường link: http://localhost:8080/beestore/account/history/detail/"+order.getOrderId()+ " để xem chi tiết hóa đơn.\r\n"
				+ "Xin chân thành cảm ơn đã sử dụng dịch vụ.");
		System.out.println("gui mail");
		emailSender.send(message);
	}

}