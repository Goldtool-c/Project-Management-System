package by.gladyshev.ProjectManagementSystem.model;

public class TaskModel implements MyModel{
    private int id;
    private String name;
    private String shortName;
    private ProjectModel pm;
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
