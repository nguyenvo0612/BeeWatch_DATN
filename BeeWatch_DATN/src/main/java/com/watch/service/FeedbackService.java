package com.watch.service;

import com.watch.entity.Feedback;

import java.util.List;

public interface FeedbackService {

	List<Feedback> findByProductID(Integer id);


	List<Feedback> findAll();


	Feedback create(Feedback feedback);


	Feedback addFeedback(Feedback feedback, Integer id);


	void delete(Integer id);

}
