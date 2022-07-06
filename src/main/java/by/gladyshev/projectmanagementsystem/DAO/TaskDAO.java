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
public class TaskDAO extends DAO {
    public TaskDAO(@Autowired JdbcTemplate jdbcTemplate, @Autowired TaskMapper taskMapper) {
        super(jdbcTemplate);
        table = "tasks";
        rm = taskMapper;
        size = jdbcTemplate.queryForObject("SELECT count (*) FROM "+ table, Integer.class);
        if (jdbcTemplate.queryForObject("SELECT MAX(id) FROM " + table, Integer.class) != null) {
            ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM " + table, Integer.class);
        } else {
            ID = 0;
        }
    }

    @Override
    public void update(MyModel tm) {
        if (((TaskModel) tm).getResponsible() != null) {
            jdbcTemplate.update("UPDATE " + table + " SET name=?, developer=? WHERE id=?",
                    tm.getName(), ((TaskModel) tm).getResponsible().getId(), tm.getId());
        } else {
            jdbcTemplate.update("UPDATE " + table + " SET name=? WHERE id=?",
                    tm.getName(), tm.getId());
        }
    }

    public int getID() {
        return this.ID;
    }

    public void decentTaskInitialization(ProjectModel pm)
    {
        List<TaskModel> tms = new ArrayList<>();
        for (int i = 0; i < pm.getTasks().size(); i++) {
            tms.add((TaskModel) searchByName(pm.getTasks().get(i).getName()));
        }
        if(tms.size()!=0) { pm.reassignTasks(tms); }
    }
}
