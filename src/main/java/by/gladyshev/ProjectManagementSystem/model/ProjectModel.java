package by.gladyshev.ProjectManagementSystem.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ProjectModel {
    private int id;
    @NotEmpty(message = "Project name can not be empty")
    @Size(min = 1, max = 150, message = "Name length should be more than 1 and less than 150 symbols")
    private String name;
    private
}
