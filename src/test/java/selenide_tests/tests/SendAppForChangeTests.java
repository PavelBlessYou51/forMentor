package selenide_tests.tests;

import com.codeborne.selenide.junit5.BrowserPerTestStrategyExtension;
import fixture.ConfigProvider;
import jdbc.JdbcHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.FileUtils.*;

/**
 * Класс содержит тесты подачи заявлений о смене владельцев, адресов, наименований заявителей
 */
@ExtendWith(BrowserPerTestStrategyExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SendAppForChangeTests extends TestSelenideBase {

    /**
     * Параметризированный тест подачи заявления о смене владельца по патенту и заявке
     */
    @Order(1)
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Тест подачи заявления права по патенту и заявке")
    public void SendAppForOwnerChangeTest(boolean isApp) {
        String appNumber = app.jdbc().getAppNumberForChanges(isApp);
        applicationNumbersWriter("src/test/resources/list_of_app/changeAppNumbers.txt", appNumber);
        app.login().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        app.login().selectSectionOfAccount("invention");
        app.changer().selectChangeApplication();
        app.changer().typeAppNumberForChange(appNumber);
        assertTrue(app.changer().checkTypeOfApp(isApp)); // проверка указания в заголовке на тип документа в который вносятся изменения
        app.changer().selectTypeOfChange("succession");
        app.changer().pressNextButton();
        app.changer().pressNextButton();
        app.changer().deleteOldOwners();
        app.changer().addNewOwner(isApp);
        app.changer().pressNextButton();
        app.changer().pressNextButton();
        app.changer().pressNextButton();
        app.changer().uploadDocsForSuccession();
        app.changer().pressNextButton();
        app.changer().uploadPaymentOrder();
        app.changer().signAndSendDocument();
        assertEquals("Пакет успешно подписан.", app.changer().getSendingConfirm()); // Проверяем наличие сообщения об успешной отправке пакета
        app.changer().pressContinueButton();
        app.changer().selectAction();
        app.changer().findAppByNumber(appNumber);
        app.changer().openFoundAppByNumber(appNumber);
        app.changer().saveAppToSoprano();
        assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), app.changer().getSavingConfirm()); // Проверяем наличие сообщения о сохранении в Сопрано
    }

    /**
     * Тест для проверки сохранения документов в Madras
     * по проведенным тестам
     */
    @Test
    @Order(2)
    @Tag("SkipInit")
    @DisplayName("Тест проверки сохранения документов в Madras")
    public void checkSaveDocsToMadrasTest() {
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<String> appNumbers = applicationNumbersReader("src/test/resources/list_of_app/changeAppNumbers.txt");
        ArrayList<Integer> actualCount = new ArrayList<>();
        for (String number : appNumbers) {
            int count = app.jdbc().checkDocsInMadras(number);
            actualCount.add(count);
        }
        Collections.sort(actualCount);
        ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(7, 7));
        assertEquals(expectedCount, actualCount);
    }

    /**
     * Метод закрывает соединение с БД
     */
    @AfterAll
    public static void tearDown() {
        JdbcHelper jdbc = app.jdbc();
        jdbc.closePortalConnection();
    }

    /**
     * Метод удаляет файл с заявками
     */
    @AfterAll
    public static void docsCleaner() {
        fileDeleter("src/test/resources/list_of_app");
    }
}
