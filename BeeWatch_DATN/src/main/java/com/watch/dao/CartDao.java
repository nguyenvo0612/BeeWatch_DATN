package com.watch.dao;

import com.watch.dto.CartDTO;
import com.watch.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao extends JpaRepository<Cart, Integer> {
    @Query("select new com.watch.dto.CartDTO(cd.quantity, p.image, p.productId, p.name, p.price) from Cart o join CartDetail cd on o.id_cart = cd.cart.id_cart join Product p on cd.productCartDetail.productId = p.productId join Accounts a on o.accounts.accountId = a.accountId where a.username =:usernameAccount")
    List<CartDTO> cartDTO(String usernameAccount);




}
