package com.watch.dao;

import com.watch.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDao extends JpaRepository<News, Integer>{
    @Query(value = "select top(3)* from news order by news_id desc", nativeQuery = true)
    List<News> findTop3News();
}
