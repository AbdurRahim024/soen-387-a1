package com.mywebapp;

import com.mywebapp.logic.custom_errors.DataMapperException;
import com.mywebapp.logic.custom_errors.FileDownloadException;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager {

    public enum DbParameter {
        URL,
        USERNAME,
        PASSWORD
    }

    private static JSONObject getConfig() throws DataMapperException {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get("/Users/emmuh/Documents/COMP/SOEN 387/soen-387-a1/MyWebApp/src/main/java/com/mywebapp/db_config.json")));
            return new JSONObject(content);
        } catch (IOException | JSONException e) {
            throw new DataMapperException("Error while reading the config file " + e);
        }
    }

    public static String getDbParameter(DbParameter parameter) throws DataMapperException {
        JSONObject json = getConfig();
        JSONObject database;
        try {
            database = json.getJSONObject("database");
            return database.getString(parameter.toString().toLowerCase());
        } catch (JSONException e) {
            throw new DataMapperException("Error while reading the config file " + e);
        }
    }

    public static String getCsvPath() throws FileDownloadException {
        JSONObject json;
        try {
            json = getConfig();
            JSONObject csv = json.getJSONObject("users_csv");
            return csv.getString("path");
        } catch (JSONException | DataMapperException e) {
            throw new FileDownloadException("Error while reading the config file " + e);
        }

    }

}
