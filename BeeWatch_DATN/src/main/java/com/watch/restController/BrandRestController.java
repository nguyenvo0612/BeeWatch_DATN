package com.watch.restController;

import com.watch.entity.Brand;
import com.watch.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/brand")
public class BrandRestController {
	@Autowired
	BrandService brandService;

	@GetMapping
	public List<Brand> getAll(){
	  return brandService.findAll();
	}
	
	@GetMapping("/getAllStatus")
	public List<Brand> getAllStatus(){
	  return brandService.getAllStatus();
	}
	@GetMapping("{name}")
	public Brand getName(@PathVariable("name") String name) {
		return brandService.findByName(name);
	}
	
	@PostMapping
	public Brand on(@RequestBody Brand brand) {
		return brandService.save(brand);
	}
	
	@PutMapping("{id}")
	public Brand update(@PathVariable("id") Integer id, @RequestBody Brand brand) {
		return brandService.save(brand);
	}
	
	@PutMapping("/delete/{id}")
	public Brand updateThai(@PathVariable("id") Integer id, @RequestBody Brand brand) {
		brand.setStatus(false);
		return brandService.save(brand);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		brandService.deleteById(id);
	}
	
	@GetMapping("/timKiem/{name}/{status}")
	public List<Brand> timKiem(@PathVariable("name") String name,@PathVariable("status") String status){
		//System.out.println("tên= "+name + " status= "+ status);
		if (status.equals("null")) {
			System.out.println("tên= "+name);
			return brandService.findByName1("%"+name+"%");
		} else {
			boolean in  = Boolean.parseBoolean(status);
			System.out.println("tên= "+name + " status="+ in);
			return brandService.findByName("%"+name+"%" , in);
		}
	}
	
	@GetMapping("/timKiem/{status}")
	public List<Brand> getStatus(@PathVariable("status") Boolean status){
		return brandService.findByStatus(status);
	}
	
	
}