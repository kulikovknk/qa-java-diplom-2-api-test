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


public class CreateCourierTest {

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
    @DisplayName("Check new courier can be created")
    //    курьера можно создать
    //    запрос возвращает правильный код ответа;
    //    успешный запрос возвращает ok: true;
    public void newCourierIsCreatedTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

     }

    @Test
    @DisplayName("Check two identical couriers can't be created")
    //    нельзя создать двух одинаковых курьеров;
    public void twoIdenticalCouriersTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

        // нельзя создать второго курьера с такими же учетными данными
        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Check new courier with existed login can't be created")
    //    если создать пользователя с логином, который уже есть, возвращается ошибка
    public void identicalCourierLoginTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью
        courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());

        // меняем пароль
        courierRequest.setPassword(new StringBuilder(courierRequest.getPassword()).reverse().toString());

        // если создать пользователя с логином, который уже есть, возвращается ошибка
        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Check mandatory parameters should be filled in for courier creation")
    //    чтобы создать курьера, нужно передать в ручку все обязательные поля;
    public void mandatoryFieldsFilledTest() {

        courierRequest = getRandomCourierRequest();

        // очистим поле FirstName (необязательное)
        courierRequest.setFirstName(null);

        // курьер с заполненными обязательными полями создается успешно
        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Check courier can't be created with the empty login")
    // создание курьера с незаполненным логином падает
    public void loginNotFilledTest() {

        courierRequest = getRandomCourierRequest();

        courierRequest.setLogin(null);

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Check courier can't be created with the empty password")
    // создание курьера с незаполненным паролем падает
    public void passwordNotFilledTest() {

        courierRequest = getRandomCourierRequest();

        courierRequest.setPassword(null);

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}
