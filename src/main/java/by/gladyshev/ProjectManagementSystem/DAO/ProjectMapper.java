package by.gladyshev.ProjectManagementSystem.DAO;

import by.gladyshev.ProjectManagementSystem.model.Model;
import by.gladyshev.ProjectManagementSystem.model.ProjectModel;
import by.gladyshev.ProjectManagementSystem.model.UserModel;
import by.gladyshev.ProjectManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            //pm.setDevelopers(resultSet.getString(""));
            System.out.println(pm.getId()+pm.getName());
            System.out.println(resultSet.getString("developers"));
            System.out.println(UserRepository.INSTANCE.Size());
            System.out.println("____________");
            if(UserRepository.INSTANCE.Size()!=0) {
                pm.assignDeveloper(resultSet.getString("developers"));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //System.out.println(resultSet.getInt("id")+resultSet.getString("name")+resultSet.getString("developers"));
        return pm;
    }
}
