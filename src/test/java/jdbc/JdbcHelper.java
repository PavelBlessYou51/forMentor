package jdbc;

import fixture.ConfigProvider;

import java.sql.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * Класс-помощник. Содержит методы для работы с БД
 */
public class JdbcHelper {

    private Connection portalConnection;
    private Connection sopranoConnection;
    private Connection madrasConnection;

    public JdbcHelper() {
        setPortalConnection();
        setSopranoConnection();
        setMadrasConnection();
    }


    /**
     * Метод устанавливает соединение с БД портала
     */
    public void setPortalConnection() {
        try {
            if (portalConnection == null) {
                portalConnection = DriverManager.getConnection(ConfigProvider.getPortalUrl(), ConfigProvider.getPortalLogin(), ConfigProvider.getPortalPassword());
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
                sopranoConnection = DriverManager.getConnection(ConfigProvider.getSopranoUrl(), ConfigProvider.getSopranoLogin(), ConfigProvider.getSopranoPassword());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод устанавливает соединение с БД Madras
     */
    public void setMadrasConnection() {
        try {
            if (madrasConnection == null) {
                madrasConnection = DriverManager.getConnection(ConfigProvider.getMadrasUrl(), ConfigProvider.getMadrasLogin(), ConfigProvider.getMadrasPassword());
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
            int del = statement.executeUpdate("DELETE FROM portaluser WHERE userid IN ('NechaevMA', 'RykovVV', 'KiselevaTS', 'PogosyanAA', 'SubbotinaLA', 'MarkovtsevaDV')");
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
            ResultSet result = statement.executeQuery("SELECT COUNT(*) AS CountOfEntities FROM portaluser WHERE userid IN ('NechaevMA', 'RykovVV', 'KiselevaTS', 'PogosyanAA', 'SubbotinaLA', 'MarkovtsevaDV')");
            result.next();
            return result.getInt("CountOfAgents");
        } catch (SQLException e) {
            System.out.println("Checking of agent registration is failed!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращает количество записей в БД
     */
    public int getNumberOfPortalUserEntries(boolean hasDelay) {
        try {
            if (hasDelay) {
                TimeUnit.MILLISECONDS.sleep(400);
            }
            Statement statement = portalConnection.createStatement();
            ResultSet result = statement.executeQuery("SELECT COUNT(*) AS CountOfAgents FROM portaluser");
            result.next();
            return result.getInt("CountOfAgents");
        } catch (SQLException e) {
            System.out.println("Getting Number of entries is failed");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
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

    /**
     * Метод для получения номера PCT заявки для дальнейшей подачи заявки
     */
    public String getPCTData() {
        try {
            String sql = "SELECT NOPCTEP FROM patent_test.pctref\n" +
                    "WHERE DTPCTAPPLI > '2010-01-01'\n" +
                    "ORDER BY DTPCTAPPLI\n" +
                    "LIMIT 1;";
            Statement statement = sopranoConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            return resultSet.getString(1);
        } catch (SQLException e) {
            System.out.println("Getting PCT number is failed!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для удаления записи о PCT заявки из pctref
     */
    public void deletePCTRecord(String PCTNumber) {
        try {
            String sql = "DELETE\n" +
                    "FROM patent_test.pctref\n" +
                    "WHERE NOPCTEP = ?";
            PreparedStatement preparedStatement = sopranoConnection.prepareStatement(sql);
            preparedStatement.setString(1, PCTNumber);
            int result = preparedStatement.executeUpdate();
            if (result != 1) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("Deleting PCT number is failed!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращает заявку для подачи выделенной заявки на ИЗО
     */
    public String getInventionApp() {
        try {
            Statement statement = sopranoConnection.createStatement();
            ResultSet result = statement.executeQuery("SELECT EXTIDAPPLI\n" +
                    "FROM patent_test.ptappli p\n" +
                    "JOIN patent_test.apply a\n" +
                    "ON p.IDAPPLI = a.IDAPPLI\n" +
                    "WHERE a.IDPERSON = 14157 AND EXTIDAPPLI REGEXP '.{4}9.{4}'\n" +
                    "ORDER BY EXTIDAPPLI DESC\n" +
                    "LIMIT 1");
            result.next();
            return result.getString("EXTIDAPPLI");
        } catch (SQLException e) {
            System.out.println("Getting of application for sending an allocated application is failed!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращает количество сохраненных в Madras документов по заявке
     */
    public int checkDocsInMadras(String appNumber) {
        try {
            String sql = "SELECT COUNT(*) AS Result\n" +
                    "FROM tph035_package t35 \n" +
                    "INNER JOIN tph014_document t14 ON t14.docpckkey = t35.pckkey \n" +
                    "LEFT JOIN tph013_docctl t13 ON t14.DOCDCTKEY = t13.DCTKEY \n" +
                    "LEFT JOIN tph016_docnote t16 ON t16.DNTDOCKEY = t14.DOCKEY\n" +
                    "WHERE t35.PCKORIAPPNUMBER = ? AND t35.PCKDATEFORMAL = CURDATE()\n" +
                    "ORDER BY pckdateformal, pckseqnumber";
            PreparedStatement preparedStatement = madrasConnection.prepareStatement(sql);
            preparedStatement.setString(1, appNumber);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            return result.getInt("Result");
        } catch (SQLException e) {
            System.out.println("Checking of entity registration is failed!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращает количество сохраненных в Soprano приоритетов по указанной заявке
     */
    public int checkInventionAppPriorities(String appNumber) {
        try {
            String sql = "SELECT COUNT(*) as Result FROM patent_test.priority where idappli = (select idappli from patent_test.ptappli where extidappli = ?)";
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

    /**
     * Метод возвращает номер заявки для тестов передачи прав\изменение адреса\наименования.
     * Статусы для изменений по заявке lgstappli in (60,71,72,75)
     * Статусы для изменений по патенту lgstappli in (100, 101)
     */
    public String getAppNumberForChanges(boolean isApp) {
        String queryCountRows;
        String queryAppNumber;
        int randomAppNumber;
        try {
            if (isApp) {
                queryCountRows = "select COUNT(*) as Result from patent_test.ptappli where lgstappli in (60,71,72,75)";
            } else {
                queryCountRows = "select COUNT(*) as Result from patent_test.ptappli where lgstappli in (100, 101)";
            }
            Statement statement = sopranoConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryCountRows);
            resultSet.next();
            randomAppNumber = new Random().nextInt(resultSet.getInt("Result")) + 1;
            if (isApp) {
                queryAppNumber = "select extidappli as Result from patent_test.ptappli where lgstappli in (60,71,72,75)";
            } else {
                queryAppNumber = "select extidappli as Result from patent_test.ptappli where lgstappli in (100, 101)";
            }
            statement = sopranoConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(queryAppNumber);
            resultSet.absolute(randomAppNumber);
            return resultSet.getString("Result");
        } catch (SQLException e) {
            System.out.println("Have no any apps for changes!");
            throw new RuntimeException(e);
        }
    }
}
