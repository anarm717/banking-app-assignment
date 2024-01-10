package az.company.auth.component;

public final class HttpConstants {

    public static final String APPLICATION_JSON = "application/json";

    public static final String ACCEPT_HEADER = "accept";
    public static final String CONTENT_TYPE = "Content-Type";

    public static final String AUTH_HEADER = "Authorization";

    public static final String X_AUTH_TOKEN = "X-Auth-Token";

    public static final String BASIC_AUTH_HEADER = "Basic";
    public static final String BEARER_AUTH_HEADER = "Bearer";
    public static final String BPM_VARIABLES = "USER_INPUTS";

    private HttpConstants() {
        throw new UnsupportedOperationException();
    }
}
