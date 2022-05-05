package by.gladyshev.projectmanagementsystem.validator;

import by.gladyshev.projectmanagementsystem.model.UserModel;
import by.gladyshev.projectmanagementsystem.util.ActiveUser;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ShowAccessValidatorTest {
    UserModel um = Mockito.mock(UserModel.class);
    ActiveUser activeUser = Mockito.mock(ActiveUser.class);

    @Test
    void showValid() {//same user
        UserModel test = new UserModel(1, "denis");
        try (MockedStatic<ActiveUser> activeUser = Mockito.mockStatic(ActiveUser.class)) {
            activeUser.when(ActiveUser::getActiveUser).thenReturn(new UserModel(1, "denis"));
            assertTrue(ShowAccessValidator.getInstance().showValid(test));
        }
    }
}