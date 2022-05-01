package by.gladyshev.ProjectManagementSystem.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskDAO extends DAO {
    @Autowired
    public TaskDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        table = "tasks";
    }
}
