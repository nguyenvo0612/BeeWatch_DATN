package com.watch.dao;

import com.watch.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDao extends JpaRepository<News, Integer>{
	
	
	
}
