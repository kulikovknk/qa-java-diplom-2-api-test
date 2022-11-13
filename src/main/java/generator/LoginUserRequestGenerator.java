package generator;

import dto.UserLoginRequest;
import dto.UserRequest;

public class LoginUserRequestGenerator {

    public static UserLoginRequest from (UserRequest userRequest) {

        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail(userRequest.getEmail());
        userLoginRequest.setPassword(userRequest.getPassword());

        return userLoginRequest;

    }
}
