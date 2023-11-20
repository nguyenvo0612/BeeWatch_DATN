package com.watch.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class CartDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_cart_detail;

    @ManyToOne
    @JoinColumn(name = "id_cart")
    private Cart cart;

    @OneToMany(mappedBy = "cartDetail")
    List<Product> listProduct;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "status")
    private int status;

}
