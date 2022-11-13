package praktikum.ordertest;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

public class OrderListTest extends OrderBaseTest {

    @Test
    @DisplayName("Check get order list of an authorized user")
    // Получение заказов авторизованного пользователя
    public void getOrderListAuthorizedUserTest() {

        checkExistingUserLogIn();
        checkGetOrderListAuthorizedUser();

    }

    @Test
    @DisplayName("Check get order list of not an authorized user")
    // Получение заказов авторизованного пользователя
    public void getOrderListNotAuthorizedUserTest() {

        checkGetOrderListNotAuthorizedUser();

    }

}
