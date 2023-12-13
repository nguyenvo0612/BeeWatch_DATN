package com.watch.dto;

import java.io.Serializable;

public class CartDTO implements Serializable{
    private int quantity;
    private String image;
    private int product_id;
    private String nameProduct;
    private Double price;

    private int cartDetailId;

    public CartDTO() {
    }

    public CartDTO(int quantity, String image, int product_id, String nameProduct, Double price, int cartDetailId) {
        this.quantity = quantity;
        this.image = image;
        this.product_id = product_id;
        this.nameProduct = nameProduct;
        this.price = price;
        this.cartDetailId = cartDetailId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(int cartDetailId) {
        this.cartDetailId = cartDetailId;
    }
}
