package com.watch.controller;


import com.watch.dao.BrandDao;
import com.watch.dao.NewsDao;
import com.watch.dao.ProductDao;
import com.watch.entity.*;
import com.watch.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

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
	
	//home người admin
	@GetMapping({"/","/admin","/admin/beewatch"})
	public String homeAmin(Model model) {
		return "redirect:/assets/admin/main/homeAdmin.html";
	}
	
	//Home người dùng
	@GetMapping({"/beewatch","/beewatch/home"})
	public String homeClient(Model model) {
		List<Product> list = productService.findTop6Img();
		model.addAttribute("items", list);
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);
		
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		
		
		List<Brand> list2 = brandService.findTop4Img();
		model.addAttribute("items2", list2);

		List<News> list3 = newsService.findAll();
		model.addAttribute("items3", list3);

		List<News> list1= newsService.findAll();
		model.addAttribute("news", list1);

		return "/layout/homeClient";	
	}
}
