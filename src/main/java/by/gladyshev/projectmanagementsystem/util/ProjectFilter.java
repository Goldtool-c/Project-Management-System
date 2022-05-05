package by.gladyshev.projectmanagementsystem.util;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.repository.Criteria;
import by.gladyshev.projectmanagementsystem.repository.ProjectRepository;
import by.gladyshev.projectmanagementsystem.repository.Search;
import by.gladyshev.projectmanagementsystem.repository.UserRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ProjectFilter {
    @NotEmpty(message = "it should not be empty")
    @Size(min = 1, message = "it should not be empty")
    private String userName;
    private String projectName;
    public ProjectFilter() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public static List<MyModel> filter(ProjectFilter filter)
    {
        List<MyModel> res = new ArrayList<>();
        if(filter.getUserName().length()==0)
        {
            res = ProjectRepository.INSTANCE.getAll();
        }
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
        res = projectNameFilter(res, filter);
        return res;
    }
    private static List<MyModel> projectNameFilter(List<MyModel> toFilter, ProjectFilter filter)
    {
        List<MyModel> res = new ArrayList<>();
        ProjectModel temp;
        for (MyModel myModel : toFilter) {
            temp = (ProjectModel) myModel;
            if (temp.getName().contains(filter.getProjectName())) {
                res.add(temp);
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "ProjectFilter{" +
                "userName='" + userName + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
