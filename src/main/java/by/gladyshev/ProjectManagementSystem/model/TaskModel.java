package by.gladyshev.ProjectManagementSystem.model;

import javax.validation.constraints.Size;

public class TaskModel implements MyModel{
    private int id;
    @Size(min = 1, message = "name cannot be empty")
    private String name;
    private String shortName;
    private ProjectModel pm;
    private UserModel responsible=new UserModel(0, "");
    public TaskModel() {
    }
    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
        setShortName();
    }
    public void setByShortName(String name)
    {
        this.name = pm.getName()+"|"+name;
        setShortName();
    }
    public UserModel getResponsible() {
        return responsible;
    }

    public void setResponsible(UserModel responsible) {
        this.responsible = responsible;
    }

    public String getShortName() {
        return shortName;
    }

    private void setShortName() {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < name.length() ; i++) {
            if(flag)
            {
                sb.append(name.charAt(i));
            }
            else{
                if(name.charAt(i)=='|')
                {
                    flag=true;
                }
            }
            shortName = sb.toString();
        }
    }

    public ProjectModel getPm() {
        return pm;
    }

    public void setPm(ProjectModel pm) {
        this.pm = pm;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}
