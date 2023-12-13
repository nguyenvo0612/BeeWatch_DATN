package com.watch.service;


import com.watch.entity.Cart;

public interface CartService {
    public void add(Cart cart);
    public void remove(int id);

    public Cart update(int proD,int qty);
    public void clear();
    public int getCount();
    public double getAmount();
    Cart findByAccId(Long accId);
}
