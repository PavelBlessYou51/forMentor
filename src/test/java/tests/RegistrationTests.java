package tests;

import bd_manager.JdbcManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс содержит тесты регистрации
 */
public class RegistrationTests extends TestBase{

    /**
     * Тест проверяет отправку запроса на регистрацию физического лица
     */
    @Test
    public void personRegistration () throws InterruptedException {
        app.registrator().fillRegistrationFormForPerson();
        assertEquals("Запрос на регистрацию успешно отправлен", app.registrator().getRegistrationRequestMessageConfirm());
    }

    /**
     * Тест проверяет отправку запроса на регистрацию юридического лица
     */
    @Test
    public void organisationRegistration () throws InterruptedException {
        app.registrator().fillRegistrationFormForOrganisation();
        assertEquals("Запрос на регистрацию успешно отправлен", app.registrator().getRegistrationRequestMessageConfirm());
    }

    /**
     * Параметризированный тест.
     * Поочередно проверяется отправку запросу на регистриацию ПП по изобретениям и ПО
     */
    @ParameterizedTest
    @ValueSource(strings = { "invention", "industrial" })
    public void patentAgentRegistration (String agentType) throws InterruptedException {
        app.registrator().fillRegistrationFormForPatentAgent(agentType);
        assertEquals("Запрос на регистрацию успешно отправлен", app.registrator().getRegistrationRequestMessageConfirm());

    }

    /**
     * Метод удаляет из тестовой БД зарегистрированных патентных поверенных
     */
    @AfterAll
    public static void deletePatientAgents() {
        JdbcManager jdbc = new JdbcManager();
        jdbc.pationAgentDeleter();

    }


}
