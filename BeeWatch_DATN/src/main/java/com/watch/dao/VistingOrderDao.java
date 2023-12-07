package com.watch.dao;

import com.watch.entity.VistingGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VistingOrderDao extends JpaRepository<VistingGuest, Integer> {
    @Query(value = "select top(1) * from visting_guest where address is null and email is null and fullname is null and phone is null and id_guest not in(select visting_guest_id from visting_guest vg join orders o on vg.id_guest = o.visting_guest_id where vg.address is null and email is null and fullname is null and phone is null )", nativeQuery = true)
    VistingGuest getVistingGuest();

    @Query(value = "select * from visting_guest where id_guest in (select top 1 visting_guest_id from orders order by create_date desc)", nativeQuery = true)
    VistingGuest getVistingGuestOrders();
    }