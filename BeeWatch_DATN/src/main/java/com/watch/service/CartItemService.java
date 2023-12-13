package com.watch.service;

import com.watch.entity.CartDetail;
import org.springframework.stereotype.Service;

@Service
public interface CartItemService {
    public void add(CartDetail cartDetail);
    public void remove(int id);
    public void update(int id,int qty);
    public void clear();
    public int getCount();
    public double getAmount();
}
