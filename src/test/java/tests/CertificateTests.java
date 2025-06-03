package tests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
    public void certificateDeactivation() {
        app.session().login("ProkoshevPV", "qweR2304");
        String deactivationConfirm = app.pinCode().certificateDeactivation();
        assertEquals("У вас нет действительного сертификата.", deactivationConfirm);
    }

    /**
     * Тест выпуска сертификата
     */
    @Test
    @Order(2)
    public void certificateActivation() {
        app.session().login("ProkoshevPV", "qweR2304");
        String deactivationConfirm = app.pinCode().certificateActivation();
        assertEquals("У вас есть сертификат", deactivationConfirm);
    }


}
