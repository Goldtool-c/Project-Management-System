package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectDAO extends DAO {
    public ProjectDAO(@Autowired JdbcTemplate jdbcTemplate, @Autowired ProjectMapper projectMapper) {
        super(jdbcTemplate);
        table = "project";
        rm = projectMapper;
        if (jdbcTemplate.queryForObject("SELECT MAX(id) FROM " + table, Integer.class) != null) {
            ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM " + table, Integer.class);
        } else {
            ID = 0;
        }
        size = jdbcTemplate.queryForObject("SELECT count (*) FROM "+ table, Integer.class);
    }

    @Override
    public void update(MyModel pm) {
        if (((ProjectModel) pm).getDevelopers().size() != 0) {
            String users = parseName(((ProjectModel) pm).getUserNames());
            if (((ProjectModel) pm).getTasks().size() != 0) {
                String tasks = parseName(((ProjectModel) pm).getTaskNames());
                jdbcTemplate.update("UPDATE " + table + " SET name=?, developers=?, tasks=? WHERE id=?",
                        pm.getName(), users, tasks, pm.getId());
            } else {
                jdbcTemplate.update("UPDATE " + table + " SET name=?, developers=?, tasks=null WHERE id=?",
                        pm.getName(), users, pm.getId());
            }
        } else {
            if (((ProjectModel) pm).getTasks().size() != 0) {
                String tasks = parseName(((ProjectModel) pm).getTaskNames());
                jdbcTemplate.update("UPDATE " + table + " SET name=?, tasks=? WHERE id=?",
                        pm.getName(), tasks, pm.getId());
            } else {
                jdbcTemplate.update("UPDATE " + table + " SET name=?, tasks=null WHERE id=?",
                        pm.getName(), pm.getId());
            }
        }
    }

    private String parseName(String[] names)//format "name1,name2,name3,...,nameN"
    {
        StringBuilder sb = new StringBuilder();
        if (names.length != 0) {
            sb.append(names[0]);
            for (int i = 1; i < names.length; i++) {
                sb.append(",");
                sb.append(names[i]);
            }
        }
        return sb.toString();
    }

}
