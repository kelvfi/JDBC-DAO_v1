import dataaccess.MySQLDatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world");

        try {
            Connection myConnection =
                    MySQLDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
            System.out.println("Verbindung aufgebaut");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
