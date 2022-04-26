package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectMapper implements RowMapper<Model> {
    @Override
    public ProjectModel mapRow(ResultSet resultSet, int i) throws SQLException {
        ProjectModel pm = new ProjectModel();
        try {
            pm.setId(resultSet.getInt("id"));
            pm.setName(resultSet.getString("name"));
            if(UserRepository.INSTANCE.Size()!=0) {
                pm.assignDeveloper(resultSet.getString("developers"));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return pm;
    }
}
