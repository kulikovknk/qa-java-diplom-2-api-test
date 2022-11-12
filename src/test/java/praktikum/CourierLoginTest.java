import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.CourierClient;
import dto.CourierRequest;
import dto.DeleteRequest;
import dto.LoginRequest;
import generator.LoginRequestGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static generator.CourierRequestGenerator.getRandomCourierRequest;

public class CourierLoginTest {

    private CourierClient courierClient;
    private CourierRequest courierRequest;
    private LoginRequest loginRequest;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  получим id для удаления созданной учетной записи
        Integer id = courierClient.loginCourier(loginRequest)
                .extract()
                .path("id");

        // удалим созданную учетную запись
        if (id != null) {

            DeleteRequest deleteRequest = new DeleteRequest(id);

            courierClient.deleteCourier(deleteRequest)
                    .assertThat()
                    .statusCode(SC_OK)
                    .and()
                    .body("ok", equalTo(true));
        }
    }

    @Test
    @DisplayName("Check courier can log in")
    //    курьер может авторизоваться
    //    успешный запрос возвращает id
    public void courierLoginTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью, запрос вернул id
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

    }

    @Test
    @DisplayName("Check courier can't log in with the non-existent credentials")
    //    если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    public void nonExistentCourierLoginTest() {

        courierRequest = getRandomCourierRequest();

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  запрос с несуществующей парой логин-пароль
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }


    @Test
    @DisplayName("Check courier can't log in with the non-existent login")
    //  система вернёт ошибку, если неправильно указать логин
    public void wrongCourierLoginTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью, запрос вернул id
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

        //  меняем логин курьера на несуществующий
        loginRequest.setLogin(new StringBuilder(loginRequest.getLogin()).reverse().toString());

        //  запрос с несуществующим логином возвращяет ошибку
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Check courier can't log in with the non-existent password")
    //  система вернёт ошибку, если неправильно указать пароль
    public void wrongCourierPasswordTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью, запрос вернул id
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

        //  меняем пароль курьера на несуществующий
        loginRequest.setPassword(new StringBuilder(loginRequest.getPassword()).reverse().toString());

        //  запрос с несуществующим паролем возвращяет ошибку
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check courier can't log in with empty login")
    //    для авторизации нужно передать все обязательные поля;
    //    если логина нет, запрос возвращает ошибку;
    public void emptyCourierLoginTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью, запрос вернул id
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

        //  очищаем логин курьера
        loginRequest.setLogin(null);

        //  запрос с пустым логином возвращяет ошибку
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Check courier can't log in with empty password")
    //    для авторизации нужно передать все обязательные поля;
    //    если пароля нет, запрос возвращает ошибку;
    public void emptyCourierPasswordTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью, запрос вернул id
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

        //  очищаем пароль курьера
        loginRequest.setPassword(null);

        //  запрос с пустым паролем возвращяет ошибку
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }


}
