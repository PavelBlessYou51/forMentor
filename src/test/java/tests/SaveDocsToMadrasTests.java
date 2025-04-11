package tests;

import exceptions.NextButtomException;
import manager.JdbcHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SaveDocsToMadrasTests extends TestBase {

    /**
     * Фабричная функция для предоставления номеров заявок по ИЗО в тест подачи досылок
     */
    static List<String> inventionAppNumbersProvider() throws IOException {
        List<String> listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/inventionAppNumbers.txt").toAbsolutePath());
        return listOfApps;
    }

    @ParameterizedTest
    @Order(2)
    @MethodSource("inventionAppNumbersProvider")
    public void sendInventionAdditionalTest(String appNumber) {
        app.session().login("ProkoshevPV", "qweR2304");
        //String sendingConfirm = app.sender().sendAdditionForInventionApp(appNumber, "madras");
        //assertEquals("Пакет успешно подписан.", sendingConfirm);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        boolean result = app.saver().saveDocsToSoprano("invention", "досылки");
        assertTrue(result);
    }

    @Test
    @Order(3)
    public void IndustrialApplicationTest() throws NextButtomException {
        app.session().login("ProkoshevPV", "qweR2304");
        String sendingConfirm = app.sender().sendIndustrialApplication(1);
        assertEquals("Пакет успешно подписан.", sendingConfirm);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        boolean result = app.saver().saveDocsToSoprano("industrial", "заявки");
        assertTrue(result);

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
    public void submitIndustrialAdditionalTest(String appNumber) throws NextButtomException{
        app.session().login("ProkoshevPV", "qweR2304");
        String sendingConfirm = app.sender().sendAdditionForIndustrialApp(appNumber);
        assertEquals("Пакет успешно подписан.", sendingConfirm);
        app.session().logout();
        app.session().login("ProkoshevPV1", "0j2Z7O8G");
        boolean result = app.saver().saveDocsToSoprano("industrial", "досылки");
        assertTrue(result);
    }

    /**
     * Фабричная функция для предоставления номеров заявок по ПО и ИЗО для проверки сохранения документов в Madras
     */
    static List<String> getListOfAppNumbers() {
        List<String> listOfApps;
        try {
            listOfApps = Files.readAllLines(Paths.get("src/test/resources/list_of_app/industrialAppNumbers.txt").toAbsolutePath());
            listOfApps.addAll(Files.readAllLines(Paths.get("src/test/resources/list_of_app/inventionAppNumbers.txt").toAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listOfApps;
    }

//    @ParameterizedTest
//    @Tag("SkipInit")
//    @MethodSource("getListOfAppNumbers")
//    public void checkSaveDocsMadrasTest(String appNumber) {
//        int List<Integer> resultCount = app.r2dbc().checkDocsInMadras(appNumber);
//        assertArrayEquals(new int[]{59, 36}, resultCount);
//    }

    /**
     * Метод закрывает соединение с БД
     */
    @AfterAll
    public static void tearDown() {
        JdbcHelper jdbc = app.jdbc();
        jdbc.closePortalConnection();

    }


}
