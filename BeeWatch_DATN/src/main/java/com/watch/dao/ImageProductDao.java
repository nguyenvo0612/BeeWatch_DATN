package com.watch.dao;

import com.watch.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductDao extends JpaRepository<ImageProduct, Integer>{

	@Query("SELECT o FROM ImageProduct o WHERE o.product.id=?1")
	List<ImageProduct> findByIdproduct(Integer id);

}