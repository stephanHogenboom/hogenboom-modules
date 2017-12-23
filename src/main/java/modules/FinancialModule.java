package modules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.model.Category;
import modules.model.FinancialEntry;

import java.time.LocalDateTime;

public class FinancialModule {

    Stage window;
    TableView<FinancialEntry> table;

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


        excellLayout.getChildren().add(table);
        layout.setCenter(excellLayout);
        Scene financialScreen = new Scene(layout);
        window.setScene(financialScreen);
        window.show();


     /*


       *//* //nameInput
        nameInput = new TextField();
        nameInput.setPromptText("name");
        nameInput.setMinWidth(100);

        //priceInput
        priceInput = new TextField();
        priceInput.setPromptText("price");
        priceInput.setMinWidth(100);

        //quantityInput
        quantityInput = new TextField();
        quantityInput.setPromptText("quantity");
        quantityInput.setMinWidth(100);

        //addButton
        Button addButton = new Button("add");
        addButton.setOnAction( e -> addProduct());



        Button deleteButton = new Button("delete");

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nameInput, priceInput, quantityInput, addButton, deleteButton);





        box.getChildren().addAll(table, hBox);

        Scene scene = new Scene(box);
        window.setScene(scene);
        window.show();*/


    }

    private TableColumn addColumn(String name) {
        TableColumn<FinancialEntry, ?> column = new TableColumn<>(name);
        column.setMinWidth(200);
        column.setCellValueFactory(new PropertyValueFactory<>(name));
        return column;
    }

    public ObservableList<FinancialEntry> getFinancialEntries() {
        ObservableList<FinancialEntry> entries = FXCollections.observableArrayList();
        entries.add(new FinancialEntry("test", new Double(200.0) , new Category("test", 1)));
        return entries;
    }
}
