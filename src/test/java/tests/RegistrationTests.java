package tests;

import manager.JdbcHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс содержит тесты регистрации
 */
public class RegistrationTests extends TestBase {

    /**
     * Тест проверяет отправку запроса на регистрацию физического лица
     */
    @Test
    @DisplayName("Тест регистрации физического лица")
    public void personRegistration() {
        String surname = app.registrator().fillRegistrationFormForPerson();
        int resultCount = app.jdbc().checkRegisteredEntity(surname, true);
        assertEquals("Запрос на регистрацию успешно отправлен", app.registrator().getRegistrationRequestMessageConfirm());
        assertEquals(1, resultCount);
        app.quit();
    }

    /**
     * Тест проверяет отправку запроса на регистрацию юридического лица
     */
    @DisplayName("Тест регистрации юридического лица")
    @Test
    public void organisationRegistration() {
        String surname = app.registrator().fillRegistrationFormForOrganisation();
        int resultCount = app.jdbc().checkRegisteredEntity(surname, true);
        assertEquals("Запрос на регистрацию успешно отправлен", app.registrator().getRegistrationRequestMessageConfirm());
        assertEquals(1, resultCount);
        app.quit();

    }

    /**
     * Параметризированный тест.
     * Поочередно проверяется отправку запросу на регистриацию ПП по изобретениям и ПО
     */
    @ParameterizedTest
    @ValueSource(strings = {"invention", "industrial"})
    @DisplayName("Тест регистрации патентного поверенного")
    public void patentAgentRegistration(String agentType) {
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
