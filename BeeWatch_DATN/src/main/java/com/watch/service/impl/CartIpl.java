package com.watch.service.impl;

import com.watch.dao.CartDao;
import com.watch.entity.Cart;
import com.watch.entity.CartDetail;
import com.watch.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class CartIpl implements CartService {
    Map<Integer, CartDetail> maps = new HashMap<>();
    Map<Integer, Cart> cartList = new HashMap<>();
    @Autowired
    CartDao cartDao;
    @Override
    public void add(Cart cart) {
        CartDetail cartDetailAdd = maps.get(cart.getId_cart());
    }

    @Override
    public void remove(int id) {
        maps.remove(id);
    }

    @Override
    public Cart update(int proD, int qty) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public double getAmount() {
        return 0;
    }

    @Override
    public Cart findByAccId(Long accId) {
        for (Cart cart : cartDao.findAll()) {
            if (cart.getAccounts().getAccountId() == accId) {
                return cart;
            }
        }
        return null;
    }
}
