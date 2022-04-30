package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.model.MyModel;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
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
        UserRepository.INSTANCE.setAll((List<MyModel>) this.index("id"));
    }
    @Override
    public void save(MyModel pm)
    {
        ID++;
        Field[] fields = pm.getClass().getDeclaredFields();
        Object[] values = new Object[fields.length];
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                values[i] = fields[i].get(pm);
            }
            jdbcTemplate.update("INSERT INTO "+table+" values(?, ?, ?, ?)", ID, values[1], values[2], "developer");//[1] -- name
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        pm.setId(ID);
        repository.add(pm);
    }
}
