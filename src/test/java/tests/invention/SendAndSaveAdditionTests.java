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

public class SendAndSaveAdditionTests extends TestBase {

    /**
     * Класс с тестами подачи измененных заявлений на ИЗО
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SendChangedApplicationTests {

        /**
         * Тест подачи изменной евразийской заявки со всеми возможными документами.
         * Подается новая первоначальная заявка
         */
        @Test
        @Order(1)
        public void submitChangedApplicationTest() {
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillInventionCommonInfoPart("withoutPriority");
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", app.sender().getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("changedApp");
            app.sender().typeAppNumberForChangedApp(appNumber);
            app.sender().fillInventionCommonInfoPart("withoutPriority");
            for (int i = 0; i < 5; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillAppDocumentFormForMadras();
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
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
         * Тест подачи изменной PCT заявки
         * Подается новая первоначальная заявка
         */
        @Test
        @Order(2)
        public void submitChangedPCTApplicationTest() throws NextButtomException {
            String PCTNumber = app.jdbc().getPCTData();
            app.jdbc().deletePCTRecord(PCTNumber);
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("PCT");
            app.sender().fillPCTCommonInfoPart(PCTNumber);
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            app.sender().addNewRepresentative();
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("changedApp");
            app.sender().typeAppNumberForChangedApp(appNumber);
            app.sender().fillInventionCommonInfoPart("withoutPriority");
            for (int i = 0; i < 5; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillPCTDocumentForm();
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
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
         * Тест подачи измененной выделенной заявки
         */
        @Test
        @Order(3)
        public void submitChangedAllocatedApplicationTest() throws NextButtomException {
            String appNumber = app.jdbc().getInventionApp();
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("allocatedApp");
            app.sender().typeAppNumberForAllocatedApp(appNumber);
            app.sender().fillInventionCommonInfoPart("withoutPriority");
            for (int i = 0; i < 5; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", app.sender().getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("changedApp");
            app.sender().typeAppNumberForChangedApp(appNumber);
            app.sender().fillInventionCommonInfoPart("withoutPriority");
            for (int i = 0; i < 5; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillAppDocumentForm("invention");
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
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
         * Тест для проверки сохранения документов в Madras
         * по проведенным тестам
         */
        @Test
        @Order(4)
        @Tag("SkipInit")
        public void checkSaveDocsToMadrasTest() {
            try {
                Thread.sleep(90000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<String> appNumbers = app.sender().applicationNumbersReader("src/test/resources/list_of_app/inventionAdditionNumbers.txt");
            ArrayList<Integer> actualCount = new ArrayList<>();
            for (String number : appNumbers) {
                int count = app.jdbc().checkDocsInMadras(number);
                actualCount.add(count);
            }
            Collections.sort(actualCount);
            ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(15, 16, 26));
            assertEquals(expectedCount, actualCount);
        }

    }

    /**
     * Класс с тестами подачи досылок на ИЗО
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SendAdditionTests {

        /**
         * Тест подачи досылки PCT заявке
         * Подается новая первоначальная заявка
         */
        @Test
        @Order(1)
        public void submitAdditionPCTApplicationTest() throws NextButtomException {
            String PCTNumber = app.jdbc().getPCTData();
            app.jdbc().deletePCTRecord(PCTNumber);
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("PCT");
            app.sender().fillPCTCommonInfoPart(PCTNumber);
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            app.sender().addNewRepresentative();
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("addition");
            app.sender().typeAppNumberForAddition(appNumber);
            app.sender().fillAdditionDocumentForm();
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("досылки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
        }

        /**
         * Тест подачи досылки со всеми документами по евразийской заявке
         */
        @Test
        @Order(2)
        public void submitAdditionApplicationTest() throws NextButtomException {
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillInventionCommonInfoPart("withoutPriority");
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", app.sender().getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("addition");
            app.sender().typeAppNumberForAddition(appNumber);
            app.sender().fillAdditionDocFormForMadras();
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("досылки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
        }

        /**
         * Тест подачи досылки выделенной евразийской заявке
         */
        @Test
        @Order(3)
        public void submitAdditionAllocatedApplicationTest() throws NextButtomException {
            String appNumber = app.jdbc().getInventionApp();
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("allocatedApp");
            app.sender().typeAppNumberForAllocatedApp(appNumber);
            app.sender().fillInventionCommonInfoPart("withoutPriority");
            for (int i = 0; i < 5; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", app.sender().getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("addition");
            app.sender().typeAppNumberForAddition(appNumber);
            app.sender().fillAdditionDocumentForm();
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("досылки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
        }

        /**
         * Тест для проверки сохранения документов в Madras
         * по проведенным тестам
         */
        @Test
        @Order(4)
        @Tag("SkipInit")
        public void checkSaveDocsToMadrasTest() {
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<String> appNumbers = app.sender().applicationNumbersReader("src/test/resources/list_of_app/inventionAdditionNumbers.txt");
            ArrayList<Integer> actualCount = new ArrayList<>();
            for (String number : appNumbers) {
                int count = app.jdbc().checkDocsInMadras(number);
                actualCount.add(count);
            }
            Collections.sort(actualCount);
            ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(14, 15, 22));
            assertEquals(expectedCount, actualCount);
        }

    }

    /**
     * Класс с тестами подачи досылок на ИЗО с указанием даты
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SendAdditionWithDateTests {

        @Test
        @Order(1)
        public void submitAdditionWithDateTest() throws InterruptedException {
            app.session().login("ProkoshevPV", "qweR2304");
            app.sender().selectSectionOfAccount("invention");
            app.sender().selectTypeOfApplication("euroApp");
            app.sender().fillInventionCommonInfoPart("withoutPriority");
            app.sender().addNewApplicants(1);
            app.sender().addNewInventors(1);
            for (int i = 0; i < 3; i++) {
                app.sender().click(By.cssSelector("input[value='Далее']"), true);
            }
            app.sender().fileUploadWithCheck("(//div[contains(@id, 'upload')]//input[@type='file'])[1]", app.sender().getAbsolutePathToFile("src/test/resources/file_to_upload/doc_for_madras_invention/Описание изобретения%(обычное).pdf"));
            app.sender().click(By.cssSelector("input[value='Далее']"), true);
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String appNumber = app.sender().extractAppNumber(By.xpath("//span[contains(text(), 'Номер заявки:')]"));
            app.session().logout();
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            app.sender().selectSectionOfAccount("invention");
            app.saver().saveDocToSoprano("заявки", appNumber);
            app.sender().click(By.xpath("//span[contains(text(), 'Подача заявок')]"), true);
            app.sender().selectTypeOfApplication("additionWithDate");
            app.sender().typeAppNumberForAdditionWithDate(appNumber);
            app.sender().fillAdditionDocumentForm();
            app.sender().fillTaxFormInvention();
            app.sender().signInApplication();
            String sendingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals("Пакет успешно подписан.", sendingConfirmation);
            app.sender().applicationNumbersWriter("src/test/resources/list_of_app/inventionAdditionNumbers.txt", appNumber);
            app.sender().click(By.cssSelector("input[value='Продолжить']"), true);
            app.saver().saveDocToSoprano("досылки", appNumber);
            String savingConfirmation = app.sender().getTextFromElement(By.cssSelector("span[class='error-message']"));
            assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), savingConfirmation); // проверка наличия сообщения об успешной записи в Soprano
            int sopranoRecords = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, sopranoRecords); // проверка формирования записей в Soprano
        }

        @Test
        @Order(2)
        @Tag("SkipInit")
        public void checkSaveDocsToMadrasTest() {
            try {
                Thread.sleep(90000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<String> appNumbers = app.sender().applicationNumbersReader("src/test/resources/list_of_app/inventionAdditionNumbers.txt");
            ArrayList<Integer> actualCount = new ArrayList<>();
            for (String number : appNumbers) {
                int count = app.jdbc().checkDocsInMadras(number);
                actualCount.add(count);
            }
            Collections.sort(actualCount);
            ArrayList<Integer> expectedCount = new ArrayList<>(Arrays.asList(15));
            assertEquals(expectedCount, actualCount);
        }

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
