package manager;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class JdbcHelper extends HelperBase {

    private Connection portalConnection;

    public JdbcHelper(ApplicationManager manager) {
        super(manager);
        setPortalConnection();
    }

    public Connection setPortalConnection() {
        try {
            if (portalConnection == null) {
                Class.forName("com.ibm.db2.jcc.DB2Driver");
                portalConnection = DriverManager.getConnection("jdbc:db2://192.168.2.75:50000/EAPOFORM", "db2admin", "Passw0rd");
            }
            return portalConnection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void closePortalConnection() {
        try {
            portalConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void pationAgentDeleter() {
        try {
            Statement statement = portalConnection.createStatement();
            int del = statement.executeUpdate("DELETE FROM portaluser WHERE userid IN ('NechaevMA', 'RykovVV', 'KiselevaTS', 'PogosyanAA', 'SubbotinaLA', 'MarkovcevaDV')");
            System.out.printf("Deleted %d agents!\n", del);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void personAndOrganisationDeleter() {
        try {
            Statement statement = portalConnection.createStatement();
            int del = statement.executeUpdate("DELETE FROM portaluser WHERE middlename = 'Фейкерович'");
            System.out.printf("Deleted %d persons and organisations!\n", del);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int checkRegisteredEntity(String surname, boolean hasDelay) {
        try {
            String sql = "SELECT COUNT(*) as CountOfEntities FROM portaluser WHERE lastname = ? AND middlename = 'Фейкерович'";
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

    public int checkRegisteredAgent() {
        try {
            Statement statement = portalConnection.createStatement();
            ResultSet result = statement.executeQuery("SELECT COUNT(*) as CountOfEntities FROM portaluser WHERE userid IN ('NechaevMA', 'RykovVV', 'KiselevaTS', 'PogosyanAA', 'SubbotinaLA', 'MarkovcevaDV')");
            result.next();
            return result.getInt("CountOfEntities");
        } catch (SQLException e) {
            System.out.println("Checking of agent registration is failed!");
            throw new RuntimeException(e);
        }
    }


}
