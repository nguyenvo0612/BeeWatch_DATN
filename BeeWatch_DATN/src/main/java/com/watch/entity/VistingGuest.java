package com.watch.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "visting_guest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VistingGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_guest;
    @Column(columnDefinition = "NVARCHAR(255) NULL")
    private String fullname;
    @Size(max = 50)
    private String email;
    @Size(max = 50)
    private String phone;
    @Column(columnDefinition = "NVARCHAR(255) NULL")
    private String address;
    @OneToMany(mappedBy = "vistingGuest")
    @JsonManagedReference
    List<Orders> listOrders;

    @Override
    public String toString() {
        return "VistingGuest{" +
                "id_guest=" + id_guest +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", listOrders=" + listOrders +
                '}';
    }
}
