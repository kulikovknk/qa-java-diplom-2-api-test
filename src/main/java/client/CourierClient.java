package client;

import client.RestClient;
import io.restassured.response.ValidatableResponse;
import dto.CourierRequest;
import dto.DeleteRequest;
import dto.LoginRequest;

import static io.restassured.RestAssured.given;
import static config.Config.*;

public class CourierClient extends RestClient {

    // create courier
    public ValidatableResponse createCourier(CourierRequest courierRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .body(courierRequest)
                .post()
                .then();
    }

    // login courier
    public ValidatableResponse loginCourier(LoginRequest loginRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .body(loginRequest)
                .post()
                .then();
    }

    // delete courier
    public ValidatableResponse deleteCourier(DeleteRequest deleteRequest) {

        return given()
                .spec(getDefaultRequestSpec())
                .delete()
                .then();
    }

}
