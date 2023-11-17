package com.watch.restController;

import com.watch.entity.Water_resistant;
import com.watch.service.WaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/water")
public class WaterRestController {
	@Autowired
	WaterService waterService;

	@GetMapping
	public List<Water_resistant> getAll(){
	  return waterService.findAll();
	}
	
	@GetMapping("/getAllStatus")
	public List<Water_resistant> getAllStatus(){
	  return waterService.findByStatus(true);
	}
	
	@GetMapping("{name}")
	public Water_resistant getName(@PathVariable("name") String name) {
		return waterService.findByName(name);
	}
	
	@PostMapping
	public Water_resistant on(@RequestBody Water_resistant Water_resistant) {
		return waterService.save(Water_resistant);
	}
	
	@PutMapping("{id}")
	public Water_resistant update(@PathVariable("id") Integer id, @RequestBody Water_resistant Water_resistant) {
		return waterService.save(Water_resistant);
	}
	
	@PutMapping("/delete/{id}")
	public Water_resistant updatetrangthai(@PathVariable("id") Integer id, @RequestBody Water_resistant Water_resistant) {
		Water_resistant.setStatus(false);
		return waterService.save(Water_resistant);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		waterService.deleteById(id);
	}
	
	@GetMapping("/timKiem/{name}/{status}")
	public List<Water_resistant> timKiem(@PathVariable("name") String name,@PathVariable("status") String status){
		//System.out.println("tên= "+name + " status= "+ status);
		if (status.equals("null")) {
			System.out.println("tên= "+name);
			return waterService.findByName1("%"+name+"%");
		} else {
			boolean in  = Boolean.parseBoolean(status);
			System.out.println("tên= "+name + " status="+ in);
			return waterService.findByName("%"+name+"%" , in);
		}
	}
	
	@GetMapping("/timKiem/{status}")
	public List<Water_resistant> getStatus(@PathVariable("status") Boolean status){
		return waterService.findByStatus(status);
	}
	
	
}