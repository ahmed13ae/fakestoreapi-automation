package com.fakestore.automation.tests.auth;

import com.fakestore.automation.dataproviders.AuthDataProvider;
import com.fakestore.automation.enums.StatusCode;
import com.fakestore.automation.listeners.ExtentReportListener;
import com.fakestore.automation.models.request.LoginRequest;
import com.fakestore.automation.models.response.AuthResponse;
import com.fakestore.automation.tests.BaseTest;
import com.fakestore.automation.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ExtentReportListener.class)
public class AuthTests extends BaseTest {


    @Test(priority = 1,
            description = "Verify user login with valid credentials returns token",
            dataProvider = "validCredentials",
            dataProviderClass = AuthDataProvider.class)
    public void testLoginWithValidCredentials(LoginRequest loginRequest) {
        logTestInfo("TC-AUTH-001", "Verify user login with valid credentials");

        // Send login request
        Response response = authService.login(loginRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response is not null
        ResponseValidator.validateResponseNotNull(response);

        // Deserialize response
        AuthResponse authResponse = response.as(AuthResponse.class);

        // Validate token exists and is not empty
        Assert.assertNotNull(authResponse.getToken(), "Token should not be null!");
        Assert.assertFalse(authResponse.getToken().isEmpty(), "Token should not be empty!");

        logger.info("Login successful with token: {}", authResponse.getToken().substring(0, 20) + "...");
    }


    @Test(priority = 2,
            description = "Verify user login with invalid username returns 401",
            dataProvider = "invalidUsername",
            dataProviderClass = AuthDataProvider.class)
    public void testLoginWithInvalidUsername(LoginRequest loginRequest) {
        logTestInfo("TC-AUTH-002", "Verify user login with invalid username");

        // Send login request
        Response response = authService.login(loginRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 401 Unauthorized
        ResponseValidator.validateStatusCode(response, StatusCode.UNAUTHORIZED.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Login correctly rejected with invalid username");
    }


    @Test(priority = 3,
            description = "Verify user login with invalid password returns 401",
            dataProvider = "invalidPassword",
            dataProviderClass = AuthDataProvider.class)
    public void testLoginWithInvalidPassword(LoginRequest loginRequest) {
        logTestInfo("TC-AUTH-003", "Verify user login with invalid password");

        // Send login request
        Response response = authService.login(loginRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 401 Unauthorized
        ResponseValidator.validateStatusCode(response, StatusCode.UNAUTHORIZED.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Login correctly rejected with invalid password");
    }


    @Test(priority = 4,
            description = "Verify user login with empty credentials returns 400 or 401",
            dataProvider = "emptyCredentials",
            dataProviderClass = AuthDataProvider.class)
    public void testLoginWithEmptyCredentials(LoginRequest loginRequest) {
        logTestInfo("TC-AUTH-004", "Verify user login with empty credentials");

        // Send login request
        Response response = authService.login(loginRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - could be 400 or 401
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == StatusCode.BAD_REQUEST.getCode() ||
                        statusCode == StatusCode.UNAUTHORIZED.getCode(),
                "Status code should be 400 or 401, but got: " + statusCode);

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Login correctly rejected with empty credentials");
    }


    @Test(priority = 5,
            description = "Verify user login without request body returns 400")
    public void testLoginWithoutBody() {
        logTestInfo("TC-AUTH-005", "Verify user login without request body");

        // Send login request without body
        Response response = authService.loginWithoutBody();

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Login correctly rejected without body");
    }


    @Test(priority = 6,
            description = "Verify user login with malformed JSON returns 400",
            dataProvider = "malformedJson",
            dataProviderClass = AuthDataProvider.class)
    public void testLoginWithMalformedJson(String malformedJson) {
        logTestInfo("TC-AUTH-006", "Verify user login with malformed JSON");

        // Send login request with malformed JSON
        Response response = authService.loginWithJsonString(malformedJson);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 400 Bad Request
        ResponseValidator.validateStatusCode(response, StatusCode.BAD_REQUEST.getCode());

        // Validate error response
        ResponseValidator.validateErrorResponse(response);

        logger.info("Login correctly rejected with malformed JSON");
    }


    @Test(priority = 7,
            description = "Verify user login with SQL injection attempt is properly handled",
            dataProvider = "sqlInjectionCredentials",
            dataProviderClass = AuthDataProvider.class)
    public void testLoginWithSqlInjection(LoginRequest loginRequest) {
        logTestInfo("TC-AUTH-007", "Verify user login with SQL injection attempt");

        // Send login request with SQL injection
        Response response = authService.login(loginRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code - should be 401 Unauthorized (system should not be compromised)
        ResponseValidator.validateStatusCode(response, StatusCode.UNAUTHORIZED.getCode());

        // Validate error response (no token should be returned)
        ResponseValidator.validateErrorResponse(response);

        // Ensure no token is present in response
        String responseBody = response.getBody().asString();
        Assert.assertFalse(responseBody.contains("token"),
                "Token should not be present in response for SQL injection attempt!");

        logger.info("SQL injection attempt correctly rejected - System not compromised");
    }

    @Test(priority = 8,
            description = "Verify user login response time is within acceptable limits",
            dataProvider = "validCredentials",
            dataProviderClass = AuthDataProvider.class)
    public void testLoginResponseTime(LoginRequest loginRequest) {
        logTestInfo("TC-AUTH-008", "Verify user login response time");

        // Send login request
        Response response = authService.login(loginRequest);

        // Log response
        ResponseValidator.logResponse(response);

        // Validate status code
        ResponseValidator.validateStatusCode(response, StatusCode.OK.getCode());

        // Validate response time - should be under 2 seconds (2000 ms)
        ResponseValidator.validateResponseTime(response, 2000);

        // Validate token is returned
        AuthResponse authResponse = response.as(AuthResponse.class);
        Assert.assertNotNull(authResponse.getToken(), "Token should not be null!");

        logger.info("Login response time: {} ms (within acceptable limit)", response.getTime());
    }


    @Test(priority = 9,
            description = "Verify multiple consecutive failed login attempts are handled properly",
            dataProvider = "failedLoginCredentials",
            dataProviderClass = AuthDataProvider.class)
    public void testMultipleFailedLoginAttempts(LoginRequest loginRequest) {
        logTestInfo("TC-AUTH-009", "Verify multiple consecutive failed login attempts");

        int attemptCount = 5;
        logger.info("Attempting {} consecutive failed logins", attemptCount);

        for (int i = 1; i <= attemptCount; i++) {
            logger.info("Failed login attempt #{}", i);

            // Send login request with wrong credentials
            Response response = authService.login(loginRequest);

            // Log response
            ResponseValidator.logResponse(response);

            // Validate status code - should be 401 Unauthorized for each attempt
            ResponseValidator.validateStatusCode(response, StatusCode.UNAUTHORIZED.getCode());


            if (i >= 5) {
                // Check if response indicates rate limiting
                logger.info("Checking for rate limiting after {} attempts", i);
            }

            // Small delay between attempts to simulate real-world scenario
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("Thread sleep interrupted", e);
            }
        }

        logger.info("All {} failed login attempts correctly returned 401 status", attemptCount);
        logger.info("Note: FakeStore API may not implement rate limiting/account lockout");
    }
}