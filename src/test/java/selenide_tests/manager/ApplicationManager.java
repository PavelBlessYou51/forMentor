package selenide_tests.manager;

import jdbc.JdbcHelper;
import org.bouncycastle.cert.ocsp.Req;

/**
 * Класс содержит экземпляры помощников для работы с порталом
 */
public class ApplicationManager {

    private ChangingHelper changer;
    private JdbcHelper jdbc;
    private LoginHelper login;
    private ForwardHelper forward;
    private RequestHelper request;

    /**
     * Возвращает класс-помощник для взаимодействия с JDBS
     */
    public JdbcHelper jdbc() {
        if (jdbc == null) {
            jdbc = new JdbcHelper();
        }
        return jdbc;
    }

    /**
     * Возвращает класс-помощник для подачи заявлений об изменении заявителя\владельца\адреса
     * по патенту и заявлению
     */
    public ChangingHelper changer() {
        if (changer == null) {
            changer = new ChangingHelper();
        }
        return changer;
    }

    /**
     * Возвращает класс-помощник для подачи заявлений об изменении заявителя\владельца\адреса
     * по патенту и заявлению
     */
    public LoginHelper login() {
        if (login == null) {
            login = new LoginHelper();
        }
        return login;
    }

    /**
     * Возвращает класс-помощник для пересылки заявки из нацведомства
     */
    public ForwardHelper forward() {
        if (forward == null) {
            forward = new ForwardHelper();
        }
        return forward;
    }

    /**
     * Возвращает класс-помощник для направления запроса гос органа \ нац ведомства
     */
    public RequestHelper request() {
        if (request == null) {
            request = new RequestHelper();
        }
        return request;
    }




}
