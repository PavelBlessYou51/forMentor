package fixture;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс с конфигурациями. Предоставляет данные для доступа к БД и порталу
 */
public class ConfigProvider {
    private static final String PROPERTIES_FILE = "config.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = ConfigProvider.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new FileNotFoundException("Не удается найти файл " + PROPERTIES_FILE);
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурационного файла " + PROPERTIES_FILE, e);
        }
    }

    // URL's

    /**
     * Метод предоставляет базовый URL
     */
    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    // Доступ к БД портала

    /**
     * Метод предоставляет URL к БД портала
     */
    public static String getPortalUrl() {
        return properties.getProperty("portal.url");
    }

    /**
     * Метод предоставляет логин к БД портала
     */
    public static String getPortalLogin() {
        return properties.getProperty("portal.login");
    }

    /**
     * Метод предоставляет пароль к БД портала
     */
    public static String getPortalPassword() {
        return properties.getProperty("portal.password");
    }

    // Доступ к Soprano

    /**
     * Метод предоставляет URL к Soprano
     */
    public static String getSopranoUrl() {
        return properties.getProperty("soprano.url");
    }

    /**
     * Метод предоставляет логин к Soprano
     */
    public static String getSopranoLogin() {
        return properties.getProperty("soprano.login");
    }

    /**
     * Метод предоставляет пароль к Soprano
     */
    public static String getSopranoPassword() {
        return properties.getProperty("soprano.password");
    }

    // Доступ к Madras

    /**
     * Метод предоставляет URL к Madras
     */
    public static String getMadrasUrl() {
        return properties.getProperty("madras.url");
    }

    /**
     * Метод предоставляет логин к Madras
     */
    public static String getMadrasLogin() {
        return properties.getProperty("madras.login");
    }

    /**
     * Метод предоставляет пароль к Madras
     */
    public static String getMadrasPassword() {
        return properties.getProperty("madras.password");
    }

    // Пользователи портала

    /**
     * Метод предоставляет логин админской записи Прокошева П.В.
     */
    public static String getAdminLogin() {
        return properties.getProperty("admin.login");
    }

    /**
     * Метод предоставляет пароль админской записи Прокошева П.В.
     */
    public static String getAdminPassword() {
        return properties.getProperty("admin.password");
    }

    /**
     * Метод предоставляет логин пользовательской записи Прокошева П.В.
     */
    public static String getUserLogin() {
        return properties.getProperty("usual.user.login");
    }

    /**
     * Метод предоставляет пароль пользовательской записи Прокошева П.В.
     */
    public static String getUserPassword() {
        return properties.getProperty("usual.user.password");
    }

    /**
     * Метод предоставляет логин админской записи Ефимова С.Н.
     */
    public static String getAdminEfimovLogin() {
        return properties.getProperty("admin.efimov.login");
    }

    /**
     * Метод предоставляет пароль админской записи Ефимова С.Н.
     */
    public static String getAdminEfimovPassword() {
        return properties.getProperty("admin.efimov.password");
    }

}
