package com.watch.dao;

import com.watch.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesDao extends JpaRepository<Roles, Integer>{
	Roles findByName(String name);
}
