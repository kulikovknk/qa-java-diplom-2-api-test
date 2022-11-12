package client;

import io.restassured.response.ValidatableResponse;
import dto.OrderRequest;

import static io.restassured.RestAssured.given;
import static config.Config.*;

public class OrderClient extends RestClient {

    // create order
    public ValidatableResponse createOrder(OrderRequest orderRequest) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(orderRequest)
                .post()
                .then();
    }

     public ValidatableResponse getOrderList() {
        return given()
                .spec(getDefaultRequestSpec())
                .get()
                .then();
    }

}
