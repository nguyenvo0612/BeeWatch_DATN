package com.watch.service.impl;


import com.watch.dao.CartDao;
import com.watch.dao.CartDetailDao;
import com.watch.entity.CartDetail;
import com.watch.service.CartItemService;
import com.watch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SessionScope
@Service
public class CartItemImpl implements CartItemService {
    Map<Integer, CartDetail> maps = new HashMap<>();
    @Autowired
    CartDao cartDao;
    @Autowired
    CartDetailDao cartDetailDao;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    ProductService productService;


    @Override
    public void add(CartDetail cartDetail) {
        CartDetail cartDetailAdd = maps.get(cartDetail.getId_cart_detail());
//        cartDao.save(cartDetail);

    }

    @Override
    public void remove(int id) {
        maps.remove(id);
    }

    @Override
    public void update(int id, int qty) {
        Optional<CartDetail> cartItem = cartDetailDao.findById(id);
        // Thực hiện logic cập nhật số lượng
        cartItem.get().setQuantity(qty);

        // Lưu cập nhật vào cơ sở dữ liệu
        cartDetailDao.save(cartItem.get());
    }

    @Override
    public void clear() {
        maps.clear();
    }



    @Override
    public int getCount() {
        return maps.size();
    }

    @Override
    public double getAmount() {
        return maps.values().stream()
                .mapToDouble(item -> (item.getQuantity() * (item.getProductCartDetail().getPrice())))
                .sum();
    }



    public CartDetail getCartDetailProId(int idPro) {
        for (CartDetail cartDetail : cartDetailDao.findAll()) {
            if (cartDetail.getProductCartDetail().getProductId() == idPro) {
                return cartDetail;
            }
        }
        return null;
    }
}
