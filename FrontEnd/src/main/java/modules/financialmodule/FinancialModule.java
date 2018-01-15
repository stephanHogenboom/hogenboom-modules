package modules.financialmodule;

import elements.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.Module;
import modules.financialmodule.model.Category;
import modules.financialmodule.model.FinancialDAO;
import modules.financialmodule.model.FinancialEntry;
import util.ValidatorKotlin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinancialModule extends Module {

    Stage window;
    TableView<FinancialEntry> table;
    Button addButton, deleteButton, editButton, returnButton;
    TextField nameInput, valueInput;
    ComboBox<String> categories;
    ValidatorKotlin validator = new ValidatorKotlin();
    private final FinancialDAO dao;
    private List<FinancialEntry> entryList;
    private final String FINANCIAL_ENTRY_TABLE_NAME = "financial_entry";
    private final String CATEGORY_TABLE_NAME = "category";

    private final PieChart pieChart;

    private final BarChart<String, Number> barChart;
    private final CategoryAxis xAxis;
    private final NumberAxis yAxis;
    private XYChart.Series series1;

    private ObservableList data;

    // constructor zet alle final objects
    public FinancialModule(){
        this.dao       = new FinancialDAO();
        this.entryList = this.dao.getAllFinancialEntries();

        this.pieChart = new PieChart();

        this.xAxis    = new CategoryAxis();
        this.yAxis    = new NumberAxis();
        this.barChart = new BarChart<>( xAxis, yAxis );
        this.series1  = new XYChart.Series();
    }

    public void display(Stage primaryStage, Scene previousScene) {

        window = setWindowProperties(primaryStage, "Financial app", 1000, 700 );

        BorderPane layout = new BorderPane();
        VBox excellLayout = new VBox();

        TableColumn<FinancialEntry, String> nameColumn = addColumn("name");
        TableColumn<FinancialEntry, Category> categoryColumn = addColumn("category");
        TableColumn<FinancialEntry, Double> valueColumn = addColumn("value");
        TableColumn<FinancialEntry, LocalDateTime> dateColumn = addColumn("date");

        //initializeTable
        table = new TableView<>();
        table.setItems(getFinancialEntries());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(nameColumn, categoryColumn, valueColumn, dateColumn);

        nameInput = new TextField();
        nameInput.setPromptText("name");

        valueInput = new TextField();
        valueInput.setPromptText("value");

        categories = new ComboBox<>();
        categories.setEditable(true);
        categories.setPromptText("category");
        for (Category category : dao.getAllCategories()) {
            categories.getItems().add(category.getName());
        }

        addButton = new Button("add");
        addButton.setOnAction(e -> {
            addEntry();
            refreshPiechart();
            refreshBarChart();
        });

        HBox inputAndButtonsBox = new HBox();
        inputAndButtonsBox.getChildren().addAll(nameInput, categories, valueInput, addButton);

        excellLayout.getChildren().addAll(table, inputAndButtonsBox);

        setDataPieChart();
        setBarChartData();

        VBox pieAndBarChart = new VBox();
        pieAndBarChart.getChildren().addAll(this.pieChart, this.barChart);

        ButtonBar buttonBar = new ButtonBar();
        returnButton = new Button("retrun");
        returnButton.setOnAction(e -> window.setScene(previousScene));

        buttonBar.getButtons().addAll(returnButton);

        layout.setCenter(excellLayout);
        layout.setRight(pieAndBarChart);
        layout.setBottom(buttonBar);

        Scene financialScreen = new Scene(layout);
        window.setScene(financialScreen);
        window.show();
    }



    private ObservableList<FinancialEntry> getFinancialEntries() {
        ObservableList<FinancialEntry> entries = FXCollections.observableArrayList();
        ArrayList<FinancialEntry> list = dao.getAllFinancialEntries();
        for (FinancialEntry entry : list) {
            entries.add(entry);
        }
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
        FinancialEntry entry;
        Category category;
        if (!categories.getItems().contains(categoryString)) {
            categories.getItems().add(categoryString);
            category = new Category(dao.incrementAndGetId(CATEGORY_TABLE_NAME), categoryString);
        } else {
            category = dao.getCategorie(categoryString);
        }
        entry = new FinancialEntry(dao.incrementAndGetId(FINANCIAL_ENTRY_TABLE_NAME), name, value, category);
        table.getItems().add(entry);
        dao.insertEntry(entry);
        dao.insertCategorie(category);
        entryList.add(entry);
        return true;

    }

    private void refreshPiechart() {
        this.pieChart.setData(this.getPieChartData());
    }

    private void setDataPieChart() {
        this.pieChart.setData(this.getPieChartData());
        this.pieChart.setTitle("Monthly overview");
    }

    private ObservableList getPieChartData(){
        ArrayList<PieChart.Data> dataList = new ArrayList<>();
        this.getEntryData().forEach((k,v) -> dataList.add(new PieChart.Data(k, v)));
        return FXCollections.observableArrayList(dataList);
    }

    private HashMap<String, Double> getEntryData(){
        HashMap<String, Double> exspensesByCategory = new HashMap<>();
        entryList.forEach(
                entry -> exspensesByCategory.compute(
                        entry.getCategory().getName(),
                        (k,v) -> v == null ? entry.getValue() : v + entry.getValue()
                )
        );
        return exspensesByCategory;
    }

    private void refreshBarChart(){
        this.getBarChartData();
        this.barChart.getData().add(this.series1);
    }

    private void setBarChartData() {
        this.barChart.setTitle("Balance");
        this.xAxis.setLabel("Month");
        this.yAxis.setLabel("Value");
        this.series1.setName("Costs");
        this.getEntryData().forEach((k,v) -> this.series1.getData().add(new XYChart.Data(k, v)));
        this.barChart.getData().addAll(this.series1);
    }

    private void getBarChartData(){
        this.getEntryData().forEach((k,v) -> this.series1.getData().add(new XYChart.Data(k, v)));
    }


}
