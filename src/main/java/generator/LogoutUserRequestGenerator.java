package generator;

import dto.UserLoginRequest;
import dto.UserLogoutRequest;

public class LogoutUserRequestGenerator {

    public static UserLogoutRequest from (UserLoginRequest userLoginRequest) {

        UserLogoutRequest userLogoutRequest = new UserLogoutRequest();
        userLogoutRequest.setToken(userLoginRequest.getRefreshToken());

        return userLogoutRequest;
    }
}
