package com.watch.controller;

import com.watch.entity.*;
import com.watch.service.BrandService;
import com.watch.service.ProductService;
import com.watch.service.SizeService;
import com.watch.service.StrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {
	@Autowired 
	HttpSession session;
	@Autowired
    UserAcounts useAcc;
	@Autowired
	SizeService sizeSV;
	@Autowired
	StrapService strapSv;
	@Autowired
	BrandService brandService;
	
	@Autowired
	ProductService productSV;
	//Login
	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		Accounts acount = useAcc.User();
		if(acount!=null) {
			model.addAttribute("message", "LOCK");
			request.getSession().setAttribute("message", "LOCK");
			session.removeAttribute("User");
			session.invalidate();
		}else {
			model.addAttribute("message", "");
			request.getSession().setAttribute("message", "");
		}
		
		return"/user/login/dangNhap";
	}
	//đăng ký
	@GetMapping("/registers")
	public String dangky(Model model) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);
		
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		return"/user/login/dangKyTK";
	}
	
	@GetMapping("/forgotPassword")
	public String quenMatKhau(Model model) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);
		
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		return"/user/quenMK";
	}
	
	@GetMapping("/logout")
	public String dangXuat() {
		session.removeAttribute("User");
		session.invalidate();
		return"redirect:/login";
	}
		
	@GetMapping("/itwatch/chinhSachBaoHanh")
	public String chinhSachBaoHanh(Model model) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);
		
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		return "/user/csbh/csbh";
	}
	
	@GetMapping("/itwatch/chinhSachDoiTra")
	public String chinhSachDoiTra(Model model) {
		List<Strap_material> straps = strapSv.findAll();
		model.addAttribute("straps", straps);
		List<Size> sizes = sizeSV.findAll();
		model.addAttribute("sizes",sizes);
		
		List<Brand> listBrand = brandService.findAll();
		model.addAttribute("brands", listBrand);
		return "/user/csbh/csdt";
	}
	
}
