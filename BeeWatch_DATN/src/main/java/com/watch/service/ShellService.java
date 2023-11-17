package com.watch.service;

import com.watch.entity.Shell_material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ShellService {

	Shell_material getById(Integer id);

	void delete(Shell_material entity);

	void deleteById(Integer id);

	Optional<Shell_material> findById(Integer id);

	List<Shell_material> findAll();

	Page<Shell_material> findAll(Pageable pageable);

	<S extends Shell_material> S save(S entity);

	List<Shell_material> findTop4Img();

	Shell_material findByName(String name);
	
	List<Shell_material> getAllStatus();
	
	List<Shell_material> findByName1(String string);

	List<Shell_material> findByName(String name,Boolean status);

	List<Shell_material> findByStatus(Boolean status);

}
