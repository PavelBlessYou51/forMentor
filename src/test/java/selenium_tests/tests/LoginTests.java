package selenium_tests.tests;

import fixture.ConfigProvider;
import io.qameta.allure.*;
import io.qameta.allure.model.Parameter;
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
@Epic("Авторизация на портале")
@DisplayName("Класс с тестами авторизации")
public class LoginTests extends TestSeleniumBase {

    @ParameterizedTest()
    @MethodSource("userDataProvider")
    @DisplayName("Тест логина")
    @Story("logIN и logOUT на портал")
    @Description("Параметризированный тест авторизации зарегистрированных пользователей. Проверяет успешный logIN и logOUT")
    public void canLoginWithValidDataTest(String login, String password) {
        Allure.parameter("Логин", login);
        Allure.parameter("Пароль", password, Parameter.Mode.MASKED);
        app.session().login(login, password);
        assertArrayEquals(new String[]{"Добро пожаловать", "Выйти"}, app.session().getConfirmLoginMessage());
        app.session().logout();
        assertEquals("Войти", app.session().getConfirmLogoutMessage());
    }

    /**
     * Фабричная функция для предоставления данных в параметризированный тест логина
     */
    static Stream<Arguments> userDataProvider() {
        return Stream.of(
                Arguments.arguments(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword()),
                Arguments.arguments(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword()),
                Arguments.arguments(ConfigProvider.getAdminEfimovLogin(), ConfigProvider.getAdminEfimovPassword())
        );
    }


}
