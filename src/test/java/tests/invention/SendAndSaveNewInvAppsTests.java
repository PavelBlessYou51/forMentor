package tests.invention;

import exceptions.NextButtomException;
import manager.JdbcHelper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tests.TestBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SendAndSaveNewInvAppsTests extends TestBase {

    /**
     * Тест евразийской заявки со всеми документами
     */
    @Test
    @Order(1)
    public void submitInventionEuroApplicationTest() throws NextButtomException {
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("invention");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillInventionCommonInfoPart();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().addNewApplicants(1);
        app.sender().addNewInventors(1);
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillAdditionalInfo("allPetitions");
        app.sender().fillAppDocumentFormForMadras();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillTaxFormInvention();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
        String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
    }

    /**
     * Тест PCT заявки
     */
    @Test
    @Order(2)
    public void submitInventionPCTApplicationTest() throws NextButtomException {
        String PCTNumber = app.jdbc().getPCTData();
        app.jdbc().deletePCTRecord(PCTNumber);
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("invention");
        app.sender().selectTypeOfApplication("PCT");
        app.sender().fillPCTCommonInfoPart(PCTNumber);
        app.sender().addNewApplicants(1);
        app.sender().addNewInventors(1);
        app.sender().addNewRepresentative();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillPCTAdditionalInfo();
        app.sender().fillPCTDocumentForm();
        app.sender().fillTaxFormInvention();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
        String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
    }

    /**
     * Тест подачи выделенной заявки
     */
    @Test
    @Order(3)
    public void submitAllocatedApplicationTest() throws NextButtomException {
        String appNumber = app.jdbc().getInventionApp();
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("invention");
        app.sender().selectTypeOfApplication("allocatedApp");
        app.sender().typeAppNumberForAllocatedApp(appNumber);
        app.sender().fillInventionCommonInfoPart();
        for (int i = 0; i < 6; i++) {
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
        }
        app.sender().fillAppDocumentForm("invention");
        app.sender().fillTaxFormInvention();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
        appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
    }

    /**
     * Тест подачи выделенной заявки c 4 приоритетами разного вида
     */
    @Test
    @Order(4)
    public void submitInventionEuroAppWithPrioritiesTest() throws NextButtomException {
        app.session().login("ProkoshevPV", "qweR2304");
        app.sender().selectSectionOfAccount("invention");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillInventionCommonInfoPart();
        app.sender().addInventionPriority("previousPCT");
        app.sender().addInventionPriority("previousEuro");
        app.sender().addInventionPriority("additionalMaterials");
        app.sender().addInventionPriority("startsOpenShowing");
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().addNewApplicants(2);
        app.sender().addNewInventors(2);
        for (int i = 0; i < 3; i++) {
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
        }
        app.sender().fillAppDocumentForm("invention");
        app.sender().fillTaxFormInvention();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
        String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(5, sopranoRecords); // проверка формирования записей в Soprano
        int countOfPriorities = app.jdbc().checkInventionAppPriorities(appNumber);
        assertEquals(4, countOfPriorities); // проверка формирования записей в Soprano о приоритетах
    }

    /**
     * Тест для проверки сохранения документов в Madras
     * по проведенным тестам
     */
    @Test
    @Order(5)
    @Tag("SkipInit")
    public void checkSaveDocsToMadrasTest() {
        try {
            Thread.sleep(90000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<String> appNumbers = app.sender().applicationNumbersReader("src/test/resources/list_of_app/inventionAppNumbers.txt");
        ArrayList<Integer> actualCount = new ArrayList<>();
        for (String number : appNumbers) {
            int count = app.jdbc().checkDocsInMadras(number);
            actualCount.add(count);
        }
        Collections.sort(actualCount);
        ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(10, 10, 10, 20));
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
        app.session().fileDeleter("src/test/resources/list_of_app");
    }

}
