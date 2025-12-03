package com.fakestore.automation.tests.carts;

import com.fakestore.automation.dataproviders.CartDataProvider;
import com.fakestore.automation.enums.StatusCode;
import com.fakestore.automation.listeners.ExtentReportListener;
import com.fakestore.automation.models.request.CartRequest;
import com.fakestore.automation.models.response.CartResponse;
import com.fakestore.automation.tests.BaseTest;
import com.fakestore.automation.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExtentReportListener.class)
public class CartPostTests extends BaseTest {


    @Test(priority = 7,
            description = "Verify adding new cart with valid data",
            dataProvider = "validCartData",
            dataProviderClass = CartDataProvider.class)
    public void testCreateCartWithValidData(CartRequest cartRequest) {
        logTestInfo("TC-CART-007", "Verify adding new cart with valid data");

        logger.info("Creating cart for user ID: {}", cartRequest.getUserId());

        // Send POST request to create cart
        Response response = cartService.createCart(cartRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 201 Created
        // Note: FakeStore API might return 200 instead of 201
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.CREATED.getCode() ||
                        statusCode == StatusCode.OK.getCode(),
                "Status code should be 200 or 201, but got: " + statusCode);

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Deserialize response to cart object
        CartResponse createdCart = response.as(CartResponse.class);

        // Validate response contains created cart with generated ID
        Assert.assertNotNull(createdCart.getId(), "Created cart should have an ID!");

        logger.info("Cart created successfully with ID: {}", createdCart.getId());

        // Validate all fields match the request data
        Assert.assertEquals(createdCart.getUserId(), cartRequest.getUserId(),
                "Cart userId should match request!");
        Assert.assertNotNull(createdCart.getProducts(), "Cart products should not be null!");
        Assert.assertEquals(createdCart.getProducts().size(), cartRequest.getProducts().size(),
                "Cart products count should match request!");

        logger.info("All fields in created cart match the request payload");
    }


    @Test(priority = 8,
            description = "Verify adding cart with missing required fields",
            dataProvider = "invalidCartMissingFields",
            dataProviderClass = CartDataProvider.class)
    public void testCreateCartWithMissingFields(String incompleteCartJson) {
        logTestInfo("TC-CART-008", "Verify adding cart with missing required fields");

        logger.info("Attempting to create cart with incomplete data");

        // Send POST request with incomplete data
        Response response = cartService.createCartWithJsonString(incompleteCartJson);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Cart creation correctly rejected due to missing required fields");
    }


    @Test(priority = 9,
            description = "Verify adding cart with empty products array",
            dataProvider = "invalidCartEmptyProducts",
            dataProviderClass = CartDataProvider.class)
    public void testCreateCartWithEmptyProducts(String emptyProductsCartJson) {
        logTestInfo("TC-CART-009", "Verify adding cart with empty products array");

        logger.info("Attempting to create cart with empty products array");

        // Send POST request with empty products array
        Response response = cartService.createCartWithJsonString(emptyProductsCartJson);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Cart creation correctly rejected due to empty products array");
    }


    @Test(priority = 10,
            description = "Verify adding cart with invalid product structure",
            dataProvider = "invalidCartStructure",
            dataProviderClass = CartDataProvider.class)
    public void testCreateCartWithInvalidProductStructure(String invalidCartJson) {
        logTestInfo("TC-CART-010", "Verify adding cart with invalid product structure");

        logger.info("Attempting to create cart with invalid product data format");

        // Send POST request with invalid product structure
        Response response = cartService.createCartWithJsonString(invalidCartJson);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Cart creation correctly rejected due to invalid product data format");
    }
}