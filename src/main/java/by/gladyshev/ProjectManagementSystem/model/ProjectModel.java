package by.gladyshev.ProjectManagementSystem.model;

import by.gladyshev.ProjectManagementSystem.repository.Criteria;
import by.gladyshev.ProjectManagementSystem.repository.Search;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ProjectModel implements Model {
    private int id;
    @NotEmpty(message = "Project name can not be empty")
    @Size(min = 1, max = 150, message = "Name length should be more than 1 and less than 150 symbols")
    private String name;
    private List<UserModel> developers = new ArrayList<>();
    //private List<Task> tasks;


    public ProjectModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProjectModel(int id, String name, List<UserModel> developers) {
        this.id = id;
        this.name = name;
        this.developers = developers;
    }
    public ProjectModel() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserModel> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<UserModel> developers) {
        this.developers = developers;
    }

    @Override
    public String toString() {
        return "ProjectModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void assignDeveloper(String name) throws IllegalAccessException {
        if(name!=null) {
            UserModel dev = (UserModel) Search.search(new Criteria("name", name), UserRepository.INSTANCE);
            if(duplicateValid(dev)) {developers.add(dev);}
            // else todo exception
        }//todo exception
    }
    public void assignDeveloper(UserModel um)
    {
        if(duplicateValid(um)) {developers.add(um);}
    }
    private boolean duplicateValid(UserModel um)
    {
        for (int i = 0; i < developers.size(); i++) {
            if(developers.get(i).equals(um))
            {
                return false;
            }
        }
        return true;
    }
}
