package by.gladyshev.ProjectManagementSystem.util;

import by.gladyshev.ProjectManagementSystem.model.UserModel;

public class ActiveUser {
    private static UserModel activeUser = new UserModel();
    static {
        activeUser.setRole("developer");
    }
    private ActiveUser() {
    }

    public static UserModel getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(UserModel activeUser) {
        ActiveUser.activeUser = activeUser;
    }
}
