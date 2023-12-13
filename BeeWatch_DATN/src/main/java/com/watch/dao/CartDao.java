package com.watch.dao;

import com.watch.dto.CartDTO;
import com.watch.entity.Cart;
import com.watch.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao extends JpaRepository<Cart, Integer> {
    @Query("select new com.watch.dto.CartDTO(cd.quantity, p.image, p.productId, p.name, p.price, cd.id_cart_detail) from Cart o join CartDetail cd on o.id_cart = cd.cart.id_cart join Product p on cd.productCartDetail.productId = p.productId join Accounts a on o.accounts.accountId = a.accountId where a.username =:usernameAccount")
    List<CartDTO> cartDTO(String usernameAccount);

    @Query("select o from CartDetail o join Cart c on o.cart.id_cart = c.id_cart where c.accounts.accountId =:idAccount")
    List<CartDetail> cartDetailForPay(Long idAccount);

    @Query("select sum(cd.quantity) from CartDetail cd join Cart c on c.id_cart = cd.cart.id_cart join Accounts a on a.accountId = c.accounts.accountId where a.username =:usernameAccount")
    Long cartQuantity(String usernameAccount);

    @Query("select sum(cd.quantity * p.price) from Cart o join CartDetail cd on o.id_cart = cd.cart.id_cart join Product p on cd.productCartDetail.productId = p.productId join Accounts a on o.accounts.accountId = a.accountId where a.username =:usernameAccount")
    Long totalPrice(String usernameAccount);

    @Query(value = "select * from cart where status = 0 order by id_cart desc", nativeQuery = true)
    Cart getCartRegister();
}
