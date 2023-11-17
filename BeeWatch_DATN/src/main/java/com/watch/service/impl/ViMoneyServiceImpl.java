package com.watch.service.impl;

import com.watch.dao.ViMoneyDao;
import com.watch.entity.ViMoney;
import com.watch.service.ViMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViMoneyServiceImpl implements ViMoneyService{
	@Autowired
	ViMoneyDao viMoneyDao;

	@Override
	public <S extends ViMoney> S save(S entity) {
		return viMoneyDao.save(entity);
	}

	@Override
	public List<ViMoney> findAll() {
		return viMoneyDao.findAll();
	}

	
	
	
}
