package com.fakestore.automation.utils;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class JsonReader {
    private static final Logger logger = LogManager.getLogger(JsonReader.class);
    private static final Gson gson = new Gson();
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";


    public static <T> T readJson(String filePath, Class<T> classType) {
        String fullPath = Paths.get(TEST_DATA_PATH + filePath).toString();
        logger.info("Reading JSON file from: {}", fullPath);

        try (FileReader reader = new FileReader(fullPath)) {
            T object = gson.fromJson(reader, classType);
            logger.info("Successfully parsed JSON to {}", classType.getSimpleName());
            return object;
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", fullPath, e);
            throw new RuntimeException("Failed to read JSON file: " + fullPath, e);
        }
    }


    public static String readJsonAsString(String filePath) {
        String fullPath = Paths.get(TEST_DATA_PATH + filePath).toString();
        logger.info("Reading JSON file as string from: {}", fullPath);

        try (FileReader reader = new FileReader(fullPath)) {
            StringBuilder jsonString = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                jsonString.append((char) character);
            }
            logger.info("Successfully read JSON as string");
            return jsonString.toString();
        } catch (IOException e) {
            logger.error("Failed to read JSON file: {}", fullPath, e);
            throw new RuntimeException("Failed to read JSON file: " + fullPath, e);
        }
    }


    public static String toJsonString(Object object) {
        logger.debug("Converting object to JSON string");
        return gson.toJson(object);
    }
}
