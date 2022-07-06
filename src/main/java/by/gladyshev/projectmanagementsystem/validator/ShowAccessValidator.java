package by.gladyshev.projectmanagementsystem.validator;

import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.util.ActiveUser;

import java.util.List;

public class ShowAccessValidator {
    private static ShowAccessValidator instance;

    private ShowAccessValidator() {
    }

    public static ShowAccessValidator getInstance() {
        if (instance == null) {
            instance = new ShowAccessValidator();
        }
        return instance;
    }

    public boolean showValid(ProjectModel pm) {
        if (ActiveUser.getActiveUser().getRole().equals("admin")) {
            return true;
        }
        List<UserModel> users = pm.getDevelopers();
        for (UserModel user : users) {
            if (user.equals(ActiveUser.getActiveUser())) {
                return true;
            }
        }
        return false;
    }

    public boolean showValid(UserModel userModel) {
        return userModel.equals(ActiveUser.getActiveUser())
                || ActiveUser.getActiveUser().getRole().equals("admin");
    }
}
