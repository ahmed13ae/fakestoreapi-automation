package com.fakestore.automation.tests;

import com.fakestore.automation.services.AuthService;
import com.fakestore.automation.services.CartService;
import com.fakestore.automation.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    // Service instances - available to all test classes
    protected ProductService productService;
    protected CartService cartService;
    protected AuthService authService;


    @BeforeSuite
    public void suiteSetup() {
        logger.info("========================================");
        logger.info("Starting FakeStore API Test Automation");
        logger.info("========================================");
        logger.info("Base URI: https://fakestoreapi.com");
        logger.info("Test Suite Started at: {}", java.time.LocalDateTime.now());
    }


    @BeforeClass
    public void setUp() {
        logger.info("Initializing service instances for test class: {}", this.getClass().getSimpleName());

        // Initialize services
        productService = new ProductService();
        cartService = new CartService();
        authService = new AuthService();

        logger.info("Service instances initialized successfully");
    }


    @AfterSuite
    public void suiteTearDown() {
        logger.info("========================================");
        logger.info("Test Suite Completed at: {}", java.time.LocalDateTime.now());
        logger.info("FakeStore API Test Automation Finished");
        logger.info("========================================");
    }


    protected void logTestInfo(String testName, String testDescription) {
        logger.info("-------------------------------------------");
        logger.info("Test: {}", testName);
        logger.info("Description: {}", testDescription);
        logger.info("-------------------------------------------");
    }
}