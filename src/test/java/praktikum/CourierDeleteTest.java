import io.qameta.allure.junit4.DisplayName;

import org.junit.Before;
import org.junit.Test;
import client.CourierClient;
import dto.CourierRequest;
import dto.DeleteRequest;
import dto.LoginRequest;
import generator.LoginRequestGenerator;

import java.util.Random;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static generator.CourierRequestGenerator.getRandomCourierRequest;

public class CourierDeleteTest {

    private CourierClient courierClient;
    private CourierRequest courierRequest;
    private LoginRequest loginRequest;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Check new courier can be deleted")
     //    успешный запрос на удаление возвращает ok: true;
    public void newCourierIsDeletedTest() {

        courierRequest = getRandomCourierRequest();

        courierClient.createCourier(courierRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));

        loginRequest = LoginRequestGenerator.from(courierRequest);

        //  успешно зашли под созданной учетной записью
        //  получим id для удаления созданной учетной записи
        Integer id = courierClient.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
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
    @DisplayName("Check delete request with no id")
    //    если отправить запрос без id, вернётся ошибка;
    public void checkDeleteRequestWithNoIdTest() {

        DeleteRequest deleteRequest = new DeleteRequest(null);

        courierClient.deleteCourier(deleteRequest)
                    .assertThat()
                    .statusCode(SC_BAD_REQUEST)
                    .and()
                    .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Check delete request with non-existed id")
    //    если отправить запрос с несуществующим id, вернётся ошибка.
    public void checkDeleteRequestWithNonExistedIdTest() {

        Random random = new Random();

        DeleteRequest deleteRequest = new DeleteRequest(random.nextInt(1000000));

        courierClient.deleteCourier(deleteRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id нет."));
    }
}
