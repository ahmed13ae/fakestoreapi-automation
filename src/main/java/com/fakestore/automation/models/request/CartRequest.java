package com.fakestore.automation.models.request;

import java.util.List;

public class CartRequest {
    private Integer userId;
    private String date;
    private List<Product> products;


    public static class Product {
        private Integer productId;
        private Integer quantity;

        public Product() {
        }

        public Product(Integer productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }


    public CartRequest() {
    }


    public CartRequest(Integer userId, String date, List<Product> products) {
        this.userId = userId;
        this.date = date;
        this.products = products;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
