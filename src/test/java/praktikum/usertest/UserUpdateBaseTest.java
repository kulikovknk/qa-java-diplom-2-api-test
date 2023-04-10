package praktikum.usertest;

import config.UserAttributeType;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

public class UserUpdateBaseTest extends UserBaseTest {

    // тесты на изменение данных авторизованного пользователя

    @Test
    @DisplayName("Check user name change")
    // изменение поля name
    public void userNameChangeTest() {

        checkUserCreation();
        checkUserLogIn();
        checkUserUpdate(UserAttributeType.NAME);

    }

    @Test
    @DisplayName("Check user password change")
    // изменение поля password
    public void userPasswordChangeTest() {

        checkUserCreation();
        checkUserLogIn();
        checkUserUpdate(UserAttributeType.PASSWORD);

    }

    @Test
    @DisplayName("Check user email change")
    // изменение поля email
    public void userEmailChangeTest() {

        checkUserCreation();
        checkUserLogIn();
        checkUserUpdate(UserAttributeType.EMAIL);

    }

    // тесты на изменение данных неавторизованного пользователя
    // во всех случаях должен вернуться код ответа 401 Unauthorized
    // изменение данных не выполнится

    @Test
    @DisplayName("Check not authorized user name change")
    // изменение поля name
    public void notAuthorizedUserNameChangeTest() {

        checkUserCreation();
        checkUserLogIn();
        checkNotAuthorizedUserUpdate(UserAttributeType.NAME);

    }

    @Test
    @DisplayName("Check not authorized user password change")
    // изменение поля password
    public void notAuthorizedUserPasswordChangeTest() {

        checkUserCreation();
        checkUserLogIn();
        checkNotAuthorizedUserUpdate(UserAttributeType.PASSWORD);

    }

    @Test
    @DisplayName("Check not authorized user email change")
    // изменение поля email
    public void notAuthorizedUserEmailChangeTest() {

        checkUserCreation();
        checkUserLogIn();
        checkNotAuthorizedUserUpdate(UserAttributeType.EMAIL);

    }

}
