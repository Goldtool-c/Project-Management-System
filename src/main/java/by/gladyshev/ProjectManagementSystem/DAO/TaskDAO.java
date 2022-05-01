package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.model.MyModel;
import by.gladyshev.ProjectManagementSystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskDAO extends DAO {
    @Autowired
    public TaskDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        table = "tasks";
        rm = new TaskMapper();
        if(jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class)!=null)
        {
            ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class);
        } else
        {
            ID=0;
        }
        repository = TaskRepository.INSTANCE;
        repository.setAll(index("id"));
    }

    public int getID() {
        return this.ID;
    }
}
