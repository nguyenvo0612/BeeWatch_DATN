package com.watch.restController;

import com.watch.entity.Discount;
import com.watch.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/discount")
public class DiscountRestController {

	@Autowired
	DiscountService discountService;
	
	@GetMapping("/statusTrue")
	public List<Discount> getAll1(){
	  return discountService.findAllStatusTrue();
	}
	
	@GetMapping
	public List<Discount> getAll(){
	  return discountService.findAll();
	}
	@PostMapping
	public Discount create(@RequestBody Discount discount) {
		return discountService.save(discount);
	}
	
//	@GetMapping("{id}")
//	public Product getOne(@PathVariable("id") Integer id) {
//		return discountService.findByid(id);
//	}
	
	@PutMapping("{id}")
	public Discount update(@PathVariable("id") Integer id, @RequestBody Discount discount) {
		return discountService.save(discount);
	}
	
	@PutMapping("/delete/{id}")
	public Discount updateXoa(@PathVariable("id") Integer id, @RequestBody Discount discount) {
		discount.setStatus(false);
		return discountService.save(discount);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		discountService.deleteById(id);
	}
	
	@GetMapping("/timKiem/{status}")
	public List<Discount> getStatus(@PathVariable("status") Boolean status){
		return discountService.findByStatus(status);
	}
	
}