package com.watch.controller;

import com.watch.dao.CartDao;
import com.watch.dao.CartDetailDao;
import com.watch.dao.OrderDetailDao;
import com.watch.dao.OrdersDao;
import com.watch.dto.CartDTO;
import com.watch.entity.*;
import com.watch.service.*;
import com.watch.service.impl.CartIpl;
import com.watch.service.impl.CartItemImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


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

	@Autowired
	CartDetailDao cartDetailDao;

	@Autowired
	CartItemService cartItemService;
	@Autowired
	private AccountService accountService;

//	@GetMapping("/beewatch/cart")
//	public List<CartDTO> cart() {
//		List<CartDTO> list = cartDao.cartDTO("admin");
//		return list;
//	}


	//giỏ hàng
	@GetMapping("/beestore/cartItem")
	public String gioHang(Model model, Principal principal) {



//				if(account.getAccountId() != null) {
//				Orders or = ordersDao.getGanNhat1(account.getAccountId());
//						if(or !=null) {
//								ordersDetailService.deleteAll(or.getOrderDetails());
//								ordersDao.delete(or);
//								System.out.println("Xóa Ok Order");
//						}
//				}
		if(principal != null) {
			Accounts account = useAcc.User();
			System.out.println(account);
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
				model.addAttribute("account", account);
				model.addAttribute("isAccount", 1);
				return "/user/GioHangLogin";
			}
		}else {

			List<Product> list = productService.findTop6Img();
			model.addAttribute("isAccount", 0);
			model.addAttribute("items", list);
			List<Strap_material> straps = strapSv.findAll();
			Accounts account = useAcc.User();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes", sizes);
			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			List<Vouchers> voucher = vser.findAllByDate();
			List<Product> top10 = productService.top10a();
			model.addAttribute("top10", top10);
			model.addAttribute("cates", voucher);
			return "/user/GioHang";
		}
	}

	@Autowired
	private CartItemImpl cartItemImpl;
	@Autowired
	private CartIpl cartIpl;

	@GetMapping("/cart-add/{id}")
	public String addCart(@PathVariable("id") Integer id,
						  Model model, Principal principal) {
		Optional<Product> product = productService.findById(id);
		if (principal != null) {
			// User is logged in
			String username = principal.getName();
			Accounts user = accountService.findUserByUsername(username);
			Cart cartForAdd = cartIpl.findByAccId(user.getAccountId());
			List<CartDTO> cartItems = cartDao.cartDTO(username);
			Long cartTotal = cartDao.cartQuantity(username);
			Long totalPrice = cartDao.totalPrice(username);
			model.addAttribute("cartItems", cartItems);
			model.addAttribute("cartTotal", cartTotal);
			model.addAttribute("totalPrice", totalPrice);
			List<CartDetail> listCartItem = cartDetailDao.findAll();
			if (listCartItem.size() != 0) {

				if (cartItemImpl.getCartDetailProId(product.get().getProductId()) != null) {
					CartDetail cartDetail = cartItemImpl.getCartDetailProId(product.get().getProductId());
					cartDetail.setQuantity(cartDetail.getQuantity() + 1);
					cartDetail.setId_cart_detail(cartDetail.getId_cart_detail());
					Logger logger = LoggerFactory.getLogger(this.getClass());
					logger.info(cartDetail.toString());
					cartDetailDao.save(cartDetail);
					return "redirect:/beestore/cartItem";
				} else {
					CartDetail cartDetail = new CartDetail();
					cartDetail.setProductCartDetail(product.get());
					cartDetail.setQuantity(1);
					cartDetail.setStatus(1);
					cartDetail.setCart(cartForAdd);
					System.out.println(cartDetail);
					cartDetailDao.save(cartDetail);
					return "redirect:/beestore/cartItem";
				}
			} else {
				CartDetail cartDetail = new CartDetail();
				cartDetail.setProductCartDetail(product.get());
				cartDetail.setQuantity(1);
				cartDetail.setStatus(1);
				cartDetail.setCart(cartForAdd);
				System.out.println(cartDetail);
				cartDetailDao.save(cartDetail);
				return "redirect:/beestore/cartItem";
			}
		}
		return "redirect:/beestore/cartItem";
	}
	@GetMapping("/remove-item/{id}")
	public String removeCart(@PathVariable("id") Integer id,
							 Model model, Principal principal){
		Optional<Product> product = productService.findById(id);
		if (principal != null) {
			// User is logged in
			String username = principal.getName();
			Accounts user = accountService.findUserByUsername(username);
			Cart cartForAdd = cartIpl.findByAccId(user.getAccountId());
			List<CartDTO> cartItems = cartDao.cartDTO(username);
			Long cartTotal = cartDao.cartQuantity(username);
			Long totalPrice = cartDao.totalPrice(username);
			model.addAttribute("cartItems", cartItems);
			model.addAttribute("cartTotal", cartTotal);
			model.addAttribute("totalPrice", totalPrice);
			List<CartDetail> listCartItem = cartDetailDao.findAll();
			if (listCartItem.size() != 0) {

				if (cartItemImpl.getCartDetailProId(product.get().getProductId()) != null) {
					CartDetail cartDetail = cartItemImpl.getCartDetailProId(product.get().getProductId());
					cartDetail.setQuantity(cartDetail.getQuantity() - 1);
					cartDetail.setId_cart_detail(cartDetail.getId_cart_detail());
					Logger logger = LoggerFactory.getLogger(this.getClass());
					logger.info(cartDetail.toString());
					cartDetailDao.delete(cartDetail);
					return "redirect:/beestore/cartItem";
				} else {
					CartDetail cartDetail = new CartDetail();
					cartDetail.setProductCartDetail(product.get());
					cartDetail.setQuantity(1);
					cartDetail.setStatus(1);
					cartDetail.setCart(cartForAdd);
					System.out.println(cartDetail);
					cartDetailDao.delete(cartDetail);
					return "redirect:/beestore/cartItem";
				}
			} else {
				CartDetail cartDetail = new CartDetail();
				cartDetail.setProductCartDetail(product.get());
				cartDetail.setQuantity(1);
				cartDetail.setStatus(1);
				cartDetail.setCart(cartForAdd);
				System.out.println(cartDetail);
				cartDetailDao.delete(cartDetail);
				return "redirect:/beestore/cartItem";
			}
		}
		return "redirect:/beestore/cartItem";
	}
	@PostMapping("updateCartItem")
	public String updateCartItem(@RequestParam int cartItemId, @RequestParam(name = "quantity", required = false) int quantity) {
		cartItemService.update(cartItemId,quantity);
		return "redirect:/beestore/cartItem";
	}
}