package by.gladyshev.ProjectManagementSystem.util;

import by.gladyshev.ProjectManagementSystem.model.MyModel;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.Criteria;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import by.gladyshev.ProjectManagementSystem.repository.Search;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ProjectFilter {
    @NotEmpty(message = "it should not be empty")
    @Size(min = 1, message = "it should not be empty")
    private String userName;
    public ProjectFilter() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public static List<MyModel> UserFilter(ProjectFilter filter)
    {

        List<MyModel> res = new ArrayList<>();
        ProjectModel tempPM;
        UserModel goal = null;
        try {
            goal = (UserModel) Search.search(new Criteria("name", filter.getUserName()), UserRepository.INSTANCE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(goal!=null) {
            for (int i = 0; i < ProjectRepository.INSTANCE.Size(); i++) {
                tempPM = (ProjectModel) ProjectRepository.INSTANCE.get(i);
                for (int j = 0; j < tempPM.getDevelopers().size(); j++) {
                    UserModel temp = tempPM.getDevelopers().get(j);
                    if (temp.equals(goal)) {
                        res.add(tempPM);
                        break;
                    }
                }
            }
        }
        // res = anotherFilter(res, ...params)
        return res;
    }
}
