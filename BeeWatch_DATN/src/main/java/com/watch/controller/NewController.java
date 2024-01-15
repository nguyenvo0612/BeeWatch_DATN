package com.watch.controller;

import com.watch.entity.News;
import com.watch.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller

public class NewController {
	@Autowired
	private NewsService vser;
	@RequestMapping("/beestore/blog/{newsId}")
	//lấy thông tin từ trình duyệt gắn vào Integer id (PathValueable)
	public String detail(Model model,@PathVariable("newsId") Integer id) {
		News item=vser.getById(id);
		model.addAttribute("details", item);
		//Duyệt các bài viết mới nhất
		List<News> list=vser.findAll();
		model.addAttribute("news", list);
		return "/user/news/TinTuc";
	}
}