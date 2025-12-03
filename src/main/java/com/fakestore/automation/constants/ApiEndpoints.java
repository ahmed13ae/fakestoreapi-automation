package com.fakestore.automation.constants;

public class ApiEndpoints {


    public static final String BASE_URI = "https://fakestoreapi.com";


    public static final String PRODUCTS = "/products";
    public static final String PRODUCT_BY_ID = "/products/{id}";


    public static final String CARTS = "/carts";
    public static final String CART_BY_ID = "/carts/{id}";
    public static final String CARTS_BY_USER = "/carts/user/{userId}";


    public static final String LOGIN = "/auth/login";

    private ApiEndpoints() {

    }
}