package selenide_tests.tests.invention;

import exceptions.TooManyLoopsException;
import fixture.ConfigProvider;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import model.PatentAgent;
import model.PersonData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import selenide_tests.tests.TestSelenideBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Класс содержит тесты подачи заявлений о смене владельцев, адресов, наименований заявителей
 */
@Epic("Подача заявления о поддержании патента в силе")
@Feature("Пересылка EA заявки из нацведомства")
@DisplayName("Класс с тестами пересылки заявки")
public class ForwardApplicationTests extends TestSelenideBase {

    @Test
    @Story("Пересылка EA заявки из нацведомтсва с тремя представителями (Представитель, Патентный поверенный и Работник ведомства) и всеми документами")
    @DisplayName("Тест пересылки ЕА заявки из нацведомтсва с тремя представителями и всеми документами")
    public void forwardEaAppFromNDP() throws TooManyLoopsException {
        if (!app.jdbc().checkIDmember(ConfigProvider.getUserLogin())) {
            app.jdbc().setIDmember(ConfigProvider.getUserLogin());
        };
        app.login().login(ConfigProvider.getUserLogin(), ConfigProvider.getUserPassword());
        app.login().selectSectionOfAccount("invention");
        app.forward().selectApplicationType("forward");
        app.forward().typeDepartmentNumber();
        app.forward().fillNameOfInvention();
        app.forward().pressNextButton();
        app.forward().addNewOwner(true);
        app.forward().pressNextButton();
        app.forward().addNewInventor();
        app.forward().pressNextButton();
        PatentAgent agent = (PatentAgent) app.forward().addNewRepresentative(true);
        PersonData person = (PersonData) app.forward().addNewRepresentative(false);
        app.forward().pressNextButton();
        assertTrue(app.forward().checkAddressForCorrespondents(agent)); // проверяем, что в адрес для переписки подставился адрес ПП
        app.forward().pressNextButton();
        app.forward().pressNextButton();
        app.forward().uploadAllDocs();
        app.forward().pressNextButton();
        app.forward().selectCommonFee();
        app.forward().uploadPaymentOrder();
        app.forward().signAndSendDocument();
        assertEquals("Пакет успешно подписан.", app.forward().getConfirmMessage()); // Проверяем наличие сообщения об успешной отправке пакета
        String appNumber = app.forward().getAppNumber();
        app.forward().downloadFile();


    }

}
