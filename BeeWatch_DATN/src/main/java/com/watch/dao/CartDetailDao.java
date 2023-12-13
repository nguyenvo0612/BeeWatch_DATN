package com.watch.dao;

import com.watch.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartDetailDao extends JpaRepository<CartDetail, Integer> {
    @Transactional
    @Modifying
    @Query(value = "delete cd from cart_detail cd join cart c on c.id_cart = cd.id_cart where c.accounts_account_id = ?1",nativeQuery = true)
    void deleteCartDetails(Long idAcc);
}
