# 🛒 FakeStoreAPI Automation Framework

A scalable, maintainable **REST API Test Automation Framework** built using **Java 21**, **RestAssured**, **TestNG**, **Extent Reports**, **Log4j2**, and **Maven**.  
This project automates testing for the public API: **https://fakestoreapi.com**

---

## 📌 Table of Contents
- [Introduction](#introduction)
- [Framework Architecture](#framework-architecture)
- [Tech Stack & Why We Use Them](#tech-stack--why-we-use-them)
- [Project Structure](#project-structure)
- [How to Run the Tests](#how-to-run-the-tests)
- [Test Data Management](#test-data-management)
- [Reports](#reports)
- [Logging](#logging)
- [Future Enhancements](#future-enhancements)

---

# 📘 Introduction

This framework is designed for **API testing** of FakeStoreAPI. It follows industry best practices including:

- Layered test design  
- Clear separation of concerns  
- Reusable service classes  
- Data-driven testing  
- Consistent logging & reporting  

The goal is to provide a production-ready, scalable structure suitable for real-world API automation.

---

# 🏗 Framework Architecture

Test Layer → TestNG test cases
Service Layer → API calls (GET, POST, PUT, DELETE)
Model Layer → Request & Response POJOs
Utility Layer → Builders, Validators, JSON helpers
Config Layer → Endpoints, Status Codes
Test Data Layer → JSON test data
Reports & Logs → Extent Reports + Log4j2



This modular architecture ensures clean code, easy maintenance, and high reusability.

---

# ⚙ Tech Stack & Why We Use Them

### **Java 21**
Modern LTS version with top performance and new language features.

---

### **RestAssured**
Best-in-class library for API automation.
- Fluent syntax  
- In-built JSON parsing  
- Simple request/response validation  
- Native support for POJOs  

---

### **TestNG**
Chosen as the test runner because:
- Excellent data provider support  
- Test grouping & suite management  
- Parallel execution capabilities  
- Modern annotation model  

---

### **Extent Reports**
Used for generating beautiful, interactive HTML reports.
- Shows API request & response  
- Supports step-level logging  
- Dashboard summary  
- Professional reporting output  

---

### **Log4j2**
Handles logging throughout the framework.
- High-performance async logging  
- Configurable using XML  
- Provides execution visibility  
- Logs stored in `/logs/application.log`

---

### **Gson**
Used for serialization/deserialization of JSON.
- Lightweight  
- Easy to integrate  
- Works seamlessly with RestAssured  

---

### **Maven**
Used for dependency resolution & build execution.
- Easy dependency management  
- Surefire integration  
- Clean packaging & lifecycle execution  

---

# ▶ How to Run the Tests

### **1. Clone the repository**

git clone https://github.com/<your-username>/fakestoreapi-automation.git
cd fakestoreapi-automation
### **2. Run the full test suite**

mvn clean test
### **3. Run specific TestNG suite**

mvn clean test -DsuiteXmlFile=src/test/resources/suites/testng.xml
### **4. Run a specific test class**

mvn -Dtest=ProductGetTests test
# 📊 Test Data Management
All test input data is stored under:

src/test/resources/testdata/
Organized by module:

/auth

/carts

/products

The JsonReader utility reads JSON and maps it to POJO models for clean data-driven testing.

# 📑 Reports
📍 Extent Reports are generated here:

/reports/extent-reports/
Open the generated index.html for:

Test execution summary

Request/response logs

Step-level status

Failure traces

# 🧾 Logging
Logs are saved under:


/logs/application.log
Configured via:


src/main/resources/log4j2.xml
Provides:

INFO-level flow logs

ERROR logs on failures

Optional DEBUG logs
