package com.fakestore.automation.utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;


public class ResponseValidator {
    private static final Logger logger = LogManager.getLogger(ResponseValidator.class);


    public static void validateStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();
        logger.info("Validating status code - Expected: {}, Actual: {}", expectedCode, actualCode);
        Assert.assertEquals(actualCode, expectedCode,
                "Status code mismatch! Expected: " + expectedCode + ", but got: " + actualCode);
    }


    public static void validateResponseNotNull(Response response) {
        logger.info("Validating response body is not null");
        Assert.assertNotNull(response.getBody(), "Response body is null!");
        Assert.assertFalse(response.getBody().asString().isEmpty(), "Response body is empty!");
    }


    public static void validateResponseTime(Response response, long maxTimeMillis) {
        long actualTime = response.getTime();
        logger.info("Validating response time - Expected: < {} ms, Actual: {} ms", maxTimeMillis, actualTime);
        Assert.assertTrue(actualTime < maxTimeMillis,
                "Response time exceeded! Expected: < " + maxTimeMillis + " ms, but got: " + actualTime + " ms");
    }


    public static void validateFieldExists(Response response, String fieldName) {
        logger.info("Validating field '{}' exists in response", fieldName);
        Assert.assertNotNull(response.jsonPath().get(fieldName),
                "Field '" + fieldName + "' does not exist in response!");
    }


    public static void validateFieldValue(Response response, String fieldName, Object expectedValue) {
        Object actualValue = response.jsonPath().get(fieldName);
        logger.info("Validating field '{}' - Expected: {}, Actual: {}", fieldName, expectedValue, actualValue);
        Assert.assertEquals(actualValue, expectedValue,
                "Field '" + fieldName + "' value mismatch! Expected: " + expectedValue + ", but got: " + actualValue);
    }


    public static void validateResponseIsArray(Response response) {
        logger.info("Validating response is an array");
        Assert.assertTrue(response.getBody().asString().trim().startsWith("["),
                "Response is not an array!");
    }


    public static void validateArraySize(Response response, int expectedSize) {
        int actualSize = response.jsonPath().getList("$").size();
        logger.info("Validating array size - Expected: {}, Actual: {}", expectedSize, actualSize);
        Assert.assertEquals(actualSize, expectedSize,
                "Array size mismatch! Expected: " + expectedSize + ", but got: " + actualSize);
    }


    public static void validateErrorResponse(Response response) {
        logger.info("Validating error response");
        String body = response.getBody().asString();
        Assert.assertTrue(body.contains("error") || body.contains("message") || body.isEmpty(),
                "Expected error response but got: " + body);
    }


    public static void logResponse(Response response) {
        logger.info("Response Status Code: {}", response.getStatusCode());
        logger.info("Response Time: {} ms", response.getTime());
        logger.debug("Response Body: {}", response.getBody().asPrettyString());
    }
}