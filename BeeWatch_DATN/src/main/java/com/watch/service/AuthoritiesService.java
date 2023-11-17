package com.watch.service;

import com.watch.entity.Accounts;
import com.watch.entity.Authorities;

import java.util.List;
import java.util.Optional;

public interface AuthoritiesService {

	Authorities getById(Long id);

	void delete(Authorities entity);

	void deleteById(Long id);

	Optional<Authorities> findById(Long id);

	List<Authorities> findAll();

	 Authorities save(Authorities entity);
	
	public List<Authorities> findAuthoritiesOfAdminstrators();

	Accounts findAcountAuthority(Long idAcc);

	int findAcountAuthorityCheck(Long idAcc);

}
