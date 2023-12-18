package dataaccess;

import domain.Student;
import java.sql.Date;
import java.util.List;

public interface MyStudentRepository extends BaseRepository<Student, Long> {

    List<Student> searchName(String searchName);
    List<Student> searchGeb(String searchDate);
    List<Student> searchGebBetween(String fist, String second);
}
