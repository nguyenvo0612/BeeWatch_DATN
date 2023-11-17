package com.watch.dao;

import com.watch.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeDao extends JpaRepository<Size, Integer>{
	@Query(nativeQuery = true, value = "SELECT TOP 4 * FROM Size o ORDER BY o.id ASC")
	List<Size> findTop4Img();
	
	@Query("SELECT o FROM Size o WHERE o.name LIKE ?1")
	Size findByName(String name);
	

	@Query("SELECT o FROM Size o WHERE o.status = true")
	List<Size> getAllStatus();

		@Query("SELECT o FROM Size o WHERE o.name LIKE ?1 and status = ?2")
	List<Size> findByName(String name,Boolean status);
	
	@Query("SELECT o FROM Size o WHERE o.status = ?1")
	List<Size> findByStatus(Boolean status);

	@Query("SELECT o FROM Size o WHERE o.name LIKE ?1")
	List<Size> findByName1(String name);
}
