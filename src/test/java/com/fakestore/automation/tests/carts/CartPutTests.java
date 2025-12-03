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
public class CartPutTests extends BaseTest {


    @Test(priority = 11,
            description = "Verify updating cart with valid data using PUT",
            dataProvider = "validCartUpdateData",
            dataProviderClass = CartDataProvider.class)
    public void testUpdateCartWithValidData(CartRequest cartRequest) {
        logTestInfo("TC-CART-011", "Verify updating cart with valid data using PUT");

        int cartIdToUpdate = 7;
        logger.info("Updating cart ID: {} with new data", cartIdToUpdate);

        // Send PUT request to update cart
        Response response = cartService.updateCart(cartIdToUpdate, cartRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 200 OK
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Deserialize response to cart object
        CartResponse updatedCart = response.as(CartResponse.class);

        // Validate response contains cart with correct ID
        Assert.assertEquals(updatedCart.getId(), Integer.valueOf(cartIdToUpdate),
                "Updated cart ID should match!");

        logger.info("Cart updated successfully with ID: {}", updatedCart.getId());

        // Validate all fields are updated with new values
        Assert.assertEquals(updatedCart.getUserId(), cartRequest.getUserId(),
                "Updated cart userId should match request!");
        Assert.assertNotNull(updatedCart.getProducts(), "Updated cart products should not be null!");

        logger.info("All fields in updated cart reflect the new values");
    }


    @Test(priority = 12,
            description = "Verify updating non-existent cart using PUT",
            dataProvider = "validCartUpdateData",
            dataProviderClass = CartDataProvider.class)
    public void testUpdateNonExistentCart(CartRequest cartRequest) {
        logTestInfo("TC-CART-012", "Verify updating non-existent cart using PUT");

        int nonExistentCartId = 99999;
        logger.info("Attempting to update non-existent cart ID: {}", nonExistentCartId);

        // Send PUT request to update non-existent cart
        Response response = cartService.updateCart(nonExistentCartId, cartRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 or 404
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.BAD_REQUEST.getCode() ||
                        statusCode == StatusCode.NOT_FOUND.getCode(),
                "Status code should be 400 or 404, but got: " + statusCode);

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Update correctly rejected for non-existent cart");
    }


    @Test(priority = 13,
            description = "Verify updating cart with invalid data format using PUT",
            dataProvider = "invalidCartUpdateData",
            dataProviderClass = CartDataProvider.class)
    public void testUpdateCartWithInvalidDataFormat(String invalidCartJson) {
        logTestInfo("TC-CART-013", "Verify updating cart with invalid data format using PUT");

        int cartIdToUpdate = 1;
        logger.info("Attempting to update cart ID: {} with invalid data format", cartIdToUpdate);

        // Send PUT request with invalid data format
        Response response = cartService.updateCartWithJsonString(cartIdToUpdate, invalidCartJson);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Cart update correctly rejected due to invalid data format");
    }
}