package com.watch.controller;


import com.watch.dao.*;
import com.watch.entity.*;
import com.watch.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
	@Autowired
	ProductService productService;
	@Autowired
	ProductDao pdao;

	@Autowired
	NewsDao ndao;
	@Autowired
	NewsService newsService;

	@Autowired
	BrandDao bdao;
	@Autowired
	BrandService brandService;
	
	@Autowired
	StrapService strapSv;
	
	@Autowired
	SizeService sizeSV;
	
	@Autowired 
    HttpSession session;
	@Autowired
	AccountService accountService;
	@Autowired
	AccountDao accountDao;
	@Autowired
	CartDao cartDao;
	@Autowired
	CartService cartService;

	@Autowired
	NewsDao newsDao;


	//home người admin
	@GetMapping({"/admin","/admin/beestore"})
	public String homeAmin(Model model) {
		return "redirect:/assets/admin/main/homeAdmin.html";
	}
	
	//Home người dùng
	@GetMapping({"/beestore","/beestore/home"})
	public String homeClient(Model model, Principal principal) {
		if (principal != null) {
			// User is logged in
			String username = principal.getName();
			Optional<Accounts> user = accountService.findByUsername(username);
			Long cartTotal = cartDao.cartQuantity(username);
			model.addAttribute("cartTotal", cartTotal);
			model.addAttribute("isAccount", 1);
			List<Product> list = productService.findTop6Img();
			model.addAttribute("items", list);
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes", sizes);

			List<Cart> carts = cartDao.findAll();
			model.addAttribute("carts", carts);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);


			List<Brand> list2 = brandService.findTop4Img();
			model.addAttribute("items2", list2);

			List<News> list3 = newsService.findAll();
			model.addAttribute("items3", list3);

			List<News> list1 = newsDao.findTop3News();
			model.addAttribute("news", list1);

			return "/layout/homeClient";
		} else {
			model.addAttribute("isAccount", 0);

			List<Product> list = productService.findTop6Img();
			model.addAttribute("items", list);
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes", sizes);

			List<Cart> carts = cartDao.findAll();
			model.addAttribute("carts", carts);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);


			List<Brand> list2 = brandService.findTop4Img();
			model.addAttribute("items2", list2);

			List<News> list3 = newsService.findAll();
			model.addAttribute("items3", list3);

			List<News> list1 = newsDao.findTop3News();
			model.addAttribute("news", list1);

			return "/layout/homeClient";
		}
	}
}
