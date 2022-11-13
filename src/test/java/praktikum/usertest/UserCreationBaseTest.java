package praktikum.usertest;

import config.UserAttributeType;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

public class UserCreationBaseTest extends UserBaseTest {

    @Test
    @DisplayName("Check a unique user can be created")
    //    тест на создание уникального пользователя
    public void createUniqueUserTest() {

        checkUserCreation();
        checkUserLogIn();

    }


    @Test
    @DisplayName("Check creating a user who is already registered")
    //    тест на создание пользователя, который уже зарегистрирован
    public void createAlreadyRegisteredUserTest() {

        checkUserDuplicateCreation();

    }

    @Test
    @DisplayName("Check user is not created with the empty name")
    //    создать пользователя и не заполнить одно из обязательных полей (name)
    public void emptyUserNameTest() {

        checkUserWithEmptyAttributeCreation(UserAttributeType.NAME);

    }

    @Test
    @DisplayName("Check user is not created with the empty password")
    //    создать пользователя и не заполнить одно из обязательных полей (password)
    public void emptyUserPasswordTest() {

        checkUserWithEmptyAttributeCreation(UserAttributeType.PASSWORD);

    }

    @Test
    @DisplayName("Check user is not created with the empty email")
    //    создать пользователя и не заполнить одно из обязательных полей (email)
    public void emptyUserEmailTest() {

        checkUserWithEmptyAttributeCreation(UserAttributeType.EMAIL);

    }

}
