package com.watch.controller;

import com.watch.dao.CartDao;
import com.watch.dao.OrderDetailDao;
import com.watch.dao.OrdersDao;
import com.watch.dto.CartDTO;
import com.watch.entity.*;
import com.watch.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class CartItemController {
	@Autowired
	public VoucherService vser;
	@Autowired
	ProductService productService;
	@Autowired
	OrdersDao ordersDao;
	@Autowired
	OrderDetailDao ordersDetailService;
	 @Autowired UserAcounts useAcc;
		@Autowired
		BrandService brandService;
		
		@Autowired
		StrapService strapSv;
		
		@Autowired
		SizeService sizeSV;

		@Autowired
		CartDao cartDao;
		@GetMapping("/beewatch/cart")
		public List<CartDTO> cart() {
			List<CartDTO> list = cartDao.cartDTO(3);
			return list;
		}
//		@GetMapping("/beewatch/cart")
//		public String cart(Model model) {
//			Accounts account = useAcc.User();
//
//			List<Product> list = productService.findTop6Img();
//			model.addAttribute("items", list);
//			List<Strap_material> straps = strapSv.findAll();
//			model.addAttribute("straps", straps);
//			List<Size> sizes = sizeSV.findAll();
//			model.addAttribute("sizes",sizes);
//			List<Brand> listBrand = brandService.findAll();
//			model.addAttribute("brands", listBrand);
//			List<Vouchers> voucher=vser.findAllByDate();
//			List<Product> top10 = productService.top10a();
//			model.addAttribute("account", account);
//			model.addAttribute("top10", top10);
//			model.addAttribute("cates", voucher);
//			return"/user/GioHang";
//		}

	//giỏ hàng
			@GetMapping("/beewatch/cartItem")
			public String gioHang(Model model) {
				Accounts account = useAcc.User();
				if(useAcc.User()==null) {
					return "redirect:/login";
				}
//				if(account.getAccountId() != null) {
//				Orders or = ordersDao.getGanNhat1(account.getAccountId());
//						if(or !=null) {
//								ordersDetailService.deleteAll(or.getOrderDetails());
//								ordersDao.delete(or);
//								System.out.println("Xóa Ok Order");
//						}
//				}
				List<Product> list = productService.findTop6Img();
				model.addAttribute("items", list);
				List<Strap_material> straps = strapSv.findAll();
				model.addAttribute("straps", straps);
				List<Size> sizes = sizeSV.findAll();
				model.addAttribute("sizes",sizes);
				List<Brand> listBrand = brandService.findAll();
				model.addAttribute("brands", listBrand);
			List<Vouchers> voucher=vser.findAllByDate();
			List<Product> top10 = productService.top10a();
			model.addAttribute("account", account);
		    model.addAttribute("top10", top10);
			model.addAttribute("cates", voucher);
			return"/user/GioHang";
	
			}
}