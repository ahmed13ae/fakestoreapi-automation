package com.fakestore.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static String reportPath;


    @Override
    public void onStart(ITestContext context) {
        logger.info("Initializing Extent Reports...");

        // Create reports directory if it doesn't exist
        File reportsDir = new File("reports/extent-reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
            logger.info("Created reports directory: {}", reportsDir.getAbsolutePath());
        }

        // Generate report file name with timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        reportPath = "reports/extent-reports/TestReport_" + timestamp + ".html";

        // Initialize ExtentSparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // Configure report
        sparkReporter.config().setDocumentTitle("FakeStore API Automation Report");
        sparkReporter.config().setReportName("API Test Execution Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        sparkReporter.config().setEncoding("UTF-8");

        // Initialize ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Add system information
        extent.setSystemInfo("Application", "FakeStore API");
        extent.setSystemInfo("Base URL", "https://fakestoreapi.com");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Test Suite", context.getName());
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));

        logger.info("Extent Reports initialized successfully");
        logger.info("Report will be generated at: {}", reportPath);
    }


    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();

        logger.info("Starting test: {}", testName);

        // Create test in report
        ExtentTest test = extent.createTest(testName);

        // Add description if available
        if (testDescription != null && !testDescription.isEmpty()) {
            test.info("Description: " + testDescription);
        }

        // Add test class information
        test.info("Test Class: " + result.getTestClass().getName());

        // Set test to thread local
        extentTest.set(test);
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Test PASSED: {}", testName);

        ExtentTest test = extentTest.get();
        test.log(Status.PASS, "Test Passed: " + testName);
        test.pass("Execution Time: " + (result.getEndMillis() - result.getStartMillis()) + " ms");
    }


    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("Test FAILED: {}", testName);

        ExtentTest test = extentTest.get();
        test.log(Status.FAIL, "Test Failed: " + testName);
        test.fail("Failure Reason: " + result.getThrowable().getMessage());
        test.fail("Execution Time: " + (result.getEndMillis() - result.getStartMillis()) + " ms");

        // Add stack trace
        test.fail("Stack Trace: " + getStackTrace(result.getThrowable()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test SKIPPED: {}", testName);

        ExtentTest test = extentTest.get();
        test.log(Status.SKIP, "Test Skipped: " + testName);

        if (result.getThrowable() != null) {
            test.skip("Skip Reason: " + result.getThrowable().getMessage());
        }
    }


    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test FAILED but within success percentage: {}", testName);

        ExtentTest test = extentTest.get();
        test.log(Status.WARNING, "Test Failed but within success percentage: " + testName);
    }


    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finishing test execution and generating report...");

        if (extent != null) {
            extent.flush();
            logger.info("========================================");
            logger.info("Extent Report generated successfully!");
            logger.info("Report Location: {}", new File(reportPath).getAbsolutePath());
            logger.info("========================================");
        }
    }


    private String getStackTrace(Throwable throwable) {
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(throwable.toString()).append("\n");

        for (StackTraceElement element : throwable.getStackTrace()) {
            stackTrace.append("\tat ").append(element.toString()).append("\n");
        }

        return stackTrace.toString();
    }


    public static ExtentTest getTest() {
        return extentTest.get();
    }
}