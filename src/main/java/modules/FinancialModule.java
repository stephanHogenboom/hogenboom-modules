package modules;

import elements.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.model.Category;
import modules.model.FinancialEntry;
import util.Validator;

import java.time.LocalDateTime;

public class FinancialModule {

    Stage window;
    TableView<FinancialEntry> table;
    Button addButton, deleteButton, editButton;
    TextField nameInput, valueInput;
    ComboBox<String> categories;
    Validator validator = new Validator();

    public void display(Stage primaryStage) {
        window = primaryStage;
        window.setMinWidth(500);
        window.setMaxHeight(500);



        BorderPane layout = new BorderPane();

        VBox excellLayout = new VBox();


        //name column
        TableColumn<FinancialEntry, String> nameColumn = addColumn("name");
        //Category column
        TableColumn<FinancialEntry, Category> categoryColumn = addColumn("category");
        //valueColumn
        TableColumn<FinancialEntry, Double> valueColumn = addColumn("value");
        //DateColumn
        TableColumn<FinancialEntry, LocalDateTime> dateColumn = addColumn("date");


        //initializeTable
        table = new TableView<>();
        table.setItems(getFinancialEntries());
        table.getColumns().addAll(nameColumn, categoryColumn, valueColumn, dateColumn);


        HBox inputAndButtonsBox = new HBox();

        nameInput = new TextField();
        nameInput.setPromptText("name");

        valueInput = new TextField();
        valueInput.setPromptText("value");

        categories = new ComboBox<>();
        categories.setEditable(true);
        categories.setPromptText("category");


        /* create buttons
         */
        addButton = new Button("add");
        addButton.setOnAction(e -> addEntry());

        inputAndButtonsBox.getChildren().addAll(nameInput, categories, valueInput, addButton);
        excellLayout.getChildren().addAll(table, inputAndButtonsBox);
        layout.setCenter(excellLayout);
        Scene financialScreen = new Scene(layout);
        window.setScene(financialScreen);
        window.show();
    }

    private TableColumn addColumn(String name) {
        TableColumn<FinancialEntry, ?> column = new TableColumn<>(name);
        column.setMinWidth(200);
        column.setCellValueFactory(new PropertyValueFactory<>(name));
        return column;
    }

    private ObservableList<FinancialEntry> getFinancialEntries() {
        ObservableList<FinancialEntry> entries = FXCollections.observableArrayList();
        entries.add(new FinancialEntry("test", new Double(200.0) , new Category("test", 1)));
        return entries;
    }

    private boolean addEntry() {
        String name = nameInput.getText();
        String valueString = valueInput.getText();
        String categoryString = categories.getSelectionModel().getSelectedItem();
        if (name == null || valueString == null || categoryString == null) {
            AlertBox.display("Invalid data", String.format("missing data: name=%s, value=%s, categorie=%s", name, valueString, categoryString));
            return false;
        }
        if (!validator.isNumeric(valueString)) {
            AlertBox.display("Invalid data", String.format(" data for value is not numeric: value=%s", valueString));
            return false;
        }
        Double value = Double.parseDouble(valueString);
        Category category = new Category(categoryString, (int) Math.random());
        FinancialEntry categorie = new FinancialEntry(name, value, category);
        table.getItems().add(categorie);
        if (!categories.getItems().contains(categoryString)) {
            categories.getItems().add(categoryString);
        }
        return true;

    }
}
