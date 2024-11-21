package tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс содержит тесты авторизации
 */
public class LoginTests extends TestBase {


    /**
     * Параметризированный тест авторизации зарегистрированных пользователей.
     * Проверяет успешный logIN и logOUT
     */
    @ParameterizedTest
    @CsvSource(value = {
            "ProkoshevPV, qweR2304",
            "EfimovSN, beatlestest",
            "ProkoshevPV1, 0j2Z7O8G"
    })
    public void canLoginWithValidData(String login, String password) {
        app.session().login(login, password);
        assertArrayEquals(new String[]{"Добро пожаловать", "Выйти"}, app.session().getConfirmLoginMessage());
        app.session().logout();
        assertEquals("Войти", app.session().getConfirmLogoutMessage());
    }



}
