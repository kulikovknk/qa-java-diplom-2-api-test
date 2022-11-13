package praktikum.ordertest;

import client.OrderClient;
import client.UserClient;
import dto.OrderRequest;
import dto.UserLoginRequest;
import dto.UserLogoutRequest;
import generator.LoginUserRequestGenerator;
import generator.LogoutUserRequestGenerator;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;

import static generator.OrderRequestGenerator.*;
import static generator.UserRequestGenerator.getExistingUserRequest;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class OrderBaseTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private UserLoginRequest userLoginRequest;


    @Before
    public void setUp() {

        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        logoutUser();
    }

    public void checkExistingUserLogIn() {

        userLoginRequest = LoginUserRequestGenerator.from(getExistingUserRequest());

        //  проверим, что успешно зашли под созданной учетной записью
        ValidatableResponse response = userClient.loginUser(userLoginRequest)
                .log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

        // сохраним токены для дальнейшего выхода и удаления пользователя
        userLoginRequest.setRefreshToken(response.extract().path("refreshToken"));
        userLoginRequest.setAuthorization(response.extract().path("accessToken"));

    }

    public void logoutUser() {

        if (userLoginRequest != null) {

            UserLogoutRequest userLogoutRequest = LogoutUserRequestGenerator.from(userLoginRequest);

            // разлогинимся и удалим созданную учетную запись
            if (userLogoutRequest.getToken() != null) {

                userClient.logoutUser(userLogoutRequest)
                        .log().all()
                        .assertThat()
                        .statusCode(SC_OK)
                        .and()
                        .body("success", equalTo(true))
                        .and()
                        .body("message", equalTo("Successful logout"));

            }
        }

    }

    public ArrayList<ArrayList<String>> getIngredientsList() {

        int i = 0;

        ArrayList<String> bunsList = new ArrayList<>();
        ArrayList<String> saucesList = new ArrayList<>();
        ArrayList<String> fillingsList = new ArrayList<>();
        ArrayList<ArrayList<String>> ingredientsList = new ArrayList<>();

        ValidatableResponse ingredients = orderClient.getIngredients()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("data", notNullValue());

        while (i < 10) {

            try {

                String type = ingredients.extract().body().path(String.format("data[%s].type", i));

                switch (type) {
                    case "bun":
                        bunsList.add(ingredients.extract().body().path(String.format("data[%s]._id", i)));
                        break;
                    case "sauce":
                        saucesList.add(ingredients.extract().body().path(String.format("data[%s]._id", i)));
                        break;
                    case "main":
                        fillingsList.add(ingredients.extract().body().path(String.format("data[%s]._id", i)));
                        break;
                }

                i++;

            } catch (Exception exception) {
                break;
            }

        }

        ingredientsList.add(bunsList);
        ingredientsList.add(saucesList);
        ingredientsList.add(fillingsList);

        return ingredientsList;

    }

    public void checkOrderCreationForAuthorizedUser() {

        OrderRequest orderRequest = getNewOrderRequestWithIngredients(getIngredientsList());

        orderClient.createOrder(orderRequest, userLoginRequest.getAuthorization())
                .log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order._id", notNullValue());

    }

    public void checkOrderCreationForNotAuthorizedUser() {

        OrderRequest orderRequest = getNewOrderRequestWithIngredients(getIngredientsList());

        orderClient.createOrder(orderRequest, null)
                .log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));

    }

    public void checkOrderCreationWithInvalidIngredientsHash() {

        OrderRequest orderRequest = getNewOrderRequestWithInvalidIngredients();

        orderClient.createOrder(orderRequest, userLoginRequest.getAuthorization())
                .log().all()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);

    }

    public void checkOrderCreationWithNoIngredients() {

        OrderRequest orderRequest = getNewOrderRequestWithNoIngredients();

        orderClient.createOrder(orderRequest, userLoginRequest.getAuthorization())
                .log().all()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("One or more ids provided are incorrect"));

    }

    public void checkGetOrderListAuthorizedUser() {

        orderClient.getOrderList(userLoginRequest.getAuthorization())
                .log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("total", notNullValue());

    }

    public void checkGetOrderListNotAuthorizedUser() {

        orderClient.getOrderList(null)
                .log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));

    }

}
