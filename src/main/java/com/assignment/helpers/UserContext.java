package com.assignment.helpers;

import com.assignment.entity.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private static final ThreadLocal<User> APP_USER = new ThreadLocal<>();
    public static void setLoggedInUser(User loggedInUser) {
        APP_USER.set(loggedInUser);
    }
    public static User getLoggedInUser() { return APP_USER.get(); }

}
