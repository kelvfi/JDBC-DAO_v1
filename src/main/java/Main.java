import dataaccess.MySQLCourseRepository;
import dataaccess.MySQLDatabaseConnection;
import domain.Student;
import ui.Cli;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            Cli myCli = new Cli(new MySQLCourseRepository());
            myCli.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: "+e.getMessage()+" SQL-State: "+e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbankfehler: " + e.getMessage());
        }


        // Student student = new Student(1L, "Kelvin", "Fiegl", new Date(2003-07-22));
    }
}
