package bd_manager;

import java.sql.*;

public class JdbcManager {

    public static void main(String[] args) {
        pationAgentDeleter();
    }

    public static void pationAgentDeleter() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.2.75:50000/EAPOFORM", "root", "root");
             Statement stmt = conn.createStatement();
        ) {
            ResultSet rs = stmt.executeQuery("DELETE FROM portaluser WHERE agentid IN (589, 587, 586, 111, 112, 113)");
            System.out.println(rs);
            System.out.println("Hello");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
