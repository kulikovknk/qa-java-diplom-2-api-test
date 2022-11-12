package praktikum;

import client.UserClient;
import dto.LoginUserRequest;
import dto.LogoutUserRequest;
import dto.UserRequest;
import generator.LoginUserRequestGenerator;
import generator.LogoutUserRequestGenerator;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static generator.UserRequestGenerator.getNewUserRequest;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserLoginTest {

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

//            userClient.logoutUser(logoutUserRequest)
//                    .log().all()
//                    .assertThat()
//                    .body("success", equalTo(true))
//                    .and()
//                    .body("message", equalTo("Successful logout"));

//            userClient.deleteUser(loginUserRequest)
//                    .assertThat()
//                    .body("success", equalTo(true));

        }
    }

    @Test
    @DisplayName("Check new user can log in")
    //    логин под существующим пользователем
    public void newUserLoginTest() {

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

    }

    @Test
    @DisplayName("Check user can't log in with non-existent email and login")
    //    логин с неверным логином и паролем
    public void userNameNotFilledTest() {

        // создаем нового пользователя, который еще не зарегистрирован
        userRequest = getNewUserRequest();

        loginUserRequest = LoginUserRequestGenerator.from(userRequest);

        //  ошибка при входе под несуществующими учетными данными
        userClient.loginUser(loginUserRequest)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));

    }

}
