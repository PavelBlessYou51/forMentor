package selenium_tests.tests;

import fixture.ConfigProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Класс содержит тесты отзыва и выпуска сертификата с установленным порядком выполнения
 */
@Epic("Профиль")
@Feature("Управление сертификатами")
@DisplayName("Класс с тестами выпуска/отзыва сертификата")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CertificateTests extends TestSeleniumBase {


    @Test
    @Order(1)
    @DisplayName("Тест деактивации сертификата пользователя")
    @Story("Деактивация сертификата")
    @Description("Тест отзыва сертификата пользователя на портале")
    public void certificateDeactivationTest() {
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        String deactivationConfirm = app.pinCode().certificateDeactivation();
        assertEquals("У вас нет действительного сертификата.", deactivationConfirm);
    }

    @Test
    @Order(2)
    @DisplayName("Тест активации сертификата пользователя")
    @Story("Активация сертификата")
    @Description("Тест выпуска сертификата пользователя на портале")
    public void certificateActivationTest() {
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        String deactivationConfirm = app.pinCode().certificateActivation();
        assertEquals("У вас есть сертификат", deactivationConfirm);
    }
}
