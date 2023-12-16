package dataaccess;

import domain.Student;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLStudentRepository implements MyStudentRepository {

    private Connection con;

    public MySQLStudentRepository() throws SQLException, ClassNotFoundException {
        this.con = con = MySQLDatabaseConnection.getConnection("jdbc:mysql://localhost:6033/kurssystem", "root", "1234");
    }

    @Override
    public Optional<Student> insert(Student entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `student` (`firstname`, `lastname`, `gebDate`) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setDate(3, entity.getGebDate());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return  Optional.empty();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(1);
                if (id != null) {
                    return this.getById(id);
                }
            } else {
                return Optional.empty();
            }
            return Optional.empty();
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Optional<Student> getById(Long id) {
        Assert.notNull(id);
        if (countStudentInDbWithId(id)==0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM `student` WHERE `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                Student student = new Student (
                        resultSet.getLong("id"),
                        resultSet.getString("fistname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("gebDate")
                );
                return Optional.of(student);

            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }
    }

    private int countStudentInDbWithId(Long id) {
        try {
            String countSQL = "SELECT COUNT(*) FROM `student` WHERE `id` = ?";
            PreparedStatement preparedStatement = con.prepareStatement(countSQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSetCount = preparedStatement.executeQuery();
            resultSetCount.next();
            int studentCount = resultSetCount.getInt(1);
            return studentCount;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }

    }

    @Override
    public List<Student> getAll() {
        String sql = "SELECT * FROM `student`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();

            while (resultSet.next()) {
                studentList.add(new Student(
                                resultSet.getLong("id"),
                                resultSet.getString("fistname"),
                                resultSet.getString("lastname"),
                                resultSet.getDate("gebDate")
                        )
                );
            }
            return studentList;

        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public Optional<Student> update(Student entity) {
        Assert.notNull(entity);

        String sql = "UPDATE `student` SET `firstname` = ?, `lastname` = ?, `gebDate` = ? WHERE `student`.`id` = ?";
        if (countStudentInDbWithId(entity.getId())==0) {
            return Optional.empty();
        } else {
            try {

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, entity.getFirstname());
                preparedStatement.setString(2, entity.getLastname());
                preparedStatement.setDate(3, entity.getGebDate());
                preparedStatement.setLong(4, entity.getId());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    return Optional.empty();
                } else {
                    return this.getById(entity.getId());
                }



            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        Assert.notNull(id);
        String sql = "DELETE * FROM `student` WHERE `id` = ?";
        try {
            if (countStudentInDbWithId(id) == 1) {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> searchName(String searchName) {
        try {
            String sql = "SELECT * FROM `student` WHERE LOWER(`firstname`) LIKE LOWER(?) OR LOWER(`lastname`) LIKE LOWER(?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+searchName+"%");
            preparedStatement.setString(2, "%"+searchName+"%");
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Student> studentList = new ArrayList<>();

            while (resultSet.next()) {
                studentList.add(new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("gebDate")
                ));
            }
            return studentList;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }

    }

    @Override
    public List<Student> searchGeb(Date searchDate) {
        try {
            String sql = "SELECT * FROM `student` WHERE LOWER(`gebDate`) LIKE LOWER(?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+searchDate+"%");
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Student> studentList = new ArrayList<>();

            while (resultSet.next()) {
                studentList.add(new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("gebDate")
                ));
            }
            return studentList;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> searchID(Date searchID) {
        try {
            String sql = "SELECT * FROM `student` WHERE LOWER(`id`) LIKE LOWER(?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+searchID+"%");
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Student> studentList = new ArrayList<>();

            while (resultSet.next()) {
                studentList.add(new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("gebDate")
                ));
            }
            return studentList;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

}
