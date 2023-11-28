package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabaseConnection {
    private static Connection con = null;

    // new funktioniert nicht mehr da wir es auf Privat gesetzt haben
    private MySQLDatabaseConnection() {

    }

    /**
     * Datenbank verbindung wird aufgebaut
     *
     * @param url
     * @param user
     * @param pwd
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection(String url, String user, String pwd) throws ClassNotFoundException, SQLException { // Falls der Driver nocht nicht da dann schmeißt es eine Exception | SQL Exception
        if(con!=null) {
            return con;
        } else {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pwd);
            return con;
        }
    }
}
