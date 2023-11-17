package com.watch.service.impl;

import com.watch.dao.AccountDao;
import com.watch.dao.AuthoritiesDao;
import com.watch.entity.Accounts;
import com.watch.entity.Authorities;
import com.watch.service.AuthoritiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthoritiesServiceImpl implements AuthoritiesService{
	@Autowired
	AuthoritiesDao authoritiesDao;
	
	@Autowired
	AccountDao acdao;

	@Override
	public Authorities save(Authorities entity) {
		return authoritiesDao.save(entity);
	}

	@Override
	public List<Authorities> findAll() {
		return authoritiesDao.findAll();
	}

	@Override
	public Optional<Authorities> findById(Long id) {
		return authoritiesDao.findById(id);
	}

	@Override
	public void deleteById(Long id) {
		authoritiesDao.deleteById(id);
	}

	@Override
	public void delete(Authorities entity) {
		authoritiesDao.delete(entity);
	}

	@Override
	public Authorities getById(Long id) {
		return authoritiesDao.getById(id);
	}
	
	
	public List<Authorities> findAuthoritiesOfAdminstrators() {
		// TODO Auto-generated method stub
		List<Accounts> Account = acdao.getAdministrators();
		return authoritiesDao.authoritiesOf(Account);
	}

	@Override
	public Accounts findAcountAuthority(Long idAcc) {
		// TODO Auto-generated method stub
		return authoritiesDao.findAcountAuthority(idAcc);
	}

	@Override
	public int findAcountAuthorityCheck(Long idAcc) {
		// TODO Auto-generated method stub
		return authoritiesDao.findAcountAuthorityCheck(idAcc);
	}
	
	
}
