package domain;

import java.sql.Date;

public class Student extends BaseEntity {

    private String firstname;
    private String lastname;
    private Date gebDate;

    public Student(Long id, String firstname, String lastname, Date gebDate) {
        super(id);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setGebDate(gebDate);
    }

    public Student(String firstname, String lastname, Date gebDate) {
        super(null);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setGebDate(gebDate);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        if (firstname == null) {
            this.firstname = firstname;
        } else {
            throw new InvalidValueException("Vorname darf nicht leer sein!");
        }

    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        if (lastname == null) {
            this.lastname = lastname;
        } else {
            throw new InvalidValueException("Nachname darf nicht leer sein!");
        }
    }

    public Date getGebDate() {
        return gebDate;
    }

    public void setGebDate(Date gebDate) {
        if (gebDate != null) {
            if (gebDate.before(new Date(System.currentTimeMillis()))) {
                this.gebDate = gebDate;
            } else {
                throw new InvalidValueException("Geburtstag muss heute oder in der Vergangenheit sein!");
            }
        } else {
            throw new InvalidValueException("Geburtsdatum darf nicht leer sein!");
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", gebDate=" + gebDate +
                '}';
    }
}
