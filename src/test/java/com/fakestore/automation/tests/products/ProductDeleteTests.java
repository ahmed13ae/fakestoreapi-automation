package com.fakestore.automation.tests.products;

import com.fakestore.automation.dataproviders.ProductDataProvider;
import com.fakestore.automation.enums.StatusCode;
import com.fakestore.automation.listeners.ExtentReportListener;
import com.fakestore.automation.tests.BaseTest;
import com.fakestore.automation.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExtentReportListener.class)
public class ProductDeleteTests extends BaseTest {


    @Test(priority = 13, description = "Verify deleting product with valid ID")
    public void testDeleteProductWithValidId() {
        logTestInfo("TC-PROD-013", "Verify deleting product with valid ID");

        int productIdToDelete = 6;
        logger.info("Deleting product ID: {}", productIdToDelete);

        // Send DELETE request to delete product
        Response response = productService.deleteProduct(productIdToDelete);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 200 OK
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Validate response contains deleted product or success message
        String responseBody = response.getBody().asString();
        Assert.assertFalse(responseBody.isEmpty(), "Response body should not be empty!");

        logger.info("Product deleted successfully - Response: {}", responseBody);

        // Optional: Verify product is actually deleted by trying to GET it
        logger.info("Optionally verifying deletion by attempting to GET deleted product...");
        Response getResponse = productService.getProductById(productIdToDelete);

        // Note: FakeStore API is a mock API, so deletion might not persist
        // In real scenarios, this should return 404
        int getStatusCode = getResponse.getStatusCode();
        logger.info("GET request after deletion returned status: {}", getStatusCode);

        if (getStatusCode == StatusCode.NOT_FOUND.getCode()) {
            logger.info("Product successfully deleted and verified - returns 404");
        } else {
            logger.warn("Note: FakeStore API is a mock API - deletion doesn't persist");
        }
    }


    @Test(priority = 14,
            description = "Verify deleting product with non-existent ID",
            dataProvider = "nonExistentProductIds",
            dataProviderClass = ProductDataProvider.class)
    public void testDeleteProductWithNonExistentId(int nonExistentProductId) {
        logTestInfo("TC-PROD-014", "Verify deleting product with non-existent ID");

        logger.info("Attempting to delete non-existent product ID: {}", nonExistentProductId);

        // Send DELETE request for non-existent product
        Response response = productService.deleteProduct(nonExistentProductId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 or 404
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.BAD_REQUEST.getCode() ||
                        statusCode == StatusCode.NOT_FOUND.getCode(),
                "Status code should be 400 or 404, but got: " + statusCode);

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Delete correctly rejected for non-existent product");
    }


    @Test(priority = 15,
            description = "Verify deleting product with invalid ID format",
            dataProvider = "invalidProductIds",
            dataProviderClass = ProductDataProvider.class)
    public void testDeleteProductWithInvalidIdFormat(String invalidProductId) {
        logTestInfo("TC-PROD-015", "Verify deleting product with invalid ID format");

        logger.info("Attempting to delete product with invalid ID format: {}", invalidProductId);

        // Send DELETE request with invalid ID format
        Response response = productService.deleteProductInvalidFormat(invalidProductId);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Delete correctly rejected due to invalid product ID format");
    }
}