package selenium_tests.tests.invention;

import exceptions.NextButtomException;
import fixture.ConfigProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jdbc.JdbcHelper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import selenium_tests.tests.TestSeleniumBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.FileUtils.*;

@Epic("Отправка новых заявок")
@Feature("Отправка заявок на изобретения")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Класс с тестами отправки новых заявок по ИЗО")
public class SendAndSaveNewInvAppsTests extends TestSeleniumBase {

    @Test
    @Order(1)
    @DisplayName("Тест отправки евразийской заявки со всеми документами")
    @Story("Отправка евразийской заявки со всеми документами")
    @Description("Тест отправки евразийской заявки со всеми документами. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
    public void submitInventionEuroApplicationTest() throws NextButtomException {
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        app.sender().selectSectionOfAccount("invention");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillInventionCommonInfoPart();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().addNewApplicants(1);
        app.sender().addNewInventors(1);
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillAdditionalInfo("allPetitions");
        app.sender().fillAppDocumentFormForMadras(false);
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().fillTaxFormInvention();
        app.sender().signInApplication();
        String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals("Пакет успешно подписан.", sendingConfirmation); // проверка успешной отправки заявки
        String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
    }

    @Test
    @Order(2)
    @DisplayName("Тест подачи PCT заявки")
    @Story("Отправка РСТ заявки")
    @Description("Тест отправки PCT заявки с несколькими документами. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
    public void submitInventionPCTApplicationTest() throws NextButtomException {
        String PCTNumber = app.jdbc().getPCTData();
        app.jdbc().deletePCTRecord(PCTNumber);
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
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
        applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
    }

    @Test
    @Order(3)
    @DisplayName("Тест подачи выделенной евразийской заявки")
    @Story("Отправка выделенной заявки")
    @Description("Тест отправки выделенной заявки с несколькими документами. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
    public void submitAllocatedApplicationTest() throws NextButtomException {
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        app.sender().selectSectionOfAccount("invention");
        app.sender().selectTypeOfApplication("euroApp");
        app.sender().fillInventionCommonInfoPart();
        app.sender().click(By.cssSelector("input[value='Далее']"), true);
        app.sender().addNewApplicants(1);
        app.sender().addNewInventors(1);
        for (int i = 0; i < 3; i++) {
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
        }
        app.sender().fillAppDocumentForm("invention");
        app.sender().fillTaxFormInvention();
        app.sender().signInApplication();
        String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
        app.session().logout();
        app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        app.session().logout();
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
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
        applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(3, sopranoRecords); // проверка формирования записей в Soprano
    }

    @Test
    @Order(4)
    @DisplayName("Тест подачи евразийской заявки c 4 приоритетами разного вида")
    @Story("Отправка евразийской заявки с 4 приоритетами разного вида")
    @Description("Тест отправки евразийской заявки c 4 приоритетами разного вида и несколькими документами. Проверяются сообщения на фронте и сохранения мета-данных в Soprano")
    public void submitInventionEuroAppWithPrioritiesTest() throws NextButtomException {
        app.session().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
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
        applicationNumbersWriter("src/test/resources/list_of_app/inventionAppNumbers.txt", appNumber);
        app.session().logout();
        app.session().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        app.sender().selectSectionOfAccount("invention");
        app.saver().saveDocToSoprano("заявки", appNumber);
        String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
        assertEquals(String.format("Заявка %s успешно сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
        int sopranoRecords = app.jdbc().checkInventionAppInSoprano(appNumber);
        assertEquals(5, sopranoRecords); // проверка формирования записей в Soprano
        int countOfPriorities = app.jdbc().checkInventionAppPriorities(appNumber);
        assertEquals(4, countOfPriorities); // проверка формирования записей в Soprano о приоритетах
    }

    @Test
    @Order(5)
    @Tag("SkipInit")
    @DisplayName("Тест проверки сохранения документов в Madras")
    @Story("Проверка сохранения статики в Madras")
    @Description("Тест для проверки сохранения документов в Madras по проведенным тестам")
    public void checkSaveDocsToMadrasTest() {
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<String> appNumbers = applicationNumbersReader("src/test/resources/list_of_app/inventionAppNumbers.txt");
        ArrayList<Integer> actualCount = new ArrayList<>();
        for (String number : appNumbers) {
            int count = app.jdbc().checkDocsInMadras(number);
            actualCount.add(count);
        }
        Collections.sort(actualCount);
        ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(10, 10, 10, 19));
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
