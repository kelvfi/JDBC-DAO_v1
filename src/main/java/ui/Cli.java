package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import dataaccess.MySQLCourseRepository;
import domain.Course;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Cli {

    MyCourseRepository repo;
    Scanner scan;

    public Cli(MySQLCourseRepository repo) { // Zugriff gamapt
        this.scan = new Scanner(System.in);
        this.repo = repo;
    }

    public void start() {

        String input = "-";
        while (!input.equals("x")) {

            showMenue(); // Zeigt das Menü an
            input = scan.nextLine();
            switch (input) {
                case "1":
                    System.out.println("Kurs eingabe");
                    break;
                case "2":
                    showAllCourses();
                    break;
                case "3":
                    showCourseDetails();
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen!");
                    break;
                default:
                    inputError(); // Default Wert, falls etwas Falsches eingegeben wurde
                    break;
            }
        }
        scan.close();
    }

    private void showCourseDetails() {
        System.out.println("Für welchen Kurs möchten sie die Kursdetails anzeigen?");
        Long courseId = Long.parseLong(scan.nextLine());

        try {
            Optional<Course> courseOptional = repo.getById(courseId);
            if (courseOptional.isPresent()) {
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kurs mit der ID: "+courseId+ " nicht gefunden!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Detail-Anfrage: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Detail-Anzeige: "+exception.getMessage());
        }
    }

    private void showAllCourses() {
        List<Course> list = null;

        try {
            list = repo.getAll();
            if (list.size() > 0) {
                for (Course course : list) { // Mapping generiert durch "Course" | Wie ein riesen speicher fungiert es.
                    System.out.println(course);
                }
            } else {
                System.out.println("Kursliste leer!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter fehler bei Anzeige aller Kurse: "+exception.getMessage());
        }
    }

    private void showMenue() {
        System.out.println("-------------------------------- KURSMANAGEMENT --------------------------------");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t"+"(3) Kursdatails anzeigen");
        System.out.println("(x) ENDE");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen eingeben die gegeben sind!");
    }
}

