package tests;

import fixture.ConfigProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

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
    @MethodSource("userDataProvider")
    @DisplayName("Тест тест логина")
    public void canLoginWithValidData(String login, String password) {
        app.session().login(login, password);
        assertArrayEquals(new String[]{"Добро пожаловать", "Выйти"}, app.session().getConfirmLoginMessage());
        app.session().logout();
        assertEquals("Войти", app.session().getConfirmLogoutMessage());
    }

    /**
     * фабричная функция для предоставления данных в параметризированный тест логина
     */
    static Stream<Arguments> userDataProvider() {
        return Stream.of(
                Arguments.arguments(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword()),
                Arguments.arguments(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword()),
                Arguments.arguments(ConfigProvider.getAdminEfimovLogin(), ConfigProvider.getAdminEfimovPassword())
        );
    }


}
