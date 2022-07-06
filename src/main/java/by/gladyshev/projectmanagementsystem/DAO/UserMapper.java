package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class UserMapper implements RowMapper<MyModel> {
    @Override
    public UserModel mapRow(ResultSet resultSet, int i) throws SQLException {
        UserModel um = new UserModel();
        um.setFlag(resultSet.getBoolean("flag"));
        um.setId(resultSet.getInt("id"));
        um.setName(resultSet.getString("name"));
        um.setPassword(resultSet.getInt("password"));
        um.setRole(resultSet.getString("role"));
        return um;
    }

}
