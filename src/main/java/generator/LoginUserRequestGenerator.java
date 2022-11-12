package generator;

import dto.LoginUserRequest;
import dto.UserRequest;

public class LoginUserRequestGenerator {

    public static LoginUserRequest from (UserRequest userRequest) {

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(userRequest.getEmail());
        loginUserRequest.setPassword(userRequest.getPassword());

        return loginUserRequest;

    }
}
