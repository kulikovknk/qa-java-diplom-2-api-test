package praktikum.usertest;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

public class UserLoginBaseTest extends UserBaseTest {

    @Test
    @DisplayName("Check existing user can log in")
    //    логин под существующим пользователем
    public void logInExistingUserTest() {
        checkUserCreation();
        checkUserLogIn();
    }

    @Test
    @DisplayName("Check user can't log in with invalid email")
    //    логин с неверным email
    public void logInWithUserInvalidEmailTest() {

        checkUserCreation();
        checkUserLogIn();
        logoutUser();

        checkUserWithInvalidEmailLogIn();

    }

    @Test
    @DisplayName("Check user can't log in with invalid password")
    //    логин с неверным password
    public void logInWithUserInvalidPasswordTest() {

        checkUserCreation();
        checkUserLogIn();
        logoutUser();

        checkUserWithInvalidPasswordLogIn();

    }

}
