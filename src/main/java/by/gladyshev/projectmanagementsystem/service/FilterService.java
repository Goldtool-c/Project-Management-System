package by.gladyshev.projectmanagementsystem.service;

import by.gladyshev.projectmanagementsystem.DAO.ProjectDAO;
import by.gladyshev.projectmanagementsystem.DAO.UserDAO;
import by.gladyshev.projectmanagementsystem.model.MyModel;
import by.gladyshev.projectmanagementsystem.model.ProjectModel;
import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.util.ProjectFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterService {//move to DAO as query
    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;
    public List<MyModel> filter(ProjectFilter filter) {
        List<MyModel> res = new ArrayList<>();
        if (filter.getUserName().length() == 0) {
            res = projectDAO.index("id");
        }
        ProjectModel tempPM;
        UserModel goal = (UserModel) userDAO.searchByName(filter.getUserName());
        //Search.search(new Criteria("name", filter.getUserName()), UserRepository.INSTANCE);
        if (goal != null) {
            for (int i = 0; i <= projectDAO.getID(); i++) {
                tempPM = (ProjectModel) projectDAO.show(i);
                if(tempPM!=null) {
                    for (int j = 0; j < tempPM.getDevelopers().size(); j++) {
                        UserModel temp = tempPM.getDevelopers().get(j);
                        if (temp.equals(goal)) {
                            res.add(tempPM);
                            break;
                        }
                    }
                }
            }
        }
        res = projectNameFilter(res, filter);
        return res;
    }

    private List<MyModel> projectNameFilter(List<MyModel> toFilter, ProjectFilter filter) {
        List<MyModel> res = new ArrayList<>();
        ProjectModel temp;
        for (MyModel myModel : toFilter) {
            temp = (ProjectModel) myModel;
            if (temp.getName().contains(filter.getProjectName())) {
                res.add(temp);
            }
        }
        return res;
    }
}
