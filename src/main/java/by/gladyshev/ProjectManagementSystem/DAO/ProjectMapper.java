package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectMapper implements RowMapper<Model> {
    @Override
    public ProjectModel mapRow(ResultSet resultSet, int i) throws SQLException {
        ProjectModel pm = new ProjectModel();
        pm.setId(resultSet.getInt("id"));
        pm.setName(resultSet.getString("name"));
        //pm.setDevelopers(resultSet.getString(""));
        pm.assignDeveloper(resultSet.getString("developers"));
        return pm;
    }
}
