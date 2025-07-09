package selenide_tests.manager;

import jdbc.JdbcHelper;

/**
 * Класс содержит экземпляры помощников для работы с порталом
 */
public class ApplicationManager {

    private ChangingHelper changer;
    private JdbcHelper jdbc;
    private LoginHelper login;

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



}
