package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.entity.user.User;
import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<Model> {
    @Override
    public UserModel mapRow(ResultSet resultSet, int i) throws SQLException {
        UserModel um = new UserModel();
        um.setId(resultSet.getInt("id"));
        um.setName(resultSet.getString("name"));
        return um;
    }
}
