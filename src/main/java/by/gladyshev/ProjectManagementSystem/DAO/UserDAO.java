package by.gladyshev.ProjectManagementSystem.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component()
public class UserDAO extends DAO {
    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        table = "userDB";
        rm = new UserMapper();
        ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class);
    }
}
