package bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi;

class HttpHelperMethods {
    private static final int FIRST_HTTP_CLIENT_ERROR = 400;
    private static final int FIRST_HTTP_SERVER_ERROR = 500;

    public static boolean isHttpClientError(int statusCode) {
        return statusCode >= FIRST_HTTP_CLIENT_ERROR && statusCode < FIRST_HTTP_SERVER_ERROR;
    }

    public static boolean isHttpServerError(int statusCode) {
        return statusCode >= FIRST_HTTP_SERVER_ERROR;
    }
}