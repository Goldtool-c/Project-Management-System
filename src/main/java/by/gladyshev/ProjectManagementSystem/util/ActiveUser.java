package by.gladyshev.ProjectManagementSystem.util;

import by.gladyshev.ProjectManagementSystem.model.UserModel;

public class ActiveUser {
    private static UserModel activeUser;

    private ActiveUser() {
    }

    public static UserModel getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(UserModel activeUser) {
        ActiveUser.activeUser = activeUser;
    }
}
