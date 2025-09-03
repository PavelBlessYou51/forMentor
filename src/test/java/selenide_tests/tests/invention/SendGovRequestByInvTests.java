package selenide_tests.tests.invention;

import com.codeborne.selenide.junit5.BrowserPerTestStrategyExtension;
import fixture.ConfigProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jdbc.JdbcHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import selenide_tests.tests.TestSelenideBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс содержит тесты отправки запроса гос органа / нац ведомства
 */
@Epic("Подача дополнительных материалов")
@Feature("Запрос нацведомства / Гос. Органа")
@DisplayName("Класс с тестами направления запросов гос органов и нац ведомств по ИЗО")
@ExtendWith(BrowserPerTestStrategyExtension.class)
public class SendGovRequestByInvTests extends TestSelenideBase {

    @Test
    @DisplayName("Тест направления запроса гос органа")
    @Story("Направление запроса гос органа")
    @Description("Тест направления и сохранения запроса гос органов")
    public void sendGovernmentRequestTest() {
        String appNumber = app.jdbc().getInventionApp(14157, 'I');
        app.login().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        app.login().selectSectionOfAccount("invention");
        app.login().selectApplicationType("request");
        app.request().typeAppNumberForRequest(appNumber);
        app.request().uploadRequest();
        app.request().uploadAttachments();
        app.request().signAndSendDocument();
        assertEquals("Пакет успешно подписан.", app.request().getConfirmMessage());
        app.login().logout();
        app.login().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        app.login().selectSectionOfAccount("invention");
        app.login().selectSection();
        app.login().findAppByNumber(appNumber);
        app.login().openFoundAppByNumber(appNumber);
        app.request().saveRequest();
        assertEquals(String.format("Досылка для заявки %s сохранена в Soprano.", appNumber), app.changer().getConfirmMessage());
        try {
            Thread.sleep(20000); // ожидание записи документов в БД
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int actualCount = app.jdbc().checkDocsInMadras(appNumber);
        assertEquals(4, actualCount);
    }

    @Test
    @DisplayName("Тест направления запроса гос органа без запроса")
    @Story("Направление запроса гос органа без запроса")
    @Description("Тест направления пустого запроса гос органов. Запрос не должен отправиться")
    public void sendEmptyGovernmentRequestTest() {
        String appNumber = app.jdbc().getInventionApp(14157, 'I');
        app.login().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        app.login().selectSectionOfAccount("invention");
        app.login().selectApplicationType("request");
        app.request().typeAppNumberForRequest(appNumber);
        app.request().uploadAttachments();
        app.request().signAndSendDocument();
        assertEquals("Документ \"Запрос нац. пат. ведомства / Государственного органа\" является обязательным для подачи.", app.request().getConfirmMessage());
    }

    /**
     * Метод закрывает соединение с БД
     */
    @AfterAll
    public static void tearDown() {
        JdbcHelper jdbc = app.jdbc();
        jdbc.closePortalConnection();
    }
}
