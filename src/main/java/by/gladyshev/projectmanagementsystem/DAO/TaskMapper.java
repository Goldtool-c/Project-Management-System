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
public class TaskMapper implements RowMapper<MyModel> {
    private UserDAO userDAO;
    private JdbcTemplate jdbcTemplate;
    private ProjectMapper pm;
    public TaskMapper(@Autowired JdbcTemplate jdbcTemplate, @Autowired UserDAO userDAO, @Autowired ProjectMapper pm) {
        this.userDAO = userDAO;
        this.jdbcTemplate = jdbcTemplate;
        this.pm = pm;
    }

    @Override
    public MyModel mapRow(ResultSet resultSet, int i) throws SQLException {

        TaskModel tm = new TaskModel();
        tm.setFlag(resultSet.getBoolean("flag"));
        tm.setId(resultSet.getInt("id"));
        tm.setName(resultSet.getString("name"));
        StringBuilder sb = new StringBuilder();
        //get ProjectName from taskName, then find project in repository and add it to this task
        for (int j = 0; j < tm.getName().length(); j++) {
            if (tm.getName().charAt(j) != '|') {
                sb.append(tm.getName().charAt(j));
            } else {
                break;
            }
        }
        ProjectModel pm = (ProjectModel) searchByName(sb.toString());
        tm.setPm(pm);
        //assigned developer in tasksTable defines by theirs id
        if (resultSet.getInt("developer") != 0) {
            UserModel um = (UserModel) userDAO.show(resultSet.getInt("developer"));
            tm.setResponsible(um);
            assert um != null;
            if (!isAssigned(um, tm)) {
                um.assignTask(tm);
            }
        }
        return tm;
    }

    private boolean isAssigned(UserModel um, TaskModel tm) {
        for (int i = 0; i < um.getTasks().size(); i++) {
            if (um.getTasks().get(i).getId() == tm.getId()) {
                return true;
            }
        }
        return false;
    }


    private MyModel searchByName(String name)
    {
        return jdbcTemplate.query("SELECT * FROM project WHERE name =?", new Object[]{name}, pm)
                .stream().findAny().orElse(null);
    }
}
