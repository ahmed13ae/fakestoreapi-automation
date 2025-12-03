package com.fakestore.automation.dataproviders;

import com.fakestore.automation.models.request.LoginRequest;
import com.fakestore.automation.utils.JsonReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.testng.annotations.DataProvider;

public class AuthDataProvider {
    private static final Gson gson = new Gson();


    @DataProvider(name = "validCredentials")
    public Object[][] getValidCredentials() {
        LoginRequest credentials = JsonReader.readJson("auth/valid-credentials.json", LoginRequest.class);
        return new Object[][]{
                {credentials}
        };
    }


    @DataProvider(name = "invalidUsername")
    public Object[][] getInvalidUsername() {
        String jsonString = JsonReader.readJsonAsString("auth/invalid-credentials.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        LoginRequest credentials = gson.fromJson(
                jsonObject.get("invalidUsername"),
                LoginRequest.class
        );

        return new Object[][]{
                {credentials}
        };
    }


    @DataProvider(name = "invalidPassword")
    public Object[][] getInvalidPassword() {
        String jsonString = JsonReader.readJsonAsString("auth/invalid-credentials.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        LoginRequest credentials = gson.fromJson(
                jsonObject.get("invalidPassword"),
                LoginRequest.class
        );

        return new Object[][]{
                {credentials}
        };
    }


    @DataProvider(name = "emptyCredentials")
    public Object[][] getEmptyCredentials() {
        String jsonString = JsonReader.readJsonAsString("auth/invalid-credentials.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        LoginRequest credentials = gson.fromJson(
                jsonObject.get("emptyCredentials"),
                LoginRequest.class
        );

        return new Object[][]{
                {credentials}
        };
    }


    @DataProvider(name = "malformedJson")
    public Object[][] getMalformedJson() {
        String jsonString = JsonReader.readJsonAsString("auth/invalid-credentials.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String malformedJson = jsonObject.get("malformedJson").getAsString();

        return new Object[][]{
                {malformedJson}
        };
    }


    @DataProvider(name = "sqlInjectionCredentials")
    public Object[][] getSqlInjectionCredentials() {
        String jsonString = JsonReader.readJsonAsString("auth/invalid-credentials.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        LoginRequest credentials = gson.fromJson(
                jsonObject.get("sqlInjection"),
                LoginRequest.class
        );

        return new Object[][]{
                {credentials}
        };
    }


    @DataProvider(name = "failedLoginCredentials")
    public Object[][] getFailedLoginCredentials() {
        String jsonString = JsonReader.readJsonAsString("auth/invalid-credentials.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        LoginRequest credentials = gson.fromJson(
                jsonObject.get("failedLogin"),
                LoginRequest.class
        );

        return new Object[][]{
                {credentials}
        };
    }


    @DataProvider(name = "multipleInvalidCredentials")
    public Object[][] getMultipleInvalidCredentials() {
        String jsonString = JsonReader.readJsonAsString("auth/invalid-credentials.json");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        LoginRequest invalidUsername = gson.fromJson(
                jsonObject.get("invalidUsername"),
                LoginRequest.class
        );

        LoginRequest invalidPassword = gson.fromJson(
                jsonObject.get("invalidPassword"),
                LoginRequest.class
        );

        LoginRequest emptyCredentials = gson.fromJson(
                jsonObject.get("emptyCredentials"),
                LoginRequest.class
        );

        return new Object[][]{
                {invalidUsername, "Invalid username"},
                {invalidPassword, "Invalid password"},
                {emptyCredentials, "Empty credentials"}
        };
    }
}