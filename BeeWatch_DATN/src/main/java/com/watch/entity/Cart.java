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
@Table(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_cart;

    @OneToOne
    @JoinColumn(referencedColumnName = "accountId")
    private Accounts accounts;

    @OneToMany(mappedBy = "cart")
    List<CartDetail> listCartDetail;
    private int status;

}
