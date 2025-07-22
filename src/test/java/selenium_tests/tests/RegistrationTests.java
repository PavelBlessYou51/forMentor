package selenium_tests.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import jdbc.JdbcHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс содержит тесты регистрации
 */
@Epic("Регистрация пользователей на портале")
@DisplayName("Класс с тестами регистрации")
public class RegistrationTests extends TestSeleniumBase {

    @Test
    @DisplayName("Тест регистрации физического лица")
    @Story("Отправка запроса на регистрацию пользователя - физ лица на портале")
    @Description("Тест проверяет отправку запроса на регистрацию физического лица")
    public void personRegistrationTest() {
        String surname = app.registrator().fillRegistrationFormForPerson();
        int resultCount = app.jdbc().checkRegisteredEntity(surname, true);
        assertEquals("Запрос на регистрацию успешно отправлен", app.registrator().getRegistrationRequestMessageConfirm());
        assertEquals(1, resultCount);
        app.quit();
    }

    @Test
    @DisplayName("Тест регистрации юридического лица")
    @Story("Отправка запроса на регистрацию пользователя - юр лица на портале")
    @Description("Тест проверяет отправку запроса на регистрацию юридического лица")
    public void organisationRegistrationTest() {
        String surname = app.registrator().fillRegistrationFormForOrganisation();
        int resultCount = app.jdbc().checkRegisteredEntity(surname, true);
        assertEquals("Запрос на регистрацию успешно отправлен", app.registrator().getRegistrationRequestMessageConfirm());
        assertEquals(1, resultCount);
        app.quit();

    }

    @ParameterizedTest
    @ValueSource(strings = {"invention", "industrial"})
    @DisplayName("Тест регистрации патентного поверенного")
    @Story("Отправка запроса на регистрацию пользователя - патентного поверенного на портале")
    @Description("Параметризированный тест. Поочередно проверяется отправку запросу на регистрацию ПП по изобретениям и ПО")
    public void patentAgentRegistrationTest(String agentType) {
        int numberOfEntities = app.jdbc().getNumberOfPortalUserEntries(false);
        app.registrator().fillRegistrationFormForPatentAgent(agentType);
        int numberOfEntitiesAfterRegistration = app.jdbc().getNumberOfPortalUserEntries(true);
        int resultCount = numberOfEntitiesAfterRegistration - numberOfEntities;
        System.out.println(resultCount);
        assertEquals("Запрос на регистрацию успешно отправлен", app.registrator().getRegistrationRequestMessageConfirm());
        assertEquals(1, resultCount);
    }

    /**
     * Метод удаляет из тестовой БД зарегистрированные сущности и закрывает соединение с БД
     */
    @AfterAll
    public static void tearDown() {
        JdbcHelper jdbc = app.jdbc();
        jdbc.pationAgentDeleter();
        jdbc.personAndOrganisationDeleter();
        jdbc.closePortalConnection();

    }


}
