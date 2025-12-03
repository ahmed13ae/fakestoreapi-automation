package com.fakestore.automation.tests.products;

import com.fakestore.automation.dataproviders.ProductDataProvider;
import com.fakestore.automation.enums.StatusCode;
import com.fakestore.automation.listeners.ExtentReportListener;
import com.fakestore.automation.models.request.ProductRequest;
import com.fakestore.automation.models.response.ProductResponse;
import com.fakestore.automation.tests.BaseTest;
import com.fakestore.automation.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExtentReportListener.class)
public class ProductPutTests extends BaseTest {


    @Test(priority = 10,
            description = "Verify updating product with valid data using PUT",
            dataProvider = "validProductUpdateData",
            dataProviderClass = ProductDataProvider.class)
    public void testUpdateProductWithValidData(ProductRequest productRequest) {
        logTestInfo("TC-PROD-010", "Verify updating product with valid data using PUT");

        int productIdToUpdate = 7;
        logger.info("Updating product ID: {} with new data", productIdToUpdate);

        // Send PUT request to update product
        Response response = productService.updateProduct(productIdToUpdate, productRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 200 OK
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Deserialize response to product object
        ProductResponse updatedProduct = response.as(ProductResponse.class);

        // Validate response contains product with correct ID
        Assert.assertEquals(updatedProduct.getId(), Integer.valueOf(productIdToUpdate),
                "Updated product ID should match!");

        logger.info("Product updated successfully with ID: {}", updatedProduct.getId());

        // Validate all fields are updated with new values
        Assert.assertEquals(updatedProduct.getTitle(), productRequest.getTitle(),
                "Updated product title should match request!");
        Assert.assertEquals(updatedProduct.getPrice(), productRequest.getPrice(),
                "Updated product price should match request!");
        Assert.assertEquals(updatedProduct.getDescription(), productRequest.getDescription(),
                "Updated product description should match request!");
        Assert.assertEquals(updatedProduct.getImage(), productRequest.getImage(),
                "Updated product image should match request!");
        Assert.assertEquals(updatedProduct.getCategory(), productRequest.getCategory(),
                "Updated product category should match request!");

        logger.info("All fields in updated product reflect the new values");
    }


    @Test(priority = 11,
            description = "Verify updating non-existent product using PUT",
            dataProvider = "validProductUpdateData",
            dataProviderClass = ProductDataProvider.class)
    public void testUpdateNonExistentProduct(ProductRequest productRequest) {
        logTestInfo("TC-PROD-011", "Verify updating non-existent product using PUT");

        int nonExistentProductId = 99999;
        logger.info("Attempting to update non-existent product ID: {}", nonExistentProductId);

        // Send PUT request to update non-existent product
        Response response = productService.updateProduct(nonExistentProductId, productRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 or 404
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.BAD_REQUEST.getCode() ||
                        statusCode == StatusCode.NOT_FOUND.getCode(),
                "Status code should be 400 or 404, but got: " + statusCode);

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Update correctly rejected for non-existent product");
    }


    @Test(priority = 12,
            description = "Verify updating product with invalid data format using PUT",
            dataProvider = "invalidProductUpdateData",
            dataProviderClass = ProductDataProvider.class)
    public void testUpdateProductWithInvalidDataFormat(String invalidProductJson) {
        logTestInfo("TC-PROD-012", "Verify updating product with invalid data format using PUT");

        int productIdToUpdate = 1;
        logger.info("Attempting to update product ID: {} with invalid data format", productIdToUpdate);

        // Send PUT request with invalid data format
        Response response = productService.updateProductWithJsonString(productIdToUpdate, invalidProductJson);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Product update correctly rejected due to invalid data format");
    }
}