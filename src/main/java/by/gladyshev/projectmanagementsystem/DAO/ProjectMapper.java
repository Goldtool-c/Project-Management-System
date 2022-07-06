package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectMapper implements RowMapper<MyModel> {
    private UserDAO userDAO;

    public ProjectMapper(@Autowired UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public ProjectModel mapRow(ResultSet resultSet, int i) throws SQLException {
        ProjectModel pm = new ProjectModel();
        pm.setFlag(resultSet.getBoolean("flag"));
        pm.setId(resultSet.getInt("id"));
        pm.setName(resultSet.getString("name"));
        List<String> devs = parseName(resultSet.getString("developers"));
        List<UserModel> uml = new ArrayList<>();
        for (int j = 0; j < devs.size(); j++) {
            uml.add((UserModel) userDAO.searchByName(devs.get(j)));
        }
        for (int j = 0; j < devs.size(); j++) {
            pm.assignDeveloper(uml.get(j));
        }
        List<String> tasks = parseName(resultSet.getString("tasks"));
        List<TaskModel> tml = new ArrayList<>();
        for (int j = 0; j < tasks.size(); j++) {
            //tml.add((TaskModel) taskDAO.searchByName(tasks.get(j)));
            TaskModel temp = new TaskModel();
            temp.setName(tasks.get(j));
            tml.add(temp);
        }
        for (int j = 0; j < tasks.size(); j++) {
            pm.addTask(tml.get(j));
        }
        return pm;
    }

    private List<String> parseName(String name) {
        List<String> devs = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (name != null) {
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
}
