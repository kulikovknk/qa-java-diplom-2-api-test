package praktikum;

import client.UserClient;
import dto.LoginUserRequest;
import dto.UpdateUserRequest;
import dto.UserRequest;
import generator.LoginUserRequestGenerator;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static generator.UserRequestGenerator.getNewUserRequest;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UpdateUserTest {

    private UserClient userClient;
    private UserRequest userRequest;
    private LoginUserRequest loginUserRequest;
    private UpdateUserRequest updateUserRequest;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

//    @After
//    public void tearDown() {
//
//        loginRequest = ru.yandex.practikum.generator.LoginRequestGenerator.from(courierRequest);
//
//        //  получим id для удаления созданной учетной записи
//        Integer id = courierClient.loginCourier(loginRequest)
//                .extract()
//                .path("id");
//
//        // удалим созданную учетную запись
//        if (id != null) {
//
//            ru.yandex.practikum.dto.DeleteRequest deleteRequest = new ru.yandex.practikum.dto.DeleteRequest(id);
//
//            courierClient.deleteCourier(deleteRequest)
//                    .assertThat()
//                    .statusCode(SC_OK)
//                    .and()
//                    .body("ok", equalTo(true));
//        }
//    }

    // изменение данных о пользователе (с авторизацией)

    @Test
    @DisplayName("Check the change of user name")
    // изменение поля name
    public void userNameChangeTest() {

        // создаем нового пользователя
        userRequest = getNewUserRequest();

        userClient.createUser(userRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

        loginUserRequest = LoginUserRequestGenerator.from(userRequest);

         //  успешно зашли под созданной учетной записью
        String accessToken = userClient.loginUser(loginUserRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        // сгенерим новое имя пользователя
        String newName = RandomStringUtils.randomAlphabetic(7);

        updateUserRequest = new UpdateUserRequest();

        updateUserRequest.setName(newName);

        userClient.updateUser(updateUserRequest, accessToken)
                .log().all()
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("name", equalTo(newName));

    }

    @Test
    @DisplayName("Check user password change")
    // изменение поля password
    public void userPasswordChangeTest() {

        // создаем нового пользователя
        userRequest = getNewUserRequest();

        userClient.createUser(userRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

        loginUserRequest = LoginUserRequestGenerator.from(userRequest);

        //  успешно зашли под созданной учетной записью
        String accessToken = userClient.loginUser(loginUserRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        // сгенерим новый password пользователя
        String newPassword = RandomStringUtils.randomAlphabetic(10);

        updateUserRequest = new UpdateUserRequest();

        updateUserRequest.setPassword(newPassword);

        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("password", equalTo(newPassword));

    }

    @Test
    @DisplayName("Check user email change")
    // изменение поля email
    public void userEmailChangeTest() {

        // создаем нового пользователя
        userRequest = getNewUserRequest();

        userClient.createUser(userRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());

        loginUserRequest = LoginUserRequestGenerator.from(userRequest);

        //  успешно зашли под созданной учетной записью
        String accessToken = userClient.loginUser(loginUserRequest)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");;

        // сгенерим новый email пользователя
        String newEmail = String.format("%s@mail.ru", RandomStringUtils.randomAlphabetic(10));

        updateUserRequest = new UpdateUserRequest();

        updateUserRequest.setEmail(newEmail);

        userClient.updateUser(updateUserRequest, accessToken)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("email", equalTo(newEmail));

    }

}
