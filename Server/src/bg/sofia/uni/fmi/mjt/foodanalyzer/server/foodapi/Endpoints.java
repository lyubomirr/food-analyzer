package bg.sofia.uni.fmi.mjt.foodanalyzer.server.foodapi;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.configuration.ServerConfiguration;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

class Endpoints {
    private static final String FOOD_API_BASE_URL = "https://api.nal.usda.gov/fdc/v1/";
    private static final String SEARCH_ENDPOINT = constructSearchEndpoint();
    private static final String DETAILS_ENDPOINT = constructDetailsEndpoint();

    private static String constructSearchEndpoint() {
        StringBuilder builder = new StringBuilder();

        builder.append(FOOD_API_BASE_URL);
        builder.append("search");

        addQueryParameter(builder, QueryParameters.API_KEY, ServerConfiguration.FOOD_API_KEY);
        addQueryParameter(builder, QueryParameters.REQUIRE_ALL_WORDS, String.valueOf(true));
        addQueryParameter(builder, QueryParameters.SEARCH_INPUT, "%s", false);

        return builder.toString();
    }

    private static String constructDetailsEndpoint() {
        StringBuilder builder = new StringBuilder();

        builder.append(FOOD_API_BASE_URL);
        builder.append("%s");

        addQueryParameter(builder, QueryParameters.API_KEY, ServerConfiguration.FOOD_API_KEY);

        return builder.toString();
    }

    private static void addQueryParameter(StringBuilder urlBuilder, String name, String value) {
        addQueryParameter(urlBuilder, name, value, true);
    }

    private static void addQueryParameter(StringBuilder urlBuilder, String name, String value,
                                          boolean encodeValue) {
        final int notFoundIndex = -1;

        if (urlBuilder.indexOf("?") == notFoundIndex) {
            urlBuilder.append("?");
        }
        else if (urlBuilder.lastIndexOf("&") != urlBuilder.length() - 1) {
            urlBuilder.append("&");
        }

        value = encodeValue ? URLEncoder.encode(value) : value;
        urlBuilder.append(String.format("%s=%s", name, value));
    }


    public static URI createSearchUrl(String searchInput) {
        try {
            var url = String.format(SEARCH_ENDPOINT, URLEncoder.encode(searchInput));
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Couldn't construct search url!");
        }
    }

    public static URI createDetailsUrl(String foodId) {
        try {
            var url = String.format(DETAILS_ENDPOINT, URLEncoder.encode(foodId));
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Couldn't construct search url!");
        }
    }
}
