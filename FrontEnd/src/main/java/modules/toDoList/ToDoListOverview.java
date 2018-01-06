package main.java.modules.toDoList;

import main.java.elements.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.modules.Module;
import main.java.modules.toDoList.model.Task;
import main.java.util.Validator;

import java.time.LocalDate;

public class ToDoListOverview extends Module {
    private Stage window;
    private TableView<Task> table;
    private TextField name, effort;
    private Validator validator = new Validator();
    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    public void display(Stage primaryStage, Scene previousScene) {

        window = setWindowProperties(primaryStage, "to do list", 1000, 500);


        Button returnButton = new Button("return");
        returnButton.setOnAction(ae -> primaryStage.setScene(previousScene));

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(returnButton);

        setUpTable();
        name = getTextField("textField");
        effort = getTextField("effort");
        Button addTaskButton = new Button("add task");
        addTaskButton.setOnAction(e -> addTask());
        HBox fieldsAndAddButton = new HBox();
        fieldsAndAddButton.getChildren().addAll(name,effort, addTaskButton);

        VBox centreDivider = new VBox();
        centreDivider.getChildren().addAll(table, fieldsAndAddButton, buttonBar);


        BorderPane mainlayout = new BorderPane();

        mainlayout.setCenter(centreDivider);


        Scene toDoListScene = new Scene(mainlayout);
        window.setScene(toDoListScene);
        window.show();
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
        return tasks;
    }

    private void addTask() {
        String nameText = name.getText();
        if (effort.getText() == null || effort.getText().trim().isEmpty() || !validator.isNumeric(effort.getText())) {
            AlertBox.display("error", "effort must be numeric and non empty");
            return;
        }
        int effortInt = Integer.parseInt(effort.getText());
        Task task = new Task(0, nameText, effortInt, LocalDate.now(), null);
        tasks.addAll(task);
    }

}
