package com.watch.restController;

import com.watch.entity.Feedback;
import com.watch.service.AccountService;
import com.watch.service.FeedbackService;
import com.watch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/feedback")
public class FeedbackRestController {
	
	@Autowired
	FeedbackService feedbackService;
	@Autowired
	AccountService accountService;
	@Autowired
	ProductService productService;
	
	@GetMapping()
	public List<Feedback> getFeedback(){
		return feedbackService.findAll();
	}
	
	
	
	@GetMapping("{id}")
	public List<Feedback> getFeedback(@PathVariable("id") Integer id){
		return feedbackService.findByProductID(id);
	}


	@PostMapping("{id}")
	public Feedback create(@RequestBody Feedback feedback,@PathVariable("id") Integer id) {		
		return feedbackService.addFeedback(feedback, id);
	}

	@PutMapping("{id}")
	public Feedback update() {
		return null;
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		feedbackService.delete(id); 
	}
	
	
}