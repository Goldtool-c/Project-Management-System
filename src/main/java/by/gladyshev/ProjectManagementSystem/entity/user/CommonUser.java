package by.gladyshev.ProjectManagementSystem.entity.user;

public class CommonUser extends User {
    private final UserRole Role = UserRole.COMMON;
    public CommonUser(String name, int password) {
        this.name = name;
        this.password = password;
    }

}
