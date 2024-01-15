package com.watch.controller;

import com.watch.dao.CartDao;
import com.watch.entity.*;
import com.watch.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

	@Autowired
	CartDao cartDao;
	@Autowired
	AccountService accountService;
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

	@GetMapping("/beestore/chinhSachBaoHanh")
	public String chinhSachBaoHanh(Model model, Principal principal) {
		if (principal != null) {
			// User is logged in
			String username = principal.getName();
			Optional<Accounts> user = accountService.findByUsername(username);
			Long cartTotal = cartDao.cartQuantity(username);
			model.addAttribute("cartTotal",cartTotal);
			model.addAttribute("isAccount", 1);
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes", sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			return "/user/csbh/csbh";
		}else {
			model.addAttribute("isAccount", 0);
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes", sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			return "/user/csbh/csbh";
		}
	}

	@GetMapping("/beestore/chinhSachDoiTra")
	public String chinhSachDoiTra(Model model,Principal principal) {
		if (principal != null) {
			// User is logged in
			String username = principal.getName();
			Optional<Accounts> user = accountService.findByUsername(username);
			Long cartTotal = cartDao.cartQuantity(username);
			model.addAttribute("cartTotal",cartTotal);
			model.addAttribute("isAccount", 1);
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes", sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			return "/user/csbh/csdt";
		}else {
			model.addAttribute("isAccount", 0);
			List<Strap_material> straps = strapSv.findAll();
			model.addAttribute("straps", straps);
			List<Size> sizes = sizeSV.findAll();
			model.addAttribute("sizes", sizes);

			List<Brand> listBrand = brandService.findAll();
			model.addAttribute("brands", listBrand);
			return "/user/csbh/csdt";
		}
	}
}
