package com.watch.restController;

import com.watch.entity.Category;
import com.watch.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {

	@Autowired
	CategoryService categoryService;
	
	@GetMapping()
	public List<Category> getAll(){
		return categoryService.findAll();
	}
	
	@GetMapping("/getAllStatus")
	public List<Category> getAllStatus(){
		return categoryService.getAllStatus();
	}
	
	@PostMapping 
	public Category create(@RequestBody Category category) {
		return categoryService.save(category);
	}
	
	@PutMapping("{id}")
	public Category update(@PathVariable("id") Integer id, @RequestBody Category category) {
		if(category.getDiscount() == null) {
			category.setDiscount(null);
		}else {
			if(category.getDiscount().getDiscountId() ==0) {
				category.setDiscount(null);
			}
		}
		return categoryService.save(category);
	}
	
	@PutMapping("/delete/{id}")
	public Category deleteThai(@PathVariable("id") Integer id, @RequestBody Category category) {
		category.setStatus(false);
		return categoryService.save(category);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		categoryService.deleteById(id);
	}
	
	
	@GetMapping("/timKiem/{name}/{status}")
	public List<Category> timKiem(@PathVariable("name") String name,@PathVariable("status") String status){
		//System.out.println("tên= "+name + " status= "+ status);
		if (status.equals("null")) {
			System.out.println("tên= "+name);
			return categoryService.findByName1("%"+name+"%");
		} else {
			boolean in  = Boolean.parseBoolean(status);
			System.out.println("tên= "+name + " status="+ in);
			return categoryService.findByName("%"+name+"%" , in);
		}
	}
	
	@GetMapping("/timKiem/{status}")
	public List<Category> getStatus(@PathVariable("status") Boolean status){
		return categoryService.findByStatus(status);
	}
	
	@GetMapping("{name}")
	public Category getName(@PathVariable("name") String name) {
		return categoryService.findByName2(name);
	}
	
}