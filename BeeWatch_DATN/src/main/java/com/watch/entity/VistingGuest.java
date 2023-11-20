package com.watch.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "visting_guest")
public class VistingGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_guest;
    @Column(columnDefinition = "NVARCHAR(255) NULL")
    private String fullname;
    @NotBlank
    @Size(max = 50)
    private String email;
    @NotBlank
    @Size(max = 50)
    private String phone;
    @Column(columnDefinition = "NVARCHAR(255) NULL")
    private String address;

    @OneToMany(mappedBy = "vistingGuest")
    List<Orders> listOrders;
}
