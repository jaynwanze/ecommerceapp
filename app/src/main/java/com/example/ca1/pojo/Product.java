package com.example.ca1.pojo;

import androidx.annotation.NonNull;

public class Product {

    private String name;
    private int quantity;
    private double price;
    private String userId;

    public Product() {
    }

    public Product(String name, int quantity, double price, String userId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", userId='" + userId + '\'' +
                '}';
    }
}
