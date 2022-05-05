package by.gladyshev.projectmanagementsystem.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginFormTest {

    @Test
    void getLogin() {
        LoginForm lf = new LoginForm();
        lf.setLogin("login");
        Assert.assertEquals("login", lf.getLogin());
    }

    @Test
    void getPassword() {
        LoginForm lf = new LoginForm();
        lf.setPassword("password");
        Assert.assertEquals("password", lf.getPassword());
    }
}