package manager;

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
    private R2dbcHelper r2dbc;


    /**
     * Инициализатор браузера. Масштабирует окно браузера на максимальный размер экрана
     */
    public void init() {
        driver = new ChromeDriver();
        driver.get("https://portal8.eapo.org/olf");
        driver.manage().window().maximize();
    }

    /**
     * Закрывает браузер
     */
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
        registrator = new RegistrationHelper(this);
        return registrator;
    }


    /**
     * Возвращает класс-помощник для взаимодействия с функционалом подачи заявок
     */
    public SendingHelper sender() {
        sender = new SendingHelper(this);
        return sender;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом сохранения заявок и досылок в Soprano
     */
    public SaveAppAndAdditionHelper saver() {
        saver = new SaveAppAndAdditionHelper(this);
        return saver;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с функционалом отзыва и выпуска пин-кода
     */
    public CertificateHelper pinCode() {
        pinCode = new CertificateHelper(this);
        return pinCode;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с JDBS
     */
    public JdbcHelper jdbc() {
        jdbc = new JdbcHelper(this);
        return jdbc;
    }

    /**
     * Возвращает класс-помощник для взаимодействия с R2DBS
     */
    public R2dbcHelper r2dbc() {
        r2dbc = new R2dbcHelper(this);
        return r2dbc;
    }



}

