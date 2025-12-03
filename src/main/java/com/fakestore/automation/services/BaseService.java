package com.fakestore.automation.services;

import com.fakestore.automation.utils.RequestBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class BaseService {
    protected static final Logger logger = LogManager.getLogger(BaseService.class);
    protected RequestSpecification requestSpec;

    public BaseService() {
        this.requestSpec = RequestBuilder.buildRequestSpec();
    }


    protected Response get(String endpoint) {
        logger.info("Sending GET request to: {}", endpoint);
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }


    protected Response get(String endpoint, String pathParamName, Object pathParamValue) {
        logger.info("Sending GET request to: {} with path param: {}={}", endpoint, pathParamName, pathParamValue);
        Response response = given()
                .spec(requestSpec)
                .pathParam(pathParamName, pathParamValue)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }


    protected Response getWithQueryParam(String endpoint, String queryParamName, Object queryParamValue) {
        logger.info("Sending GET request to: {} with query param: {}={}", endpoint, queryParamName, queryParamValue);
        Response response = given()
                .spec(requestSpec)
                .queryParam(queryParamName, queryParamValue)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }


    protected Response post(String endpoint, Object body) {
        logger.info("Sending POST request to: {}", endpoint);
        logger.debug("Request body: {}", body);
        Response response = given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }


    protected Response postWithJsonString(String endpoint, String jsonBody) {
        logger.info("Sending POST request to: {} with JSON string", endpoint);
        logger.debug("Request body: {}", jsonBody);
        Response response = given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }


    protected Response postWithoutBody(String endpoint) {
        logger.info("Sending POST request to: {} without body", endpoint);
        Response response = given()
                .spec(requestSpec)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }


    protected Response put(String endpoint, String pathParamName, Object pathParamValue, Object body) {
        logger.info("Sending PUT request to: {} with path param: {}={}", endpoint, pathParamName, pathParamValue);
        logger.debug("Request body: {}", body);
        Response response = given()
                .spec(requestSpec)
                .pathParam(pathParamName, pathParamValue)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }


    protected Response putWithJsonString(String endpoint, String pathParamName, Object pathParamValue, String jsonBody) {
        logger.info("Sending PUT request to: {} with path param: {}={}", endpoint, pathParamName, pathParamValue);
        logger.debug("Request body: {}", jsonBody);
        Response response = given()
                .spec(requestSpec)
                .pathParam(pathParamName, pathParamValue)
                .body(jsonBody)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }


    protected Response delete(String endpoint, String pathParamName, Object pathParamValue) {
        logger.info("Sending DELETE request to: {} with path param: {}={}", endpoint, pathParamName, pathParamValue);
        Response response = given()
                .spec(requestSpec)
                .pathParam(pathParamName, pathParamValue)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();

        logger.info("Received response with status code: {}", response.getStatusCode());
        return response;
    }
}