package tests;

import fixture.ConfigProvider;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Класс содержит тесты отзыва и выпуска сертификата с установленным порядком выполнения
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CertificateTests extends TestBase {


    /**
     * Тест отзыва сертификата
     */
    @Test
    @Order(1)
    @DisplayName("Тест деактивации сертификата пользователя")
    public void certificateDeactivation() {
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        String deactivationConfirm = app.pinCode().certificateDeactivation();
        assertEquals("У вас нет действительного сертификата.", deactivationConfirm);
    }

    /**
     * Тест выпуска сертификата
     */
    @Test
    @Order(2)
    @DisplayName("Тест активации сертификата пользователя")
    public void certificateActivation() {
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        String deactivationConfirm = app.pinCode().certificateActivation();
        assertEquals("У вас есть сертификат", deactivationConfirm);
    }


}
