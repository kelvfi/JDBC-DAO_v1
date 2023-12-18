package dataaccess;

import domain.Student;
import java.sql.Date;
import java.util.List;

public interface MyStudentRepository extends BaseRepository<Student, Long> {

    List<Student> searchName(String searchName);
    List<Student> searchGeb(Date searchDate);
    List<Student> searchGebBetween(Date fist, Date second);
}
