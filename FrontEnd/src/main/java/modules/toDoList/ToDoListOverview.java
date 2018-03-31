package modules.toDoList;

import elements.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import modules.Module;
import util.ExcelService;
import util.Validator;

import java.time.LocalDate;
import java.util.List;

public class ToDoListOverview extends Module {
    private TableView<Task> table;
    private TextField nameTextField, effortTextField;
    private Validator validator = new Validator();
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private final ToDoListDao dao = new ToDoListDao();
    private final ExcelService excelService = new ExcelService();
    private final Button exportButton = new Button("export");

    public BorderPane display() {
        ButtonBar buttonBar = new ButtonBar();


        setUpTable();
        nameTextField = getTextField("textField");
        effortTextField = getTextField("effortTextField");
        Button addTaskButton = new Button("add task");
        addTaskButton.setOnAction(e -> addTask());
        Button completeButton = new Button("complete");
        completeButton.setOnAction(e -> completeTask(table.getSelectionModel().getSelectedItem()));
        exportButton.setOnAction(e -> toExcell());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            dao.deleteTask(table.getSelectionModel().getSelectedItem());
            table.setItems(getTasks());
        });

        VBox centreDivider = new VBox();

        buttonBar.getButtons().addAll(nameTextField, effortTextField, addTaskButton, deleteButton, completeButton, exportButton);
        centreDivider.getChildren().addAll(table, buttonBar);


        BorderPane mainlayout = new BorderPane();

        mainlayout.setCenter(centreDivider);

        return mainlayout;
    }

    private void toExcell() {
        excelService.toExcell(getTasks(), "tasks.csv");
    }

    private void setUpTable() {
        table = new TableView<>();
        TableColumn<Task, String> name = addColumn("name");
        TableColumn<Task, Integer> effort = addColumn("effort");
        TableColumn<Task, LocalDate> start = addColumn("startDate");
        TableColumn<Task, LocalDate> finished = addColumn("dateOfCompletion");
        table.setItems(getTasks());
        table.getColumns().addAll(name, effort, start, finished);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private ObservableList<Task> getTasks() {
        List<Task> storedTasks = dao.getTasks();
        tasks.removeAll(tasks);
        tasks.addAll(storedTasks);
        return tasks;
    }

    private void completeTask(Task task) {
        try {
            dao.updateTask(task);
            table.setItems(getTasks());
        } catch (Exception e) {
            AlertBox.display("error while updating task: %s", e.getMessage());
        }


    }

    private void addTask() {
        String nameText = nameTextField.getText();
        if (effortTextField.getText() == null || effortTextField.getText().trim().isEmpty() || !validator.isNumeric(effortTextField.getText())) {
            AlertBox.display("error", "effortTextField must be numeric and non empty");
            return;
        }
        int effortInt = Integer.parseInt(effortTextField.getText());
        Task task = new Task(0, nameText, effortInt, LocalDate.now(), null);
        dao.InsertTask(task);
        table.setItems(getTasks());
    }

}
