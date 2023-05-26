package models;

import java.util.List;

public class ApiResponse {
    private String response_code;
    private String response_message;
    private String token;
    private List<ApiQuestion> results;

    public String getResponseCode() {
        return response_code;
    }

    public String getResponseMessage() {
        return response_message;
    }

    public String getToken() {
        return token;
    }

    public List<ApiQuestion> getResults() {
        return results;
    }
}