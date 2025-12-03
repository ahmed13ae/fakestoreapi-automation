package com.fakestore.automation.dataproviders;

import com.fakestore.automation.models.request.CartRequest;
import com.fakestore.automation.utils.JsonReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.testng.annotations.DataProvider;

public class CartDataProvider {
    private static final Gson gson = new Gson();


    @DataProvider(name = "validCartData")
    public Object[][] getValidCartData() {
        CartRequest cart = JsonReader.readJson("carts/valid-cart.json", CartRequest.class);
        return new Object[][]{
                {cart}
        };
    }


    @DataProvider(name = "invalidCartMissingFields")
    public Object[][] getInvalidCartMissingFields() {
        String jsonString = JsonReader.readJsonAsString("carts/invalid-cart.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String missingFieldsJson = jsonObject.get("missingFields").toString();

        return new Object[][]{
                {missingFieldsJson}
        };
    }


    @DataProvider(name = "invalidCartEmptyProducts")
    public Object[][] getInvalidCartEmptyProducts() {
        String jsonString = JsonReader.readJsonAsString("carts/invalid-cart.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String emptyProductsJson = jsonObject.get("emptyProducts").toString();

        return new Object[][]{
                {emptyProductsJson}
        };
    }


    @DataProvider(name = "invalidCartStructure")
    public Object[][] getInvalidCartStructure() {
        String jsonString = JsonReader.readJsonAsString("carts/invalid-cart.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String invalidStructureJson = jsonObject.get("invalidStructure").toString();

        return new Object[][]{
                {invalidStructureJson}
        };
    }


    @DataProvider(name = "validCartUpdateData")
    public Object[][] getValidCartUpdateData() {
        String jsonString = JsonReader.readJsonAsString("carts/update-cart.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        CartRequest updateCart = gson.fromJson(
                jsonObject.get("validUpdate"),
                CartRequest.class
        );

        return new Object[][]{
                {updateCart}
        };
    }


    @DataProvider(name = "invalidCartUpdateData")
    public Object[][] getInvalidCartUpdateData() {
        String jsonString = JsonReader.readJsonAsString("carts/update-cart.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String invalidUpdateJson = jsonObject.get("invalidUpdate").toString();

        return new Object[][]{
                {invalidUpdateJson}
        };
    }


    @DataProvider(name = "validCartIds")
    public Object[][] getValidCartIds() {
        return new Object[][]{
                {1},
                {5},
                {7}
        };
    }


    @DataProvider(name = "invalidCartIds")
    public Object[][] getInvalidCartIds() {
        return new Object[][]{
                {"xyz"},
                {"abc123"},
                {"!@#"}
        };
    }


    @DataProvider(name = "nonExistentCartIds")
    public Object[][] getNonExistentCartIds() {
        return new Object[][]{
                {99999},
                {88888}
        };
    }


    @DataProvider(name = "validUserIds")
    public Object[][] getValidUserIds() {
        return new Object[][]{
                {1},
                {2},
                {3}
        };
    }


    @DataProvider(name = "nonExistentUserIds")
    public Object[][] getNonExistentUserIds() {
        return new Object[][]{
                {99999},
                {88888}
        };
    }
}