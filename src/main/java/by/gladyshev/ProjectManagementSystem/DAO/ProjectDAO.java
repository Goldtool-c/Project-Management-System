package by.gladyshev.ProjectManagementSystem.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProjectDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public ProjectDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
