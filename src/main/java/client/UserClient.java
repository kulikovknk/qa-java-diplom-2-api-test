package client;

import dto.*;
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
    public ValidatableResponse loginUser(UserLoginRequest userLoginRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .body(userLoginRequest)
                .post(getAPIUserLogin())
                .then();
    }

    public ValidatableResponse logoutUser(UserLogoutRequest userLogoutRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .body(userLogoutRequest)
                .post(getAPIUserLogout())
                .then();
    }

    // update user
    public ValidatableResponse updateUser(UserUpdateRequest userUpdateRequest, String accessToken) {

        return given()
                .spec(getDefaultRequestSpec())
                .header("authorization", accessToken != null ? accessToken : "")
                .body(userUpdateRequest)
                .patch(getAPIUserUpdate())
                .then();
    }

    // delete user
    public ValidatableResponse deleteUser(UserDeleteRequest userDeleteRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .header("authorization", userDeleteRequest.getAuthorization())
                .delete(getAPIUserUpdate())
                .then();
    }

}
