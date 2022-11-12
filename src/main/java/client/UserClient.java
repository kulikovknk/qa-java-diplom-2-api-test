package client;

import dto.LoginUserRequest;
import dto.LogoutUserRequest;
import dto.UpdateUserRequest;
import dto.UserRequest;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static config.Config.*;

public class UserClient extends RestClient {

    // create user
    public ValidatableResponse createUser(UserRequest userRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .body(userRequest)
                .post(getAPIUserCreate())
                .then();
    }

    // login user
    public ValidatableResponse loginUser(LoginUserRequest loginUserRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .body(loginUserRequest)
                .post(getAPIUserLogin())
                .then();
    }

    public ValidatableResponse logoutUser(LogoutUserRequest logoutUserRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .body(logoutUserRequest)
                .post(getApiUserLogout())
                .then();
    }

    // update user
    public ValidatableResponse updateUser(UpdateUserRequest updateUserRequest, String accessToken) {

        return given()
                .spec(getDefaultRequestSpec())
                .header("authorization", accessToken)
                .body(updateUserRequest)
                .patch(getAPIUserUpdate())
                .then();
    }

    // delete user
    public ValidatableResponse deleteUser(LoginUserRequest loginUserRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .body(loginUserRequest)
                .delete(getAPIUserUpdate())
                .then();
    }

}
