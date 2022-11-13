package praktikum.usertest;

import client.UserClient;
import config.UserAttributeType;
import dto.*;
import generator.DeleteUserRequestGenerator;
import generator.LoginUserRequestGenerator;
import generator.LogoutUserRequestGenerator;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;

import static generator.UserRequestGenerator.getNewUserRequest;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserBaseTest {

    private UserClient userClient;
    private UserRequest userRequest;
    private UserLoginRequest userLoginRequest;
    private UserUpdateRequest userUpdateRequest;
    private UserLogoutRequest userLogoutRequest;
    private UserDeleteRequest userDeleteRequest;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        logoutUser();
        deleteUser();
    }

    public void checkUserCreation() {

        userRequest = getNewUserRequest();

        userClient.createUser(userRequest)
                .log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

    }

    public void checkUserDuplicateCreation() {

        userRequest = getNewUserRequest();

        // создали нового пользователя
        userClient.createUser(userRequest)
                .log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

        // успешно залогинились под учетными данными нового пользователя
        checkUserLogIn();

        // при попытке создать второго пользователя с такими же учетными данными
        // вернётся код ответа 403 Forbidden
        userClient.createUser(userRequest)
                .log().all()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));

    }

    public void checkUserWithEmptyAttributeCreation(UserAttributeType attributeType) {

        userRequest = getNewUserRequest();

        switch (attributeType) {

            case NAME:
                userRequest.setName(null);
                break;
            case EMAIL:
                userRequest.setEmail(null);
                break;
            case PASSWORD:
                userRequest.setPassword(null);
                break;
            default:
                System.out.println("Некорректно указан тип данных пользователя. " +
                        "Изменение данных пользователя не выполнено");
                break;
        }

        userClient.createUser(userRequest)
                .log().all()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

    public void checkUserLogIn() {

        userLoginRequest = LoginUserRequestGenerator.from(userRequest);

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

    public void checkUserWithInvalidEmailLogIn() {

        userRequest.setEmail("lsjbvlahgjdce@mail.ru");

        userLoginRequest = LoginUserRequestGenerator.from(userRequest);

        //  ошибка при входе под несуществующими учетными данными
        userClient.loginUser(userLoginRequest)
                .log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));

    }

    public void checkUserWithInvalidPasswordLogIn() {

        userRequest.setPassword("khvYTfKHvbvHD^R^RUVgvkY^ru6");

        userLoginRequest = LoginUserRequestGenerator.from(userRequest);

        //  ошибка при входе под несуществующими учетными данными
        userClient.loginUser(userLoginRequest)
                .log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));

    }

    public void checkUserUpdate(UserAttributeType attributeType) {

        userUpdateRequest = new UserUpdateRequest();

        switch (attributeType) {
            case NAME:

                String newName = RandomStringUtils.randomAlphabetic(7);

                userUpdateRequest.setName(newName);

                userClient.updateUser(userUpdateRequest, userLoginRequest.getAuthorization())
                        .log().all()
                        .assertThat()
                        .statusCode(SC_OK)
                        .and()
                        .body("success", equalTo(true))
                        .and()
                        .body("user.name", equalTo(newName));

                break;

            case EMAIL:

                String newEmail = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(7).toLowerCase());

                userUpdateRequest.setEmail(newEmail);

                userClient.updateUser(userUpdateRequest, userLoginRequest.getAuthorization())
                        .log().all()
                        .assertThat()
                        .statusCode(SC_OK)
                        .and()
                        .body("success", equalTo(true))
                        .and()
                        .body("user.email", equalTo(newEmail));
                break;

            case PASSWORD:

                String newPassword = RandomStringUtils.randomAlphabetic(10);

                userUpdateRequest.setPassword(newPassword);

                userClient.updateUser(userUpdateRequest, userLoginRequest.getAuthorization())
                        .log().all()
                        .assertThat()
                        .statusCode(SC_OK)
                        .and()
                        .body("success", equalTo(true));

                break;

            default:

                System.out.println("Некорректно указан тип данных пользователя. " +
                        "Изменение данных пользователя не выполнено");
                break;

        }
    }

    public void checkNotAuthorizedUserUpdate(UserAttributeType attributeType) {

        userUpdateRequest = new UserUpdateRequest();

        switch (attributeType) {
            case NAME:

                String newName = RandomStringUtils.randomAlphabetic(7);

                userUpdateRequest.setName(newName);

                userClient.updateUser(userUpdateRequest, null)
                        .log().all()
                        .assertThat()
                        .statusCode(SC_UNAUTHORIZED)
                        .and()
                        .body("success", equalTo(false))
                        .and()
                        .body("message", equalTo("You should be authorised"));

                break;

            case EMAIL:

                String newEmail = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(7).toLowerCase());

                userUpdateRequest.setEmail(newEmail);

                userClient.updateUser(userUpdateRequest, null)
                        .log().all()
                        .assertThat()
                        .statusCode(SC_UNAUTHORIZED)
                        .and()
                        .body("success", equalTo(false))
                        .and()
                        .body("message", equalTo("You should be authorised"));
                break;

            case PASSWORD:

                String newPassword = RandomStringUtils.randomAlphabetic(10);

                userUpdateRequest.setPassword(newPassword);

                userClient.updateUser(userUpdateRequest, null)
                        .log().all()
                        .assertThat()
                        .statusCode(SC_UNAUTHORIZED)
                        .and()
                        .body("success", equalTo(false))
                        .and()
                        .body("message", equalTo("You should be authorised"));

                break;

            default:
                System.out.println("Некорректно указан тип данных пользователя. " +
                        "Изменение данных не выполнено");
                break;
        }

    }

    public void logoutUser() {

        if (userLoginRequest != null) {

            userLogoutRequest = LogoutUserRequestGenerator.from(userLoginRequest);

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

    public void deleteUser() {

        if (userLoginRequest != null) {

            userDeleteRequest = DeleteUserRequestGenerator.from(userLoginRequest);

            if (userDeleteRequest.getAuthorization() != null) {

                userClient.deleteUser(userDeleteRequest)
                        .log().all()
                        .assertThat()
                        .statusCode(SC_ACCEPTED)
                        .and()
                        .body("success", equalTo(true))
                        .and()
                        .body("message", equalTo("User successfully removed"));

            }
        }
    }

}
