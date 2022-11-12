package praktikum;

import client.UserClient;
import dto.LoginUserRequest;
import dto.LogoutUserRequest;
import dto.UserRequest;
import generator.LoginUserRequestGenerator;
import generator.LogoutUserRequestGenerator;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static generator.UserRequestGenerator.getNewUserRequest;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest {

    private UserClient userClient;
    private UserRequest userRequest;
    private LoginUserRequest loginUserRequest;
    private LogoutUserRequest logoutUserRequest;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {

        logoutUserRequest = LogoutUserRequestGenerator.from(loginUserRequest);

        // разлогинимся и удалим созданную учетную запись
        if (logoutUserRequest.getToken() != null) {

            userClient.logoutUser(logoutUserRequest)
                    .log().all()
                    .assertThat()
                    .body("success", equalTo(true))
                    .and()
                    .body("message", equalTo("Successful logout"));

//            userClient.deleteUser(loginUserRequest)
//                    .assertThat()
//                    .body("success", equalTo(true));

        }

    }


    @Test
    @DisplayName("Check new user can be created")
    //    тест на создание уникального пользователя
    public void createNewUserTest() {

        userRequest = getNewUserRequest();

        userClient.createUser(userRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

        loginUserRequest = LoginUserRequestGenerator.from(userRequest);

        //  успешно зашли под созданной учетной записью
        String refreshToken = userClient.loginUser(loginUserRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("refreshToken");

        loginUserRequest.setRefreshToken(refreshToken);

    }

    @Test
    @DisplayName("Check identical user can't be created")
    //    тест на создание пользователя, который уже зарегистрирован
    public void createTwoIdenticalUsersTest() {

        userRequest = getNewUserRequest();

        userClient.createUser(userRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

        loginUserRequest = LoginUserRequestGenerator.from(userRequest);

        //  успешно зашли под созданной учетной записью
        userClient.loginUser(loginUserRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

        // нельзя создать второго пользователя с такими же учетными данными
        userClient.createUser(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));

    }

    @Test
    @DisplayName("Check courier can't be created with the empty name")
    //    создать пользователя и не заполнить одно из обязательных полей (name)
    public void userNameNotFilledTest() {

        userRequest = getNewUserRequest();

        userRequest.setName(null);

        userClient.createUser(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Check courier can't be created with the empty password")
    //    создать пользователя и не заполнить одно из обязательных полей (password)
    public void userPasswordNotFilledTest() {

        userRequest = getNewUserRequest();

        userRequest.setPassword(null);

        userClient.createUser(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Check courier can't be created with the empty email")
    //    создать пользователя и не заполнить одно из обязательных полей (Email)
    public void userEmailNotFilledTest() {

        userRequest = getNewUserRequest();

        userRequest.setEmail(null);

        userClient.createUser(userRequest)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

}
