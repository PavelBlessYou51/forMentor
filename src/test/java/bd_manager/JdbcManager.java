package bd_manager;

import java.sql.*;

public class JdbcManager {

    public static void main(String[] args) {
        pationAgentDeleter();
    }

    public static void pationAgentDeleter() {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            Connection conn = DriverManager.getConnection("jdbc:db2://192.168.2.75:50000/EAPOFORM", "db2admin", "Passw0rd");
            PreparedStatement del = conn.prepareStatement("DELETE FROM portaluser WHERE userid IN ('NechaevMA', 'RykovVV', 'KiselevaTS', 'PogosyanAA', 'SubbotinaLA', 'MarkovcevaDV')");
            del.execute();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

}
