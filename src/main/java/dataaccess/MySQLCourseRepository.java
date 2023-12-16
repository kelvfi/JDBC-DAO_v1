package dataaccess;

import domain.Course;
import domain.CourseType;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLCourseRepository implements MyCourseRepository {

    private Connection con;

    public MySQLCourseRepository() throws SQLException, ClassNotFoundException {
        this.con = MySQLDatabaseConnection.getConnection("jdbc:mysql://localhost:6033/kurssystem", "root", "1234");
    }

    @Override
    public Optional<Course> insert(Course entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `courses` (`name`, `description`, `hours`, `begindate`, `enddate`, `coursetype`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName()); // Holen aus den kurs ein Statement heraus und belegen somit das SQL Statement
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getHours());
            preparedStatement.setDate(4, entity.getBeginDate());
            preparedStatement.setDate(5, entity.getEndDate());
            preparedStatement.setString(6, entity.getCourseType().toString()); // Enumerationstyp deswegen to String

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return Optional.empty();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(1);
                if (id !=  null) {
                    return this.getById(id);
                }
            } else {
                return Optional.empty();
            }

        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Course> getById(Long id) {
        Assert.notNull(id);
        if (countCoursesInDbWithId(id)==0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM `courses` WHERE `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                Course course = new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                );
                return Optional.of(course);

            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }
    }

    private int countCoursesInDbWithId(Long id) {
        try {
            String countSQL = "SELECT COUNT(*) FROM `courses` WHERE `id` = ?";
            PreparedStatement preparedStatement = con.prepareStatement(countSQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSetCount = preparedStatement.executeQuery();
            resultSetCount.next();
            int courseCount = resultSetCount.getInt(1);
            return courseCount;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }

    }

    @Override
    public List<Course> getAll() {
        String sql = "SELECT * FROM `courses`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();

            while (resultSet.next()) {
                courseList.add(new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                        )
                );
            }
            return courseList;

        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public Optional<Course> update(Course entity) {

        Assert.notNull(entity);

        String sql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";
        if (countCoursesInDbWithId(entity.getId())==0) {
            return Optional.empty();
        } else {
            try {

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                preparedStatement.setInt(3, entity.getHours());
                preparedStatement.setDate(4, entity.getBeginDate());
                preparedStatement.setDate(5, entity.getEndDate());
                preparedStatement.setString(6, entity.getCourseType().toString());
                preparedStatement.setLong(7, entity.getId());

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
        String sql = "DELETE * FROM `courses` WHERE `id` = ?";
        try {
            if (countCoursesInDbWithId(id) == 1) {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Course> findAllCoursesByName(String name) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByDescription(String description) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByNameOrDescription(String searchText) {
        try {
            // SQL Statement zum suchen von Kursen bei name und description alles LOWERCASE
            String sql = "SELECT * FROM `courses` WHERE LOWER(`description`) LIKE LOWER(?) OR LOWER(`name`) LIKE LOWER(?)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+searchText+"%");
            preparedStatement.setString(2, "%"+searchText+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();

            // Alles ausgeben was hier gefunden wurde
            while (resultSet.next()) {
                courseList.add(new Course(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getInt("hours"),
                                resultSet.getDate("begindate"),
                                resultSet.getDate("enddate"),
                                CourseType.valueOf(resultSet.getString("coursetype"))
                        )
                );
            }
            return courseList;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Course> findAllCourseType(String courseType) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByStartDate(Date startDate) {
        return null;
    }

    @Override
    public List<Course> findAllRunningCourses() {
        return null;
    }
}
