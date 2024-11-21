package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitApplicationIndustrialTests extends TestBase {

    /**
     * Тест подачи заявки на ПО
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 3, 5 })
    public void SubmitIndustrialApplication (int countOfSamples) throws InterruptedException {
        app.session().login("ProkoshevPV", "qweR2304");
        String sendingConfirm = app.submitter().sendIndustrialApplication(countOfSamples);
        assertEquals("Пакет успешно подписан.", sendingConfirm);
    }

}
