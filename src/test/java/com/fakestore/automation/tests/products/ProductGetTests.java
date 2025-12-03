package com.fakestore.automation.tests.products;

import com.fakestore.automation.dataproviders.ProductDataProvider;
import com.fakestore.automation.enums.StatusCode;
import com.fakestore.automation.listeners.ExtentReportListener;
import com.fakestore.automation.models.response.ProductResponse;
import com.fakestore.automation.tests.BaseTest;
import com.fakestore.automation.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

@Listeners(ExtentReportListener.class)
public class ProductGetTests extends BaseTest {


    @Test(priority = 1, description = "Verify getting all products successfully")
    public void testGetAllProducts() {
        logTestInfo("TC-PROD-001", "Verify getting all products successfully");

        // Send GET request to get all products
        Response response = productService.getAllProducts();

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Validate response is an array
        ResponseValidator.validateResponseIsArray(response);

        // Deserialize response to list of products
        List<ProductResponse> products = response.jsonPath().getList("$", ProductResponse.class);

        // Validate products list is not empty
        Assert.assertFalse(products.isEmpty(), "Products list should not be empty!");

        logger.info("Total products returned: {}", products.size());

        // Validate each product has required fields
        for (ProductResponse product : products) {
            Assert.assertNotNull(product.getId(), "Product ID should not be null!");
            Assert.assertNotNull(product.getTitle(), "Product title should not be null!");
            Assert.assertNotNull(product.getPrice(), "Product price should not be null!");
            Assert.assertNotNull(product.getDescription(), "Product description should not be null!");
            Assert.assertNotNull(product.getCategory(), "Product category should not be null!");
            Assert.assertNotNull(product.getImage(), "Product image should not be null!");

            logger.debug("Validated product: ID={}, Title={}", product.getId(), product.getTitle());
        }

        logger.info("All products have valid structure with required fields");
    }


    @Test(priority = 2,
            description = "Verify getting all products with limit parameter",
            dataProvider = "productLimitParams",
            dataProviderClass = ProductDataProvider.class)
    public void testGetAllProductsWithLimit(int limit) {
        logTestInfo("TC-PROD-002", "Verify getting all products with limit parameter: " + limit);

        // Send GET request with limit parameter
        Response response = productService.getAllProductsWithLimit(limit);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Validate response is an array
        ResponseValidator.validateResponseIsArray(response);

        // Validate array size matches limit
        ResponseValidator.validateArraySize(response, limit);

        // Deserialize response to list of products
        List<ProductResponse> products = response.jsonPath().getList("$", ProductResponse.class);

        // Validate products list size matches limit
        Assert.assertEquals(products.size(), limit,
                "Expected " + limit + " products, but got: " + products.size());

        logger.info("Successfully retrieved exactly {} products with limit parameter", limit);

        // Validate structure of returned products
        for (ProductResponse product : products) {
            Assert.assertNotNull(product.getId(), "Product ID should not be null!");
            Assert.assertNotNull(product.getTitle(), "Product title should not be null!");
        }

        logger.info("All {} products have correct structure", limit);
    }


    @Test(priority = 3,
            description = "Verify getting all products with invalid limit parameter",
            dataProvider = "invalidProductLimitParams",
            dataProviderClass = ProductDataProvider.class)
    public void testGetAllProductsWithInvalidLimit(int invalidLimit) {
        logTestInfo("TC-PROD-003", "Verify getting all products with invalid limit parameter: " + invalidLimit);

        // Send GET request with invalid limit parameter
        Response response = productService.getAllProductsWithLimit(invalidLimit);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Invalid limit parameter correctly rejected with status 400");
    }


    @Test(priority = 4,
            description = "Verify getting single product by valid ID",
            dataProvider = "validProductIds",
            dataProviderClass = ProductDataProvider.class)
    public void testGetProductByValidId(int productId) {
        logTestInfo("TC-PROD-004", "Verify getting single product by valid ID: " + productId);

        // Send GET request to get product by ID
        Response response = productService.getProductById(productId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Deserialize response to product object
        ProductResponse product = response.as(ProductResponse.class);

        // Validate product ID matches requested ID
        Assert.assertEquals(product.getId(), Integer.valueOf(productId),
                "Product ID should match requested ID!");

        // Validate all required fields are present
        Assert.assertNotNull(product.getTitle(), "Product title should not be null!");
        Assert.assertNotNull(product.getPrice(), "Product price should not be null!");
        Assert.assertNotNull(product.getDescription(), "Product description should not be null!");
        Assert.assertNotNull(product.getCategory(), "Product category should not be null!");
        Assert.assertNotNull(product.getImage(), "Product image should not be null!");
        Assert.assertNotNull(product.getRating(), "Product rating should not be null!");

        logger.info("Successfully retrieved product: ID={}, Title={}, Price={}",
                product.getId(), product.getTitle(), product.getPrice());
        logger.info("Product has all mandatory fields with valid data");
    }


    @Test(priority = 5,
            description = "Verify getting single product with non-existent ID",
            dataProvider = "nonExistentProductIds",
            dataProviderClass = ProductDataProvider.class)
    public void testGetProductByNonExistentId(int productId) {
        logTestInfo("TC-PROD-005", "Verify getting single product with non-existent ID: " + productId);

        // Send GET request to get product by non-existent ID
        Response response = productService.getProductById(productId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 or 404
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.BAD_REQUEST.getCode() ||
                        statusCode == StatusCode.NOT_FOUND.getCode(),
                "Status code should be 400 or 404, but got: " + statusCode);

        // Validate error response or null value
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.isEmpty() ||
                        responseBody.contains("error") ||
                        responseBody.equals("null"),
                "Response should contain error message or null value");

        logger.info("Non-existent product ID correctly returned error response");
    }


    @Test(priority = 6,
            description = "Verify getting single product with invalid ID format",
            dataProvider = "invalidProductIds",
            dataProviderClass = ProductDataProvider.class)
    public void testGetProductByInvalidIdFormat(String invalidProductId) {
        logTestInfo("TC-PROD-006", "Verify getting single product with invalid ID format: " + invalidProductId);

        // Send GET request with invalid ID format
        Response response = productService.getProductByIdInvalidFormat(invalidProductId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Invalid product ID format correctly rejected with status 400");
    }
}