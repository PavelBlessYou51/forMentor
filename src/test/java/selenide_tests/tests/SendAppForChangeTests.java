package selenide_tests.tests;

import fixture.ConfigProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Класс содержит тесты подачи заявлений о смене владельцев, адресов, наименований заявителей
 */
public class SendAppForChangeTests extends TestSelenideBase {

    /**
     * Тест подачи заявления о смене владельца по патенту и заявке
     */
    @Test
    @DisplayName("Тест подачи заявления права по патенту и заявке")
    public void SendAppForOwnerChangeTest() {
        String appNumber = app.jdbc().getAppNumberForChanges(true);
        app.login().login(ConfigProvider.getAdminLogin(), ConfigProvider.getAdminPassword());
        app.login().selectSectionOfAccount("invention");
        app.changer().selectChangeApplication();
        app.changer().typeAppNumberForChange(appNumber);

        app.changer().selectTypeOfChange("succession");


    }
}
