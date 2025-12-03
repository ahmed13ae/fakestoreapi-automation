package com.fakestore.automation.services;

import com.fakestore.automation.constants.ApiEndpoints;
import com.fakestore.automation.models.request.ProductRequest;
import io.restassured.response.Response;

public class ProductService extends BaseService {


    public Response getAllProducts() {
        logger.info("Getting all products");
        return get(ApiEndpoints.PRODUCTS);
    }


    public Response getAllProductsWithLimit(int limit) {
        logger.info("Getting products with limit: {}", limit);
        return getWithQueryParam(ApiEndpoints.PRODUCTS, "limit", limit);
    }


    public Response getProductById(int productId) {
        logger.info("Getting product by ID: {}", productId);
        return get(ApiEndpoints.PRODUCT_BY_ID, "id", productId);
    }


    public Response getProductByIdInvalidFormat(String productId) {
        logger.info("Getting product by invalid ID format: {}", productId);
        return get(ApiEndpoints.PRODUCT_BY_ID, "id", productId);
    }


    public Response createProduct(ProductRequest productRequest) {
        logger.info("Creating new product: {}", productRequest.getTitle());
        return post(ApiEndpoints.PRODUCTS, productRequest);
    }


    public Response createProductWithJsonString(String jsonBody) {
        logger.info("Creating product with JSON string");
        return postWithJsonString(ApiEndpoints.PRODUCTS, jsonBody);
    }


    public Response updateProduct(int productId, ProductRequest productRequest) {
        logger.info("Updating product ID: {}", productId);
        return put(ApiEndpoints.PRODUCT_BY_ID, "id", productId, productRequest);
    }


    public Response updateProductWithJsonString(int productId, String jsonBody) {
        logger.info("Updating product ID: {} with JSON string", productId);
        return putWithJsonString(ApiEndpoints.PRODUCT_BY_ID, "id", productId, jsonBody);
    }


    public Response deleteProduct(int productId) {
        logger.info("Deleting product ID: {}", productId);
        return delete(ApiEndpoints.PRODUCT_BY_ID, "id", productId);
    }


    public Response deleteProductInvalidFormat(String productId) {
        logger.info("Deleting product with invalid ID format: {}", productId);
        return delete(ApiEndpoints.PRODUCT_BY_ID, "id", productId);
    }
}