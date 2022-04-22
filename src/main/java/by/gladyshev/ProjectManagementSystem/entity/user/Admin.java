package by.gladyshev.ProjectManagementSystem.entity.user;

public class Admin extends User {
    private final UserRole Role = UserRole.ADMIN;
    public Admin(String name, int password) {
        this.name = name;
        this.password = password;
    }
}
