package com.watch.service;

import com.watch.entity.ViMoney;

import java.util.List;

public interface ViMoneyService {

	List<ViMoney> findAll();

	<S extends ViMoney> S save(S entity);

}
