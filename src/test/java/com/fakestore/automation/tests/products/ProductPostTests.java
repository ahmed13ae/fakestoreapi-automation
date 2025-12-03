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
public class ProductPostTests extends BaseTest {


    @Test(priority = 7,
            description = "Verify adding new product with valid data",
            dataProvider = "validProductData",
            dataProviderClass = ProductDataProvider.class)
    public void testCreateProductWithValidData(ProductRequest productRequest) {
        logTestInfo("TC-PROD-007", "Verify adding new product with valid data");

        logger.info("Creating product with title: {}", productRequest.getTitle());

        // Send POST request to create product
        Response response = productService.createProduct(productRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 201 Created
        // Note: FakeStore API returns 200 instead of 201, adjust based on actual API behavior
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.CREATED.getCode() ||
                        statusCode == StatusCode.OK.getCode(),
                "Status code should be 200 or 201, but got: " + statusCode);

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Deserialize response to product object
        ProductResponse createdProduct = response.as(ProductResponse.class);

        // Validate response contains created product with generated ID
        Assert.assertNotNull(createdProduct.getId(), "Created product should have an ID!");

        logger.info("Product created successfully with ID: {}", createdProduct.getId());

        // Validate all fields match the request data
        Assert.assertEquals(createdProduct.getTitle(), productRequest.getTitle(),
                "Product title should match request!");
        Assert.assertEquals(createdProduct.getPrice(), productRequest.getPrice(),
                "Product price should match request!");
        Assert.assertEquals(createdProduct.getDescription(), productRequest.getDescription(),
                "Product description should match request!");
        Assert.assertEquals(createdProduct.getImage(), productRequest.getImage(),
                "Product image should match request!");
        Assert.assertEquals(createdProduct.getCategory(), productRequest.getCategory(),
                "Product category should match request!");

        logger.info("All fields in created product match the request payload");
    }


    @Test(priority = 8,
            description = "Verify adding new product with missing required fields",
            dataProvider = "invalidProductMissingFields",
            dataProviderClass = ProductDataProvider.class)
    public void testCreateProductWithMissingFields(String incompleteProductJson) {
        logTestInfo("TC-PROD-008", "Verify adding new product with missing required fields");

        logger.info("Attempting to create product with incomplete data");

        // Send POST request with incomplete data
        Response response = productService.createProductWithJsonString(incompleteProductJson);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Product creation correctly rejected due to missing required fields");
    }


    @Test(priority = 9,
            description = "Verify adding new product with invalid price format",
            dataProvider = "invalidProductPriceFormat",
            dataProviderClass = ProductDataProvider.class)
    public void testCreateProductWithInvalidPriceFormat(String invalidProductJson) {
        logTestInfo("TC-PROD-009", "Verify adding new product with invalid price format");

        logger.info("Attempting to create product with invalid price format");

        // Send POST request with invalid price format
        Response response = productService.createProductWithJsonString(invalidProductJson);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Product creation correctly rejected due to invalid price format");
    }
}