package by.gladyshev.projectmanagementsystem.DAO;

import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.repository.UserRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<MyModel> {
    @Override
    public UserModel mapRow(ResultSet resultSet, int i) throws SQLException {
        if(resultSet.getString("flag").equals("true")) {
            UserModel um = new UserModel();
            um.setId(resultSet.getInt("id"));
            um.setName(resultSet.getString("name"));
            um.setPassword(resultSet.getInt("password"));
            um.setRole(resultSet.getString("role"));
            if (UserRepository.INSTANCE.Size() != 0) {
                repositoryUpdate(um);
            }
            return um;
        } else
        {
            return null;
        }
    }
    private void repositoryUpdate(MyModel pm) {
        for (int i = 0; i < UserRepository.INSTANCE.Size(); i++) {
            if(UserRepository.INSTANCE.get(i).equals(pm))
            {
                UserRepository.INSTANCE.getAll().set(i, pm);
            }
        }
    }
}
