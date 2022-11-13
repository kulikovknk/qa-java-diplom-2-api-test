package config;

public class Config {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String BASE_PATH = "/api";
    private static final String API_USER_CREATE = "/auth/register";
    private static final String API_USER_LOGIN = "/auth/login";
    private static final String API_USER_UPDATE = "/auth/user";
    private static final String API_USER_LOGOUT = "/auth/logout";
    private static final String API_GET_INGREDIENTS = "ingredients";
    private static final String API_ORDER_CREATE = "orders";
    private static final String API_ORDER_LIST = "orders";


    public static String getBaseUri() {
        return BASE_URI;
    }
    public static String getBasePath() {
        return BASE_PATH;
    }
    public static String getAPIUserCreate() {
        return API_USER_CREATE;
    }
    public static String getAPIUserLogin() {
        return API_USER_LOGIN;
    }
    public static String getAPIUserLogout() {
        return API_USER_LOGOUT;
    }
    public static String getAPIUserUpdate() {
        return API_USER_UPDATE;
    }
    public static String getAPIGetIngredients() {
        return API_GET_INGREDIENTS;
    }
    public static String getAPIOrderCreate() {
        return API_ORDER_CREATE;
    }
    public static String getApiOrderList() {
        return API_ORDER_LIST;
    }

}

