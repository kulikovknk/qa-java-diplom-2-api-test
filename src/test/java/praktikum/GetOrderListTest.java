import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import client.OrderClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Check order list can be received")
    //    в тело ответа возвращается список заказов
    public void getOrderListTest() {

//        orderClient.getOrderList()
//                .assertThat()
//                .statusCode(SC_OK)
//                .and()
//                .body("orders", notNullValue());

    }

}
