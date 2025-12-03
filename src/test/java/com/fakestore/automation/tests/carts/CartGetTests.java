package com.fakestore.automation.tests.carts;

import com.fakestore.automation.dataproviders.CartDataProvider;
import com.fakestore.automation.enums.StatusCode;
import com.fakestore.automation.listeners.ExtentReportListener;
import com.fakestore.automation.models.response.CartResponse;
import com.fakestore.automation.tests.BaseTest;
import com.fakestore.automation.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

@Listeners(ExtentReportListener.class)
public class CartGetTests extends BaseTest {


    @Test(priority = 1, description = "Verify getting all carts successfully")
    public void testGetAllCarts() {
        logTestInfo("TC-CART-001", "Verify getting all carts successfully");

        // Send GET request to get all carts
        Response response = cartService.getAllCarts();

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Validate response is an array
        ResponseValidator.validateResponseIsArray(response);

        // Deserialize response to list of carts
        List<CartResponse> carts = response.jsonPath().getList("$", CartResponse.class);

        // Validate carts list is not empty
        Assert.assertFalse(carts.isEmpty(), "Carts list should not be empty!");

        logger.info("Total carts returned: {}", carts.size());

        // Validate each cart has required fields
        for (CartResponse cart : carts) {
            Assert.assertNotNull(cart.getId(), "Cart ID should not be null!");
            Assert.assertNotNull(cart.getUserId(), "Cart userId should not be null!");
            Assert.assertNotNull(cart.getDate(), "Cart date should not be null!");
            Assert.assertNotNull(cart.getProducts(), "Cart products should not be null!");
            Assert.assertFalse(cart.getProducts().isEmpty(), "Cart products should not be empty!");

            // Validate products array contains objects with productId and quantity
            for (CartResponse.Product product : cart.getProducts()) {
                Assert.assertNotNull(product.getProductId(), "Product ID should not be null!");
                Assert.assertNotNull(product.getQuantity(), "Product quantity should not be null!");
            }

            logger.debug("Validated cart: ID={}, UserID={}, Products={}",
                    cart.getId(), cart.getUserId(), cart.getProducts().size());
        }

        logger.info("All carts have valid structure with required fields");
    }


    @Test(priority = 2,
            description = "Verify getting single cart by valid ID",
            dataProvider = "validCartIds",
            dataProviderClass = CartDataProvider.class)
    public void testGetCartByValidId(int cartId) {
        logTestInfo("TC-CART-002", "Verify getting single cart by valid ID: " + cartId);

        // Send GET request to get cart by ID
        Response response = cartService.getCartById(cartId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Deserialize response to cart object
        CartResponse cart = response.as(CartResponse.class);

        // Validate cart ID matches requested ID
        Assert.assertEquals(cart.getId(), Integer.valueOf(cartId),
                "Cart ID should match requested ID!");

        // Validate all required fields are present
        Assert.assertNotNull(cart.getUserId(), "Cart userId should not be null!");
        Assert.assertNotNull(cart.getDate(), "Cart date should not be null!");
        Assert.assertNotNull(cart.getProducts(), "Cart products should not be null!");

        logger.info("Successfully retrieved cart: ID={}, UserID={}, Products={}",
                cart.getId(), cart.getUserId(), cart.getProducts().size());
        logger.info("Cart has all required fields with valid structure");
    }


    @Test(priority = 3,
            description = "Verify getting single cart with non-existent ID",
            dataProvider = "nonExistentCartIds",
            dataProviderClass = CartDataProvider.class)
    public void testGetCartByNonExistentId(int cartId) {
        logTestInfo("TC-CART-003", "Verify getting single cart with non-existent ID: " + cartId);

        // Send GET request to get cart by non-existent ID
        Response response = cartService.getCartById(cartId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 or 404
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.BAD_REQUEST.getCode() ||
                        statusCode == StatusCode.NOT_FOUND.getCode(),
                "Status code should be 400 or 404, but got: " + statusCode);

        // Validate error response or null value
        ResponseValidator.validateErrorResponse(response);

        logger.info("Non-existent cart ID correctly returned error response");
    }


    @Test(priority = 4,
            description = "Verify getting single cart with invalid ID format",
            dataProvider = "invalidCartIds",
            dataProviderClass = CartDataProvider.class)
    public void testGetCartByInvalidIdFormat(String invalidCartId) {
        logTestInfo("TC-CART-004", "Verify getting single cart with invalid ID format: " + invalidCartId);

        // Send GET request with invalid ID format
        Response response = cartService.getCartByIdInvalidFormat(invalidCartId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Invalid cart ID format correctly rejected with status 400");
    }


    @Test(priority = 5,
            description = "Verify getting carts for specific user",
            dataProvider = "validUserIds",
            dataProviderClass = CartDataProvider.class)
    public void testGetCartsByUserId(int userId) {
        logTestInfo("TC-CART-005", "Verify getting carts for specific user: " + userId);

        // Send GET request to get carts by user ID
        Response response = cartService.getCartsByUserId(userId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Validate response is an array
        ResponseValidator.validateResponseIsArray(response);

        // Deserialize response to list of carts
        List<CartResponse> carts = response.jsonPath().getList("$", CartResponse.class);

        logger.info("Total carts returned for user {}: {}", userId, carts.size());

        // Validate all carts belong to the specified user
        for (CartResponse cart : carts) {
            Assert.assertEquals(cart.getUserId(), Integer.valueOf(userId),
                    "All carts should have userId=" + userId);

            // Validate cart structure
            Assert.assertNotNull(cart.getId(), "Cart ID should not be null!");
            Assert.assertNotNull(cart.getDate(), "Cart date should not be null!");
            Assert.assertNotNull(cart.getProducts(), "Cart products should not be null!");

            logger.debug("Validated cart: ID={}, UserID={}", cart.getId(), cart.getUserId());
        }

        logger.info("All carts belong to user {} and have correct structure", userId);
    }


    @Test(priority = 6,
            description = "Verify getting carts for non-existent user",
            dataProvider = "nonExistentUserIds",
            dataProviderClass = CartDataProvider.class)
    public void testGetCartsByNonExistentUserId(int userId) {
        logTestInfo("TC-CART-006", "Verify getting carts for non-existent user: " + userId);

        // Send GET request to get carts by non-existent user ID
        Response response = cartService.getCartsByUserId(userId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        int statusCode = response.getStatusCode();

        // Could return 200 with empty array OR 400/404 with error message
        if (statusCode == StatusCode.OK.getCode()) {
            // If 200, should return empty array
            List<CartResponse> carts = response.jsonPath().getList("$", CartResponse.class);
            Assert.assertTrue(carts.isEmpty(), "Carts list should be empty for non-existent user!");
            logger.info("Non-existent user correctly returned empty array");
        } else {
            // If error status, validate it's 400 or 404
            Assert.assertTrue(statusCode == StatusCode.BAD_REQUEST.getCode() ||
                            statusCode == StatusCode.NOT_FOUND.getCode(),
                    "Status code should be 200, 400, or 404, but got: " + statusCode);
            ResponseValidator.validateErrorResponse(response);
            logger.info("Non-existent user correctly returned error response");
        }
    }
}