package by.gladyshev.ProjectManagementSystem.validator;

import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.util.ActiveUser;

import java.util.List;

public class ProjectAccessValidator {
    private static ProjectAccessValidator instance;
    private ProjectAccessValidator(){}
    public static ProjectAccessValidator getInstance() {
        if(instance == null){		//если объект еще не создан
            instance = new ProjectAccessValidator();	//создать новый объект
        }
        return instance;
    }
    public boolean showValid(ProjectModel pm)
    {
        List<UserModel> users = pm.getDevelopers();
        for (UserModel user : users) {
            if (user.equals(ActiveUser.getActiveUser())) {
                return true;
            }
        }
        return false;
    }
}
