package com.fakestore.automation.services;

import com.fakestore.automation.constants.ApiEndpoints;
import com.fakestore.automation.models.request.CartRequest;
import io.restassured.response.Response;

public class CartService extends BaseService {


    public Response getAllCarts() {
        logger.info("Getting all carts");
        return get(ApiEndpoints.CARTS);
    }


    public Response getCartById(int cartId) {
        logger.info("Getting cart by ID: {}", cartId);
        return get(ApiEndpoints.CART_BY_ID, "id", cartId);
    }


    public Response getCartByIdInvalidFormat(String cartId) {
        logger.info("Getting cart with invalid ID format: {}", cartId);
        return get(ApiEndpoints.CART_BY_ID, "id", cartId);
    }


    public Response getCartsByUserId(int userId) {
        logger.info("Getting carts for user ID: {}", userId);
        return get(ApiEndpoints.CARTS_BY_USER, "userId", userId);
    }


    public Response createCart(CartRequest cartRequest) {
        logger.info("Creating new cart for user ID: {}", cartRequest.getUserId());
        return post(ApiEndpoints.CARTS, cartRequest);
    }


    public Response createCartWithJsonString(String jsonBody) {
        logger.info("Creating cart with JSON string");
        return postWithJsonString(ApiEndpoints.CARTS, jsonBody);
    }


    public Response updateCart(int cartId, CartRequest cartRequest) {
        logger.info("Updating cart ID: {}", cartId);
        return put(ApiEndpoints.CART_BY_ID, "id", cartId, cartRequest);
    }


    public Response updateCartWithJsonString(int cartId, String jsonBody) {
        logger.info("Updating cart ID: {} with JSON string", cartId);
        return putWithJsonString(ApiEndpoints.CART_BY_ID, "id", cartId, jsonBody);
    }


    public Response deleteCart(int cartId) {
        logger.info("Deleting cart ID: {}", cartId);
        return delete(ApiEndpoints.CART_BY_ID, "id", cartId);
    }
}
