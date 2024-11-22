package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitApplicationIndustrialTests extends TestBase {

    /**
     * Параметризированный тест подачи заявок на ПО (1, 3, 5 образцов)
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 3, 5 })
    public void SubmitIndustrialApplicationTest (int countOfSamples) throws InterruptedException {
        app.session().login("ProkoshevPV", "qweR2304");
        String sendingConfirm = app.submitter().sendIndustrialApplication(countOfSamples);
        assertEquals("Пакет успешно подписан.", sendingConfirm);
    }

    /**
     * Параметризированный тест подачи заявок на ПО.
     * priorityType: тип приоритета
     */
    @ParameterizedTest
    @ValueSource(strings = {"previousPCT", "previousEuro", "additionalMaterials", "startsOpenShowing"})
    public void SubmitIndustrialApplicationWithPriorityTest (String priorityType) throws InterruptedException {
        app.session().login("ProkoshevPV", "qweR2304");
        String sendingConfirm = app.submitter().sendIndustrialApplicationWithPriority(priorityType);
        assertEquals("Пакет успешно подписан.", sendingConfirm);
    }

}
