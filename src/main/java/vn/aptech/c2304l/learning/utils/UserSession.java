package vn.aptech.c2304l.learning.utils;

import vn.aptech.c2304l.learning.model.User;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;

    private UserSession() { }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}

