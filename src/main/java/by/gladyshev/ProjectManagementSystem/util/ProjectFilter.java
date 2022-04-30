package by.gladyshev.ProjectManagementSystem.util;

import by.gladyshev.ProjectManagementSystem.entity.Project;
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
        System.out.println("Вызываем проектный фильтр");
        res = projectNameFilter(res, filter);
        System.out.println("Он кончил");
        return res;
    }
    private static List<MyModel> projectNameFilter(List<MyModel> toFilter, ProjectFilter filter)
    {
        List<MyModel> res = new ArrayList<>();
        ProjectModel temp;
        System.out.println("Залетаем в цикл");
        System.out.println(toFilter.size());
        for (MyModel myModel : toFilter) {
            temp = (ProjectModel) myModel;
            System.out.println("temp: "+temp.getName());
            System.out.println("filter: "+filter.getProjectName());
            System.out.println(temp.getName().contains(filter.getProjectName()));
            System.out.println("______________");
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
