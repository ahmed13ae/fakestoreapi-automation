package com.fakestore.automation.services;

import com.fakestore.automation.constants.ApiEndpoints;
import com.fakestore.automation.models.request.LoginRequest;
import io.restassured.response.Response;

public class AuthService extends BaseService {


    public Response login(LoginRequest loginRequest) {
        logger.info("Attempting login for user: {}", loginRequest.getUsername());
        return post(ApiEndpoints.LOGIN, loginRequest);
    }


    public Response loginWithJsonString(String jsonBody) {
        logger.info("Attempting login with JSON string");
        return postWithJsonString(ApiEndpoints.LOGIN, jsonBody);
    }


    public Response loginWithoutBody() {
        logger.info("Attempting login without body");
        return postWithoutBody(ApiEndpoints.LOGIN);
    }
}