package client;

import io.restassured.response.ValidatableResponse;
import dto.OrderRequest;

import static io.restassured.RestAssured.given;
import static config.Config.*;

public class OrderClient extends RestClient {

    public ValidatableResponse getIngredients() {
        return given()
                .spec(getDefaultRequestSpec())
                .get(getAPIGetIngredients())
                .then();
    }

    // create order
    public ValidatableResponse createOrder(OrderRequest orderRequest, String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .header("authorization", accessToken != null ? accessToken : "")
                .body(orderRequest)
                .post(getAPIOrderCreate())
                .then();
    }

     public ValidatableResponse getOrderList(String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .header("authorization", accessToken != null ? accessToken : "")
                .get(getApiOrderList())
                .then();
    }

}
