package com.fakestore.automation.utils;

import com.fakestore.automation.constants.ApiEndpoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestBuilder {
    private static final Logger logger = LogManager.getLogger(RequestBuilder.class);


    public static RequestSpecification buildRequestSpec() {
        logger.debug("Building default request specification");

        return new RequestSpecBuilder()
                .setBaseUri(ApiEndpoints.BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "FakeStore-Automation-Framework")
                .build();
    }


    public static RequestSpecification buildRequestSpec(String basePath) {
        logger.debug("Building request specification with base path: {}", basePath);

        return new RequestSpecBuilder()
                .setBaseUri(ApiEndpoints.BASE_URI)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "FakeStore-Automation-Framework")
                .build();
    }


    public static RequestSpecification buildRequestSpecWithAuth(String token) {
        logger.debug("Building request specification with authentication");

        return new RequestSpecBuilder()
                .setBaseUri(ApiEndpoints.BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "FakeStore-Automation-Framework")
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}