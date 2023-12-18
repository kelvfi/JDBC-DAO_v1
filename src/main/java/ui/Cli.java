package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import dataaccess.MySQLCourseRepository;
import dataaccess.MyStudentRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;
import domain.Student;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Cli {

    MyCourseRepository repo;
    MyStudentRepository sepo;
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
                    //addCourse(); FUNKTIONIERT NICHT
                    break;
                case "2":
                    showAllCourses();
                    break;
                case "3":
                    showCourseDetails();
                    break;
                case "4":
                    updateCourseDetails();
                    break;
                case "5":
                    deleteCourse();
                    break;
                case "6":
                    courseSearch();
                case "7":
                    runningCourses();
                    break;
                case "a":
                    addStudent();
                    break;
                case "b":
                    showAllStudents();
                    break;
                case "c":
                    showStudentDetails();
                    break;
                case "d":
                    updateStudent();
                    break;
                case "e":
                    deleteStudent();
                    break;
                case "f":
                    searchName();
                    break;
                case "g":
                    searchGeb();
                    break;
                case "h":
                    searchGebBetween();
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

    private void searchGebBetween() {
        System.out.println("Bitte geben sie das erste Datum ein: ");
        Date first = Date.valueOf(scan.nextLine());

        System.out.println("Bitte geben sie das zweite Datum ein: ");
        Date second = Date.valueOf(scan.nextLine());

        try {
            sepo.searchGebBetween(first, second);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Suche: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
    }

    private void searchGeb() {
        System.out.println("Bitte geben die das Datum ein (YYYY-MM-DD): ");
        Date datum = Date.valueOf(scan.nextLine());

        try {
            sepo.searchGeb(datum);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Suche: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
    }

    private void searchName() {
        System.out.println("Bitte geben sie den Namen ein:");
        String name = scan.nextLine();

        try {
            sepo.searchName(name);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Suche: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
    }

    private void deleteStudent() {
        System.out.println("Welchen Student möchten sie Löschen bitte ID eingeben: ");
        Long studentIdToDelete = Long.parseLong(scan.nextLine());

        try {
            sepo.deleteById(studentIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim löschen: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
    }

    private void updateStudent() {
        System.out.println("Für welche Student-ID möchten sie die Details ändern?");
        Long courseId = Long.parseLong(scan.nextLine());

        try {
            Optional<Student> studentOptional = sepo.getById(courseId);
            if (studentOptional.isEmpty()) {
                System.out.println("Student mit der Gegebenen ID nicht in der Datenbank!");
            } else {
                Student student = studentOptional.get();

                System.out.println("Änderungen für folgenden Student: ");
                System.out.println(student);

                String firstname, lastname, gebDate;

                System.out.println("Bitte neue Kursdaten angeben (ENTER, falls keine Änderung gewünscht ist!)");
                System.out.println("Vorname: ");
                firstname = scan.nextLine();
                System.out.println("Nachname: ");
                lastname = scan.nextLine();
                System.out.println("Geburtsdatum (YYYY-MM-DD): ");
                gebDate = scan.nextLine();

                Optional<Student> optionalStudentUpdated = sepo.update(
                        new Student(
                                student.getId(),
                                firstname.equals("") ? student.getFirstname() : firstname,
                                lastname.equals("") ? student.getLastname() : lastname,
                                gebDate.equals("") ? student.getGebDate() : Date.valueOf(gebDate)
                        )
                );

                // Man gibt hier 2x Funktionen mit also wie mit if/else
                optionalStudentUpdated.ifPresentOrElse(
                        (c)-> System.out.println("Student Aktuallisiert" + c),
                        ()-> System.out.println("Student konnte nicht Aktuallisiert werden!")
                );
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: "+illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: "+invalidValueException.getMessage()); // Z.B. Enddatum vor Beginndatum
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
    }

    private void addStudent() {
        String firstname, lastname;
        Date gebDate;

        try {
            System.out.println("Bitte alle Studentendaten angeben: ");

            System.out.println("Vorname: ");
            firstname = scan.nextLine();
            if (firstname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Nachname: ");
            lastname = scan.nextLine();
            if (lastname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Geburtsdatum (YYYY-MM-DD): ");
            gebDate = Date.valueOf(scan.nextLine());

            Optional<Student> studentOptional = sepo.insert(
                    new Student(firstname,lastname,gebDate)
            );

            if (studentOptional.isPresent()) {
                System.out.println("Student angelegt: "+studentOptional.get());
            } else {
                System.out.println("Student konnte nicht angelegt werden.");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: "+illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Studentendaten nicht korrekt angegeben: "+invalidValueException.getMessage()); // Z.B. Enddatum vor Beginndatum
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
    }

    private void showStudentDetails() {
        System.out.println("Für welchen Kurs möchten sie die Studentendetails anzeigen?");
        Long studentID = Long.parseLong(scan.nextLine());

        try {
            Optional<Student> studentOptional = sepo.getById(studentID);
            if (studentOptional.isPresent()) {
                System.out.println(studentOptional.get());
            } else {
                System.out.println("Student mit der ID: "+studentID+ " nicht gefunden!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Detail-Anfrage: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Detail-Anzeige: "+exception.getMessage());
        }
    }

    private void showAllStudents() {
        List<Student> list = null;

        try {
            list = sepo.getAll();
            if (list.size() > 0) {
                for (Student student : list) { // Mapping generiert durch "Course" | Wie ein riesen speicher fungiert es.
                    System.out.println(student);
                }
            } else {
                System.out.println("Studentenliste leer!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Studenten: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter fehler bei Anzeige aller Studenten: "+exception.getMessage());
        }
    }

    private void runningCourses() {
        System.out.println("Aktuell laufende Kurse: ");
        List<Course> list;

        try {
            list = repo.findAllRunningCourses();

            for (Course course : list) {
                System.out.println(course);
            }

        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Anzeige für laufende Kurse: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter fehler bei Kurs-Anzeige für laufende Kurse: "+exception.getMessage());
        }
    }

    // Noch testen
    private void courseSearch() {
        System.out.println("Geben Sie einen Suchbegriff an!");
        String searchString = scan.nextLine();
        List<Course> courseList;

        try {
            courseList = repo.findAllCoursesByNameOrDescription(searchString);

            for (Course course : courseList) {
                System.out.println(course);
            }

        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kussuche: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei der Kurssuche: "+exception.getMessage());
        }
    }

    private void deleteCourse() {
        System.out.println("Welchen Kurs möchten sie Löschen bitte ID eingeben: ");
        Long courseIdToDelete = Long.parseLong(scan.nextLine());

        try {
            repo.deleteById(courseIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim löschen: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
    }

    private void updateCourseDetails() {
        System.out.println("Für welche Kurs-ID möchten sie die Details ändern?");
        Long courseId = Long.parseLong(scan.nextLine());

        try {
            Optional<Course> courseOptional = repo.getById(courseId);
            if (courseOptional.isEmpty()) {
                System.out.println("Kurs mit der Gegebenen ID nicht in der Datenbank!");
            } else {
                Course course = courseOptional.get();

                System.out.println("Änderungen für folgenden Kurs: ");
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten angeben (ENTER, falls keine Änderung gewünscht ist!)");
                System.out.println("Name: ");
                name = scan.nextLine();
                System.out.println("Beschreibung: ");
                description = scan.nextLine();
                System.out.println("Stundenanzahl: ");
                hours = scan.nextLine();
                System.out.println("Startdatum (YYYY-MM-DD): ");
                dateFrom = scan.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD): ");
                dateTo = scan.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/OE)");
                courseType = scan.nextLine();

                Optional<Course> optionalCourseUpdated = repo.update(
                        new Course(
                                course.getId(),
                                name.equals("") ? course.getName() : name,
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? course.getBeginDate() : Date.valueOf(dateFrom),
                                dateTo.equals("") ? course.getEndDate() : Date.valueOf(dateTo),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        )
                );

                // Man gibt hier 2x Funktionen mit also wie mit if/else
                optionalCourseUpdated.ifPresentOrElse(
                        (c)-> System.out.println("Kurs Aktuallisiert" + c),
                        ()-> System.out.println("Kurs konnte nicht Aktuallisiert werden!")
                );
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: "+illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: "+invalidValueException.getMessage()); // Z.B. Enddatum vor Beginndatum
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
    }

    private void addCourse() {

        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;

        try {
            System.out.println("Bitte alle Kursdaten angeben: ");

            System.out.println("Name: ");
            name = scan.nextLine();
            if (name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Beschreibung: ");
            description = scan.nextLine();
            if (description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Stundenanzahl: ");
            hours = Integer.parseInt(scan.nextLine());
            if (hours <= 0) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");

            System.out.println("Startdatum (YYYY-MM-DD): ");
            dateFrom = Date.valueOf(scan.nextLine());

            System.out.println("Enddatum (YYYY-MM-DD): ");
            dateTo = Date.valueOf(scan.nextLine());

            System.out.println("Kurstyp (ZA/BF/FF/OE): ");
            courseType = CourseType.valueOf(scan.nextLine());

            Optional<Course> optionalCourse = repo.insert(
                    new Course( name, description, hours, dateFrom, dateTo, courseType)
            );

            if (optionalCourse.isPresent()) {
                System.out.println("Kurs angelegt: "+optionalCourse.get());
            } else {
                System.out.println("Kurs konnte nicht angelegt werden.");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: "+illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: "+invalidValueException.getMessage()); // Z.B. Enddatum vor Beginndatum
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: "+databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler: "+exception.getMessage());
        }
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
        System.out.println("---------------------------------- KURSMANAGEMENT ---------------------------------");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t"+"(3) Kursdatails anzeigen");
        System.out.println("(4) Kursdetails ändern \t (5) Kurs löschen \t"+"(6) Kurssuche");
        System.out.println("(7) Laufende Kurse");
        System.out.println("(x) ENDE");
        System.out.println("--------------------------------- STUDENTMANAGEMENT --------------------------------");
        System.out.println("(a) Student eingeben \t (b) Alle Studenten anzeigen \t"+"(c) Studentendetails anzeigen");
        System.out.println("(d) Studentendetails ändern \t (e) Student löschen \t"+ "(f) Nach Namen suchen");
        System.out.println("(g) Nach Geburtsdatum suchen \t (h) Nach Geburtsdatum zwischen zwischen zwei Daten suchen \t");
        System.out.println("(x) ENDE");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen eingeben die gegeben sind!");
    }
}

