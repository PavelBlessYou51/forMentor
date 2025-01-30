package manager;

import java.sql.*;
import java.util.concurrent.TimeUnit;


/**
 * Класс-помощник. Содержит методы для работы с БД
 */
public class JdbcHelper extends HelperBase {

    private Connection portalConnection;
    private Connection sopranoConnection;

    public JdbcHelper(ApplicationManager manager) {
        super(manager);
        setPortalConnection();
        setSopranoConnection();
    }

    /**
     * Метод устанавливает соединение с БД портала
     */
    public void setPortalConnection() {
        try {
            if (portalConnection == null) {
                portalConnection = DriverManager.getConnection("jdbc:db2://192.168.2.75:50000/EAPOFORM", "db2admin", "Passw0rd");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод устанавливает соединение с БД Soprano
     */
    public void setSopranoConnection() {
        try {
            if (sopranoConnection == null) {
                sopranoConnection = DriverManager.getConnection("jdbc:mysql://192.168.2.197:3306/", "observer", "J]K0VtpN");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод закрывает соединение с БД
     */
    public void closePortalConnection() {
        try {
            portalConnection.close();
            sopranoConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для удаления из БД ПП после теста отправки запросов на регистрацию
     */
    public void pationAgentDeleter() {
        try {
            Statement statement = portalConnection.createStatement();
            int del = statement.executeUpdate("DELETE FROM portaluser WHERE userid IN ('NechaevMA', 'RykovVV', 'KiselevaTS', 'PogosyanAA', 'SubbotinaLA', 'MarkovcevaDV')");
            System.out.printf("Deleted %d agents!\n", del);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для удаления из БД физ и юр лица после теста отправки запросов на регистрацию
     */
    public void personAndOrganisationDeleter() {
        try {
            Statement statement = portalConnection.createStatement();
            int del = statement.executeUpdate("DELETE FROM portaluser WHERE middlename = 'Фейкерович'");
            System.out.printf("Deleted %d persons and organisations!\n", del);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод проверяет запись в БД после теста отправки запроса на регистрацию физ и юр лиц
     */
    public int checkRegisteredEntity(String surname, boolean hasDelay) {
        try {
            String sql = "SELECT COUNT(*) AS CountOfEntities FROM portaluser WHERE lastname = ? AND middlename = 'Фейкерович'";
            PreparedStatement preparedStatement = portalConnection.prepareStatement(sql);
            preparedStatement.setString(1, surname);
            if (hasDelay) {
                TimeUnit.MILLISECONDS.sleep(400);
            }
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            return result.getInt("CountOfEntities");
        } catch (SQLException e) {
            System.out.println("Checking of entity registration is failed!");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод проверяет запись в БД после теста отправки запроса на регистрацию ПП
     */
    public int checkRegisteredAgent() {
        try {
            Statement statement = portalConnection.createStatement();
            ResultSet result = statement.executeQuery("SELECT COUNT(*) AS CountOfEntities FROM portaluser WHERE userid IN ('NechaevMA', 'RykovVV', 'KiselevaTS', 'PogosyanAA', 'SubbotinaLA', 'MarkovcevaDV')");
            result.next();
            return result.getInt("CountOfEntities");
        } catch (SQLException e) {
            System.out.println("Checking of agent registration is failed!");
            throw new RuntimeException(e);
        }
    }


    /**
     * Метод проверяет создание записей в БД Soprano после сохранения заявок на ИЗО и ПО
     */
    public int checkInventionAppInSoprano(String appNumber) {
        try {
            String sql = "SELECT COUNT(*) AS Result " +
                    "FROM " +
                    "(SELECT ptappli.extidappli FROM patent_test.own JOIN patent_test.ptappli ON own.idappli = ptappli.idappli WHERE ptappli.extidappli = ? " +
                    "UNION ALL " +
                    "SELECT ptappli.extidappli FROM patent_test.invent JOIN patent_test.ptappli ON invent.idappli = ptappli.idappli WHERE ptappli.extidappli = ? " +
                    "UNION ALL " +
                    "SELECT ptappli.extidappli FROM patent_test.ptappli WHERE ptappli.extidappli = ?) AS a";
            PreparedStatement preparedStatement = sopranoConnection.prepareStatement(sql);
            for (int i = 1; i <= 3; i++) {
                preparedStatement.setString(i, appNumber);
            }
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            return result.getInt("Result");
        } catch (SQLException e) {
            System.out.println("Checking of entity registration is failed!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод проверяет создание записей в БД Soprano после сохранения досылок по ПО и ИЗО
     */
    public int checkInventionAdditionInSoprano(String appNumber) {
        try {
            String sql = "SELECT COUNT(*) AS Result FROM patent_test.history where idappli = (SELECT idappli FROM patent_test.ptappli where extidappli = ?)";
            PreparedStatement preparedStatement = sopranoConnection.prepareStatement(sql);
            preparedStatement.setString(1, appNumber);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            return result.getInt("Result");
        } catch (SQLException e) {
            System.out.println("Checking of entity registration is failed!");
            throw new RuntimeException(e);
        }
    }
}
