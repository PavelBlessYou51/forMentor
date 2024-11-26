package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitApplicationInventionTests extends TestBase {

    /**
     * Параметризированный тест подачи заявок на изобретения.
     * priorityType: тип приоритета или его отсутствие
     */
    @ParameterizedTest
    @ValueSource(strings = {"withoutPreority", "previousPCT", "previousEuro", "additionalMaterials", "startsOpenShowing"})
    public void SubmitInventionApplicationTest (String priorityType) throws InterruptedException {
        app.session().login("ProkoshevPV", "qweR2304");
        String sendingConfirm = app.submitter().sendInventionApplication(priorityType);
        assertEquals("Пакет успешно подписан.", sendingConfirm);
    }

    /**
     * Тест заявки на изобретение с ходатайством
     */
    @Test
    public void SubmitInventionApplicationWithPetitionTest () throws InterruptedException {
        app.session().login("ProkoshevPV", "qweR2304");
        String sendingConfirm = app.submitter().sendInventionApplicationWithPetition();
        assertEquals("Пакет успешно подписан.", sendingConfirm);
    }

    /**
     * Тест PCT заявки
     */
    @Test
    public void SubmitInventionPCTApplicationTest () throws InterruptedException {
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        String sendingConfirm = app.submitter().sendInventionPCTApplication();
        assertEquals("Пакет успешно подписан.", sendingConfirm);
    }




}

