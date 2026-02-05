package com.backend.openai.util;

public class Constants {
    // API Endpoints
    public static final String API_BASE_PATH = "/api/v1";
    public static final String OPENAI_ENDPOINT = "/openai";

    // OpenAI Related
    public static final String OPENAI_API_KEY_HEADER = "OpenAI-Api-Key";
    public static final String OPENAI_DEFAULT_MODEL = "gpt-3.5-turbo";

    // Response Messages
    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Failure";
    public static final String ERROR_GENERIC = "An error occurred. Please try again later.";
    public static final String ERROR_INVALID_REQUEST = "Invalid request.";
    public static final String ERROR_UNAUTHORIZED = "Unauthorized access.";

    // Other Constants
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String UTF_8 = "UTF-8";
    
    // HTTP Response Codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_ERROR = 500;
    
    // Custom String Codes for HTTP Statuses
    public static final String RAM_200 = "RAM_200";
    public static final String RAM_201 = "RAM_201";
    public static final String RAM_400 = "RAM_400";
    public static final String RAM_401 = "RAM_401";
    public static final String RAM_403 = "RAM_403";
    public static final String RAM_404 = "RAM_404";
    public static final String RAM_500 = "RAM_500";
    

}
