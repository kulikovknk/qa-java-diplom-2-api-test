package generator;

import dto.LoginUserRequest;
import dto.LogoutUserRequest;

public class LogoutUserRequestGenerator {

    public static LogoutUserRequest from (LoginUserRequest loginUserRequest) {

        LogoutUserRequest logoutUserRequest = new LogoutUserRequest();
//        logoutUserRequest.setToken(String.format("{{%s}}", loginUserRequest.getRefreshToken()));
        logoutUserRequest.setToken(loginUserRequest.getRefreshToken());

        return logoutUserRequest;
    }
}
