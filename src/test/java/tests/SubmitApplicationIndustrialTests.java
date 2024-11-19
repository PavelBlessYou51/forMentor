package tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitApplicationIndustrialTests extends TestBase {

    /**
     * Тест подачи заявки на ПО
     */
    @Test
    public void SubmitIndustrialApplication () throws InterruptedException {
        app.session().login("ProkoshevPV", "qweR2304");
        String sendingConfirm = app.submitter().sendIndustrialApplication();
        assertEquals("Пакет успешно подписан.", sendingConfirm);
    }

}
