package selenium_tests.manager;

import fixture.ConfigProvider;
import io.qameta.allure.Step;
import jdbc.JdbcHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


/**
 * Класс содержит методы для работы с браузером, а также методы для получения классов-помощников
 */
public class ApplicationManager {

    protected WebDriver driver;
    private LoginHelper session;
    private RegistrationHelper registrator;
    private SendingHelper sender;
    private SaveAppAndAdditionHelper saver;
    private CertificateHelper pinCode;
    private JdbcHelper jdbc;


    /**
     * Инициализатор браузера. Масштабирует окно браузера на максимальный размер экрана
     */
    @Step("Инициализация браузера")
    public void init() {
        driver = new ChromeDriver();
        driver.get(ConfigProvider.getBaseUrl());
        driver.manage().window().maximize();
    }

    /**
     * Закрывает браузер
     */
    @Step("Завершение сеанса")
    public void quit() {
        driver.quit();
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом авторизации
     */
    public LoginHelper session() {
        if (session == null) {
            session = new LoginHelper(this);
        }
        return session;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом регистрации
     */
    public RegistrationHelper registrator() {
        if (registrator == null) {
            registrator = new RegistrationHelper(this);
        }
        return registrator;
    }


    /**
     * Возвращает класс-помощник для взаимодействия с функционалом подачи заявок
     */
    public SendingHelper sender() {
        if (sender == null) {
            sender = new SendingHelper(this);
        }
        return sender;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом сохранения заявок и досылок в Soprano
     */
    public SaveAppAndAdditionHelper saver() {
        if (saver == null) {
            saver = new SaveAppAndAdditionHelper(this);
        }
        return saver;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом отзыва и выпуска пин-кода
     */
    public CertificateHelper pinCode() {
        if (pinCode == null) {
            pinCode = new CertificateHelper(this);
        }
        return pinCode;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с JDBS
     */
    public JdbcHelper jdbc() {
        if (jdbc == null) {
            jdbc = new JdbcHelper();
        }
        return jdbc;
    }


}

