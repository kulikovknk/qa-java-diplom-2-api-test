package generator;

import dto.UserDeleteRequest;
import dto.UserLoginRequest;

public class DeleteUserRequestGenerator {

    public static UserDeleteRequest from (UserLoginRequest userLoginRequest) {

        UserDeleteRequest userDeleteRequest = new UserDeleteRequest();
        userDeleteRequest.setAuthorization(userLoginRequest.getAuthorization());

        return userDeleteRequest;
    }

}
