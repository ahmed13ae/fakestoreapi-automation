package com.fakestore.automation.dataproviders;

import com.fakestore.automation.models.request.ProductRequest;
import com.fakestore.automation.utils.JsonReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.testng.annotations.DataProvider;

public class ProductDataProvider {
    private static final Gson gson = new Gson();


    @DataProvider(name = "validProductData")
    public Object[][] getValidProductData() {
        ProductRequest product = JsonReader.readJson("products/valid-product.json", ProductRequest.class);
        return new Object[][]{
                {product}
        };
    }


    @DataProvider(name = "invalidProductMissingFields")
    public Object[][] getInvalidProductMissingFields() {
        String jsonString = JsonReader.readJsonAsString("products/invalid-product.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String missingFieldsJson = jsonObject.get("missingFields").toString();

        return new Object[][]{
                {missingFieldsJson}
        };
    }


    @DataProvider(name = "invalidProductPriceFormat")
    public Object[][] getInvalidProductPriceFormat() {
        String jsonString = JsonReader.readJsonAsString("products/invalid-product.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String invalidPriceJson = jsonObject.get("invalidPrice").toString();

        return new Object[][]{
                {invalidPriceJson}
        };
    }


    @DataProvider(name = "validProductUpdateData")
    public Object[][] getValidProductUpdateData() {
        String jsonString = JsonReader.readJsonAsString("products/update-product.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        ProductRequest updateProduct = gson.fromJson(
                jsonObject.get("validUpdate"),
                ProductRequest.class
        );

        return new Object[][]{
                {updateProduct}
        };
    }


    @DataProvider(name = "invalidProductUpdateData")
    public Object[][] getInvalidProductUpdateData() {
        String jsonString = JsonReader.readJsonAsString("products/update-product.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String invalidUpdateJson = jsonObject.get("invalidUpdate").toString();

        return new Object[][]{
                {invalidUpdateJson}
        };
    }


    @DataProvider(name = "validProductIds")
    public Object[][] getValidProductIds() {
        return new Object[][]{
                {1},
                {5},
                {10}
        };
    }


    @DataProvider(name = "invalidProductIds")
    public Object[][] getInvalidProductIds() {
        return new Object[][]{
                {"abc"},
                {"xyz123"},
                {"!@#"}
        };
    }


    @DataProvider(name = "nonExistentProductIds")
    public Object[][] getNonExistentProductIds() {
        return new Object[][]{
                {99999},
                {88888}
        };
    }


    @DataProvider(name = "productLimitParams")
    public Object[][] getProductLimitParams() {
        return new Object[][]{
                {5},
                {10},
                {1}
        };
    }


    @DataProvider(name = "invalidProductLimitParams")
    public Object[][] getInvalidProductLimitParams() {
        return new Object[][]{
                {-1},
                {0},
                {-100}
        };
    }
}
