package modules.toDoList;

import acces.GeneralDAO;
import elements.AlertBox;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ToDoListDao extends GeneralDAO {
    private Connection connection = getConnection();


    public boolean InsertTask(Task task) {
        String sql = "INSERT INTO task VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, incrementAndGetId("task"));
            statement.setInt(2, task.getEffort());
            statement.setString(3, task.getName());
            statement.setString(4, task.getStartDate().toString());
            statement.setString(5, "");
            return statement.execute();
        } catch (SQLException e) {
            AlertBox.display("error", e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void updateTask(Task task) throws SQLException {
        String sql = "UPDATE task SET date_of_completion =  ? where id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, LocalDate.now().toString());
            statement.setLong(2, task.getOid());
            statement.execute();
        } catch (SQLException e) {
            System.out.println("error = " + e.getMessage());
            throw new SQLException(e);
        }
    }



    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task";
        try (Statement stmnt = connection.createStatement();
             ResultSet rs = stmnt.executeQuery(sql)) {
            while (rs.next()) {
                Task tsk = new Task();
                tsk.setOid(rs.getInt(1));
                tsk.setEffort(rs.getInt(2));
                tsk.setName(rs.getString(3));
                tsk.setStartDate(LocalDate.parse(rs.getString(4)));
                tsk.setDateOfCompletion(rs.getString(5).trim().isEmpty()? null :
                        LocalDate.parse(rs.getString(5)));
                tasks.add(tsk);
            }
        } catch (SQLException e) {
            AlertBox.display("error", e.getMessage());
            e.printStackTrace();
        }
        return tasks;
    }

    public void deleteTask(Task task) {
        String sql = "DELETE FROM task WHERE oid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, task.getOid());
            stmt.execute();
        } catch (SQLException e) {
            AlertBox.display("error", e.getMessage());
        }
    }
}
