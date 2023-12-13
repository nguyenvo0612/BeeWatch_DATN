package com.watch.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "cart_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_cart_detail;

    @ManyToOne
    @JoinColumn(name = "id_cart")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product productCartDetail;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "status")
    private int status;

}
