package com.watch.controller;

import com.watch.dao.*;
import com.watch.entity.*;
import com.watch.service.*;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class AccountController {
	@Autowired
	OrdersService orderService;
	@Autowired
	WishListService wishListService;
	@Autowired
	AccountService accountService;
	@Autowired 
	HttpSession session;
	@Autowired
	OrdersDao orderDao;
	@Autowired
	UserAcounts useAcc;
	@Autowired
	ProductDao productDao;
	@Autowired
	VoucherDao voucherDao;
	@Autowired
	AccountDao accountDao;
	@Autowired
	CartDao cartDao;
	@Autowired
	OrderDetailDao detailDao;
	@Autowired
	SizeService sizeSV;
	@Autowired
	StrapService strapSv;
	@Autowired
	BrandService brandService;

	@Autowired
	ProductService productSV;

	@Autowired
	OrderDetailDao orderDetailDao;
	// Get Cap nhat tai khoan
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/lich_su_ko_login/{orderID}")
	public String orderDetail(@PathVariable(name = "orderID") int orderID, Model model) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("isAccount", 0);
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		Pageable pageable = PageRequest.of(0,1);
		Page<Orders> ord = orderDao.findOrdersByOrderId(orderID, pageable);
		model.addAttribute("number",ord.getNumber());
		model.addAttribute("totalPages",ord.getTotalPages());
		model.addAttribute("totalElements",ord.getTotalElements());
		model.addAttribute("size",ord.getSize());
		model.addAttribute("orders", ord);
		return "/user/account/lichSuMuaHang";
	}

	@GetMapping("/beestore/account/view")
	public String capNhatTaiKhoan(Model model, HttpServletRequest request) {

		if(useAcc.User()==null) {
			return "redirect:/login";
		}
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		Accounts account = useAcc.User();
		Long id = account.getAccountId();
		Accounts account1= accountDao.getById(id);
		Date ngaySinh = account1.getBirthdate();
		String dateNgaySinh="";

		if(ngaySinh!=null) {
			DateFormat  dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
			dateNgaySinh  = dateFormat.format(ngaySinh);
		}
		if(!"".equals(account1.getImage()) && account1.getImage() != null) {
			model.addAttribute("ckimage",true);
		}
		model.addAttribute("dateNgaySinh",dateNgaySinh);
		model.addAttribute("account",account1);
		session.setAttribute("accountSession",account1);
		return "/user/account/capNhatTK";
	}

	// Get lichSuMuahang
	@GetMapping("/beestore/account/history")
	public String lichSuMuahang(Model model, HttpServletRequest request, @RequestParam("p") Optional<Integer> p, Principal principal) {
		if(principal != null) {
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes",sizes);
			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			Accounts account = useAcc.User();
			Long id = account.getAccountId();
			Pageable pageable = PageRequest.of(p.orElse(0), 7);
			Page<Orders> ord = orderService.getOrderByUserId(id, pageable);
			model.addAttribute("number",ord.getNumber());
			model.addAttribute("totalPages",ord.getTotalPages());
			model.addAttribute("totalElements",ord.getTotalElements());
			model.addAttribute("size",ord.getSize());
			model.addAttribute("orders", ord);
			return "/user/account/lichSuMuaHang";
		}else {
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes",sizes);
			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);

			return "/user/account/lichSuMuaHang0Login";
		}

	}

	//huy don hang
	@GetMapping("/beestore/account/history/cancel/{orderId}")
	public String huyDon(Model model, HttpServletRequest request
			,@PathVariable("orderId") Integer orderId
			, @RequestParam("p") Optional<Integer> p
	,	Principal principal) {
		try {
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes",sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			Accounts account = useAcc.User();


//			model.addAttribute("orders", orderService.findByUserId(id));
			Orders order = orderService.getById(orderId);
			if(principal == null) {
				model.addAttribute("isAccount", 0);
				model.addAttribute("brands", listBrand);
				Pageable pageable = PageRequest.of(0,1);
				Page<Orders> ord = orderDao.findOrdersByOrderId(orderId, pageable);
				model.addAttribute("number",ord.getNumber());
				model.addAttribute("totalPages",ord.getTotalPages());
				model.addAttribute("totalElements",ord.getTotalElements());
				model.addAttribute("size",ord.getSize());
				model.addAttribute("orders", ord);
				if (order.getStatus() == 1 || order.getStatus() == 2) {
					order.setStatus(0);
					logger.info(String.valueOf(order.getStatus()));
					if (order.getTthaiThanhToan() == 1) {
						order.setTthaiThanhToan(2);
					}
					List<OrderDetail> odt = detailDao.getOdtByOd(orderId);
//				List<Product> listPro = productDao.getProductByOrders(orderId);

					for (OrderDetail o : odt) {
						Product pro = productDao.getProductByOrderDetail(o.getOrderDetailId());
						pro.setQuantity(pro.getQuantity() + o.getQuantity());
						productSV.save(pro);
					}
					//back lại số lượng sp
					// back lại sl voucher

					if (null != order.getVoucher()) {
						Vouchers voucher = voucherDao.getVoucherWithOrder(order.getVoucher().getVoucherName());
						voucher.setQuantity(voucher.getQuantity() + 1);
						voucherDao.save(voucher);
					}
				}
				if(order.getTienCoc() != 0) {
					order.setTienSauGiam(order.getTienCoc());
					order.setTthaiThanhToan(3);
				}
				orderService.save(order);
				System.out.println("Đã hủy đơn login: "+orderId);
				model.addAttribute("message", "Hủy đơn thành công");
				return "/user/account/lichSuMuaHang";

			} else {
				Long id = account.getAccountId();
				Pageable pageable = PageRequest.of(p.orElse(0), 7);
				Page<Orders> ord = orderService.getOrderByUserId(id, pageable);
				model.addAttribute("number", ord.getNumber());
				model.addAttribute("totalPages", ord.getTotalPages());
				model.addAttribute("totalElements", ord.getTotalElements());
				model.addAttribute("size", ord.getSize());

				model.addAttribute("orders", ord);


				if (order.getStatus() == 1 || order.getStatus() == 2) {
					order.setStatus(0);
					if (order.getTthaiThanhToan() == 1) {
						order.setTthaiThanhToan(2);
					}
					List<OrderDetail> odt = detailDao.getOdtByOd(orderId);
//				List<Product> listPro = productDao.getProductByOrders(orderId);

					for (OrderDetail o : odt) {
						Product pro = productDao.getProductByOrderDetail(o.getOrderDetailId());
						pro.setQuantity(pro.getQuantity() + o.getQuantity());
						productSV.save(pro);
					}
					//back lại số lượng sp
					// back lại sl voucher

					if (null != order.getVoucher()) {
						Vouchers voucher = voucherDao.getVoucherWithOrder(order.getVoucher().getVoucherName());
						voucher.setQuantity(voucher.getQuantity() + 1);
						voucherDao.save(voucher);
					}
				}
				if(order.getTienCoc() != 0) {
					order.setTienSauGiam(order.getTienCoc());
					order.setTthaiThanhToan(3);
				}

				orderService.save(order);
				System.out.println("Đã hủy đơn: " + orderId);
				model.addAttribute("message", "Hủy đơn thành công");
				return "/user/account/lichSuMuaHang";
			}
		} catch (Exception e) {
			// TODO: handle
			e.printStackTrace();
			model.addAttribute("message", "Có lỗi xảy ra trong quá trình hủy!");
			return "/user/account/lichSuMuaHang";
		}
	}

	//hoan don hang
	@GetMapping("/beestore/account/history/refund/{orderId}/{reason}")
	public String hoanDon(Model model,
						  HttpServletRequest request,
						  @PathVariable("orderId") Integer orderId,
						  @RequestParam(name = "p", defaultValue = "0") Optional<Integer> p,
						  @PathVariable("reason") String reason
	) {
		System.out.println("-------------------------------------------------");
		System.out.println("ly do là: "+reason);
		System.out.println("-------------------------------------------------");

		try {
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes",sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			Accounts account = useAcc.User();
			Long id = account.getAccountId();
//			model.addAttribute("orders", orderService.findByUserId(id));

			Orders order = orderService.getById(orderId);
			order.setReason(reason);
			System.out.println("/n");
			System.out.println(reason);
			System.out.println("/n");
			Pageable pageable = PageRequest.of(p.orElse(0), 7);
			Page<Orders> ord = orderService.getOrderByUserId(id, pageable);
			model.addAttribute("number",ord.getNumber());
			model.addAttribute("totalPages",ord.getTotalPages());
			model.addAttribute("totalElements",ord.getTotalElements());
			model.addAttribute("size",ord.getSize());

			model.addAttribute("orders", ord);

			if(order.getStatus() == 4) {
				order.setStatus(5);
				if(order.getTthaiThanhToan() == 4) {
					order.setTthaiThanhToan(2);
				}
				List<OrderDetail> odt = detailDao.getOdtByOd(orderId);
//				List<Product> listPro = productDao.getProductByOrders(orderId);

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

			orderService.save(order);
			System.out.println("Đã hoàn đơn: "+orderId);
			model.addAttribute("message", "Hoàn đơn hàng thành công");
			return "/user/account/lichSuMuaHang";
		} catch (Exception e) {
			// TODO: handle
			e.printStackTrace();
			model.addAttribute("message", "Có lỗi xảy ra trong quá trình hoàn đơn!");
			return "/user/account/lichSuMuaHang";
		}
	}

	//	-----------------------------------------------------------------
	@PostMapping("/beestore/account/history/refundOrder/{orderId}")
	public String hoanDon(
			Model model,
			HttpServletRequest request,
			@PathVariable("orderId") Integer orderId,
			@RequestParam(name = "p", defaultValue = "0") Optional<Integer> p,
			@RequestParam("reason") String reason,
			@RequestParam("anh_san_pham_loi") String anh_san_pham_loi
	) {
		try {
			System.out.println("---------------------------------");
			System.out.println("/n");
			System.out.println("ten anh sp loi:"+anh_san_pham_loi);
			System.out.println("ly do"+reason);
			System.out.println("/n");
			System.out.println("---------------------------------");

			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes",sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			Accounts account = useAcc.User();

			Long id = account.getAccountId();
//			model.addAttribute("orders", orderService.findByUserId(id));

			Orders order = orderService.getById(orderId);
			order.setReason(reason);
			order.setAnhLoiSanPham(anh_san_pham_loi);
			System.out.println("/n");
			System.out.println(reason);
			System.out.println("/n");
			Pageable pageable = PageRequest.of(p.orElse(0), 7);
			Page<Orders> ord = orderService.getOrderByUserId(id, pageable);
			model.addAttribute("number",ord.getNumber());
			model.addAttribute("totalPages",ord.getTotalPages());
			model.addAttribute("totalElements",ord.getTotalElements());
			model.addAttribute("size",ord.getSize());

			model.addAttribute("orders", ord);

			if(order.getStatus() == 4) {
				order.setStatus(5);
				if(order.getTthaiThanhToan() == 4) {
					order.setTthaiThanhToan(2);
				}
				List<OrderDetail> odt = detailDao.getOdtByOd(orderId);
//				List<Product> listPro = productDao.getProductByOrders(orderId);

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

			orderService.save(order);
			System.out.println("Đã hoàn đơn: "+orderId);
			model.addAttribute("message", "Hoàn đơn hàng thành công");
			return "/user/account/lichSuMuaHang";
		} catch (Exception e) {
			// TODO: handle
			e.printStackTrace();
			model.addAttribute("message", "Có lỗi xảy ra trong quá trình hoàn đơn!");
			return "/user/account/lichSuMuaHang";
		}
	}
//	-----------------------------------------------------------------

	@RequestMapping("/beestore/account/history/search")
	public String Search(Model model, @RequestParam("keyword") String kw, @RequestParam("page") Optional<Integer> p) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		try {
			session.setAttribute("keywords", kw);
			Pageable pageable = PageRequest.of(p.orElse(0), 12);
			Page<WishList> page = orderDao.findByKeywords("%" + kw + "%", pageable);
			orderDao.findByKeywords(kw, pageable);
			model.addAttribute("items", page);
			return "/user/account/lichSuMuaHang";
		} catch (Exception e) {
			System.out.println(e);
		}
		return "/user/account/lichSuMuaHang0Login";
	}

	@GetMapping("/beestore/account/favorite")
	public String sanPhamYeuThich(Model model, HttpServletRequest request) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		if(useAcc.User()==null) {
			return "redirect:/login";
		}
		Accounts account = useAcc.User();
		Long id = account.getAccountId();
		model.addAttribute("wishLists", wishListService.findByUserId(id));
		return "/user/account/sanPhamYeuThich";
	}

	// Get doiMatKhau
	@GetMapping("/beestore/account/changePassword")
	public String doiMatKhau(Model model) {
		if(useAcc.User()==null) {
			return "redirect:/login";
		}
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		return "/user/account/doiMatKhau";
	}

	@GetMapping("/beestore/account/like/{id}")
	public String likeOrUnlike(@PathVariable("id") Integer id,Model model) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		Accounts account = useAcc.User();
		if(useAcc.User()==null) {
			return "redirect:/login";
		}

		Long idac = account.getAccountId();
		WishList wl = wishListService.findBy(id, idac);
		wishListService.delete(wl);
		return "redirect:/beestore/account/favorite";
	}

	@PostMapping("/beestore/account/update")
	public String capNhatTaiKhoan2(@ModelAttribute("account") Accounts account
			, @RequestParam("image1") String image1, @RequestParam("image2") String image2
			, @RequestParam("bithdate") String bithdate,Model model) throws ParseException {
		if(useAcc.User()==null) {
			return "redirect:/login";
		}
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);

		String amage = account.getImage();
		if(!"".equals(amage) && amage != null) {
			amage = amage.replaceAll(",", "");
		}
		if( !"".equals(image1)) {
			account.setImage(image1);
		}else {
			account.setImage(image2);
		}
		try {

			Accounts account1 = useAcc.User();
			Long id = account1.getAccountId();
			Accounts account2= accountDao.getById(id);
			account.setCreate_date(account2.getCreate_date());
			account.setStatus(account2.isStatus());
			account.setRoles(account2.getRoles());
			account.setAdminAstrator(account2.getAdminAstrator());

			DateFormat  dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
			Date dateNgaySinh  = dateFormat.parse(bithdate);
			account.setBirthdate(dateNgaySinh);

			accountService.save(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/beestore/account/view";
	}

	@GetMapping("/beestore/account/history/detail/{orderId}")
	public String chiTietDonHang(Model model, @PathVariable("orderId") Integer orderId,HttpServletRequest request) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);

		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		//Accounts account = useAcc.User();

		List<OrderDetail> detail = detailDao.findOrderDetailById(orderId);
		model.addAttribute("details", detail);
		System.out.println(detail);
		return "/user/account/orderDetail";
	}


}