package tests;

import exceptions.NextButtomException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс содержит тестовые классы с методами подачи и сохранения заявок в Soprano
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class SendAndSaveAppAndAdditionTests extends TestBase {

    @Nested
    @Order(1)
    public class SubmitApplicationIndustrialTests {

        /**
         * Параметризированный тест подачи заявок на ПО (1, 3, 5 образцов)
         */
        @ParameterizedTest
        @ValueSource(ints = {3, 5})
        public void submitIndustrialApplicationTest(int countOfSamples) throws NextButtomException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendIndustrialApplication(countOfSamples);
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

        /**
         * Параметризированный тест подачи заявок на ПО.
         * priorityType: тип приоритета
         */
        @ParameterizedTest
        @ValueSource(strings = {"previousPCT", "previousEuro", "additionalMaterials", "startsOpenShowing"})
        public void submitIndustrialApplicationWithPriorityTest(String priorityType) throws NextButtomException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendIndustrialApplicationWithPriority(priorityType);
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

    }

    @Nested
    @Order(3)
    public class SaveAppTests {

        /**
         * Параметризированный тест сохранения заявок в Soprano
         */
        @ParameterizedTest
        @ValueSource(strings = {"invention", "industrial"})
        public void saveInventionApp(String appType) {
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            boolean result = app.saver().saveDocsToSoprano(appType, "заявки");
            assertTrue(result);
        }

    }

    @Nested
    @Order(4)
    public class CheckAppsInSopranoTests {


        /**
         * Фабричная функция для предоставления номеров заявок по ИЗО для проверки их наличия в БД Soprano
         */
        static List<String> inventionAppNumbersProvider() throws IOException {
            List<String> listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/inventionAppNumbers.txt").toAbsolutePath());
            return listOfApps;
        }

        /**
         * Параметризированный тест для проверки корректной записи сохраненной заявки по ИЗО в БД Soprano
         */
        @ParameterizedTest
        @MethodSource("inventionAppNumbersProvider")
        @Tag("SkipInit")
        public void checkInventionAppInSopranoTest(String appNumber) {
            int resultCount = app.jdbc().checkInventionAppInSoprano(appNumber);
            assertEquals(3, resultCount);
        }

        /**
         * Фабричная функция для предоставления номеров заявок по ПО для проверки их наличия в БД Soprano
         */
        static List<String> industrialAppNumbersProvider() throws IOException {
            List<String> listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/industrialAppNumbers.txt").toAbsolutePath());
            return listOfApps;
        }

        /**
         * Параметризированный тест для проверки корректной записи сохраненной заявки по ПО в БД Soprano
         */
        @ParameterizedTest
        @MethodSource("industrialAppNumbersProvider")
        @Tag("SkipInit")
        public void checkIndustrialAppInSopranoTest(String appNumber) {
            int resultCount = app.jdbc().checkInventionAppInSoprano(appNumber);
            assertEquals(3, resultCount);
        }

    }


    @Nested
    @Order(5)
    public class SubmitAdditionTests {


        /**
         * Фабричная функция для предоставления номеров заявок по ИЗО в тест подачи досылок
         */
        static List<String> inventionAppNumbersProvider() throws IOException {
            List<String> listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/inventionAppNumbers.txt").toAbsolutePath());
            return listOfApps;
        }

        /**
         * Параметризированный тест подачи досылок по изобретениям
         */
        @ParameterizedTest
        @MethodSource("inventionAppNumbersProvider")
        public void submitInventionAdditionalTest(String appNumber) {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendAdditionForInventionApp(appNumber, "soprano");
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }

        /**
         * Фабричная функция для предоставления номеров заявок по ПО в тест подачи досылок
         */
        static List<String> industrialAppNumbersProvider() throws IOException {
            List<String> listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/industrialAppNumbers.txt").toAbsolutePath());
            return listOfApps;
        }

        /**
         * Параметризированный тест подачи досылок по ПО
         */
        @ParameterizedTest
        @MethodSource("industrialAppNumbersProvider")
        public void submitIndustrialAdditionalTest(String appNumber) throws NextButtomException {
            app.session().login("ProkoshevPV", "qweR2304");
            String sendingConfirm = app.sender().sendAdditionForIndustrialApp(appNumber);
            assertEquals("Пакет успешно подписан.", sendingConfirm);
        }


    }

    @Nested
    @Order(6)
    public class SaveAdditionTests {

        /**
         * Параметризированный тест сохранения досылок
         */
        @ParameterizedTest
        @ValueSource(strings = {"invention", "industrial"})
        public void saveAdditionToApp(String appType) {
            app.session().login("ProkoshevPV1", "0j2Z7O8G");
            boolean result = app.saver().saveDocsToSoprano(appType, "досылки");
            assertTrue(result);
        }


    }

    @Nested
    @Order(7)
    public class CheckAdditionInSopranoTests {


        /**
         * Фабричная функция для предоставления номеров заявок по ИЗО для проверки их наличия досылок в Soprano
         */
        static List<String> inventionAdditionNumbersProvider() throws IOException {
            List<String> listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/inventionAppNumbers.txt").toAbsolutePath());
            return listOfApps;
        }

        /**
         * Параметризированный тест для проверки корректной записи сохраненной досылки по ИЗО в БД Soprano
         */
        @ParameterizedTest
        @MethodSource("inventionAdditionNumbersProvider")
        @Tag("SkipInit")
        public void checkInventionAppInSopranoTest(String appNumber) {
            int resultCount = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, resultCount);
        }

        /**
         * Фабричная функция для предоставления номеров досылок по ПО для проверки их наличия в БД Soprano
         */
        static List<String> industrialAdditionNumbersProvider() throws IOException {
            List<String> listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/industrialAppNumbers.txt").toAbsolutePath());
            return listOfApps;
        }

        /**
         * Параметризированный тест для проверки корректной записи сохраненной досылки по ПО в БД Soprano
         */
        @ParameterizedTest
        @MethodSource("industrialAdditionNumbersProvider")
        @Tag("SkipInit")
        public void checkIndustrialAppInSopranoTest(String appNumber) {
            int resultCount = app.jdbc().checkInventionAdditionInSoprano(appNumber);
            assertEquals(2, resultCount);
        }

    }

    /**
     * Метод удаляет списки файлы со списками заявок после выполнения тестов сохранения заявок и досылок
     */
    @AfterAll
    public static void docsCleaner() {
        app.session().fileDeleter("src/test/resources/list_of_app");

    }

}
