package generator;

import dto.UserRequest;
import org.apache.commons.lang3.RandomStringUtils;

public class UserRequestGenerator {

    public static UserRequest getNewUserRequest() {

        String userName = RandomStringUtils.randomAlphabetic(7);

        UserRequest userRequest = new UserRequest();

        userRequest.setName(userName);
        userRequest.setPassword(RandomStringUtils.randomAlphabetic(10));
        userRequest.setEmail(String.format("%s@mail.ru", userName.toLowerCase()));

        return userRequest;

    }

    public static UserRequest getExistingUserRequest() {

        UserRequest userRequest = new UserRequest();
        userRequest.setName("hgfJGjJHGJ");
        userRequest.setPassword("HGjjGGfjtFJGFJGf");
        userRequest.setEmail("hgfJGjJHGJ@mail.ru");

        return userRequest;

    }

}
