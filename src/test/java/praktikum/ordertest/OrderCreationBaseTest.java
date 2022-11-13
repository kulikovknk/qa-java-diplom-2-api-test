package praktikum.ordertest;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

public class OrderCreationBaseTest extends OrderBaseTest {

    @Test
    @DisplayName("Check creation of an order with authorization")
    // создание заказа с авторизацией и ингредиентами
    public void createOrderAuthorizedUserTest() {

        checkExistingUserLogIn();
        checkOrderCreationForAuthorizedUser();

    }

    @Test
    @DisplayName("Check creation of an order without authorization")
    // создание заказа без авторизации
    public void createOrderNotAuthorizedUserTest() {

        checkOrderCreationForNotAuthorizedUser();

    }

    @Test
    @DisplayName("Check creation of an order with no ingredients")
    // создание заказа с авторизацией без ингредиентов
    public void createOrderAuthorizedUserNoIngredientsTest() {

        checkExistingUserLogIn();
        checkOrderCreationWithNoIngredients();

    }

    @Test
    @DisplayName("Check creation of an order with invalid ingredients hash")
    // создание заказа с авторизацией с неверным хешем ингредиентов
    public void createOrderAuthorizedUserInvalidIngredientsHashTest() {

        checkExistingUserLogIn();
        checkOrderCreationWithInvalidIngredientsHash();

    }
}
