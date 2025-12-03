package com.fakestore.automation.tests.carts;

import com.fakestore.automation.dataproviders.CartDataProvider;
import com.fakestore.automation.enums.StatusCode;
import com.fakestore.automation.listeners.ExtentReportListener;
import com.fakestore.automation.tests.BaseTest;
import com.fakestore.automation.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExtentReportListener.class)
public class CartDeleteTests extends BaseTest {


    @Test(priority = 14, description = "Verify deleting cart with valid ID")
    public void testDeleteCartWithValidId() {
        logTestInfo("TC-CART-014", "Verify deleting cart with valid ID");

        int cartIdToDelete = 6;
        logger.info("Deleting cart ID: {}", cartIdToDelete);

        // Send DELETE request to delete cart
        Response response = cartService.deleteCart(cartIdToDelete);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 200 OK
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Validate response contains deleted cart or success message
        String responseBody = response.getBody().asString();
        Assert.assertFalse(responseBody.isEmpty(), "Response body should not be empty!");

        logger.info("Cart deleted successfully - Response: {}", responseBody);
    }


    @Test(priority = 15,
            description = "Verify deleting cart with non-existent ID",
            dataProvider = "nonExistentCartIds",
            dataProviderClass = CartDataProvider.class)
    public void testDeleteCartWithNonExistentId(int nonExistentCartId) {
        logTestInfo("TC-CART-015", "Verify deleting cart with non-existent ID");

        logger.info("Attempting to delete non-existent cart ID: {}", nonExistentCartId);

        // Send DELETE request for non-existent cart
        Response response = cartService.deleteCart(nonExistentCartId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 or 404
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.BAD_REQUEST.getCode() ||
                        statusCode == StatusCode.NOT_FOUND.getCode(),
                "Status code should be 400 or 404, but got: " + statusCode);

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Delete correctly rejected for non-existent cart");
    }
}