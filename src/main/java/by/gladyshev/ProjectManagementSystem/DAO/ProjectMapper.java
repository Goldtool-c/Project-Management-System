package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.entity.Task;
import by.gladyshev.ProjectManagementSystem.model.MyModel;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.TaskModel;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import by.gladyshev.ProjectManagementSystem.repository.TaskRepository;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectMapper implements RowMapper<MyModel> {
    @Override
    public ProjectModel mapRow(ResultSet resultSet, int i) throws SQLException {
        ProjectModel pm = new ProjectModel();
        try {
            pm.setId(resultSet.getInt("id"));
            pm.setName(resultSet.getString("name"));
            if(UserRepository.INSTANCE.Size()!=0) {
                List<String> devs = parseName(resultSet.getString("developers"));
                for (int j = 0; j < devs.size(); j++) {
                    pm.assignDeveloper(devs.get(j));
                }
                repositoryUpdate(pm);
            }
            if(TaskRepository.INSTANCE.Size()!=0) {
                List<String> tasks = parseName(resultSet.getString("tasks"));
                for (int j = 0; j < tasks.size(); j++) {
                    pm.addTask(tasks.get(j));
                }
                System.out.println("начинаем обнолять репозиторий");
                repositoryUpdate(pm);
                System.out.println("заканчиваем");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return pm;
    }
    private List<String> parseName(String name)
    {
        List<String> devs = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(name!=null) {
            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) != ',') {
                    sb.append(name.charAt(i));
                } else {
                    devs.add(sb.toString());
                    sb = new StringBuilder();
                }
            }
            devs.add(sb.toString());
        }
        return devs;
    }
    private void repositoryUpdate(MyModel pm) {
        for (int i = 0; i < ProjectRepository.INSTANCE.Size(); i++) {
            if(ProjectRepository.INSTANCE.get(i).equals(pm))
            {
                System.out.println("обновили");
                ProjectRepository.INSTANCE.getAll().set(i, pm);
            }
        }
    }
}
