package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.entity.user.User;
import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.repository.ProjectRepository;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO extends DAO {
    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        table = "userDB";
        rm = new UserMapper();
        ID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM "+table, Integer.class);
        repository = UserRepository.INSTANCE;
        UserRepository.INSTANCE.setAll((List<Model>) this.index());
    }
}
