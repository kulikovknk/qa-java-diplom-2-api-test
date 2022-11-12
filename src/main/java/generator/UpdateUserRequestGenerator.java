package generator;

import dto.UpdateUserRequest;
import dto.UserRequest;

public class UpdateUserRequestGenerator {

    public static UpdateUserRequest from (UserRequest userRequest) {

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
//        updateUserRequest.setName(userRequest.getName());
//        updateUserRequest.setPassword(userRequest.getPassword());
//        updateUserRequest.setEmail(userRequest.getEmail());

        return updateUserRequest;

    }

}
