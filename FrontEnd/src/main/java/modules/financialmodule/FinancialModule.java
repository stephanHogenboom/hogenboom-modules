package main.java.modules.financialmodule;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import main.java.elements.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.modules.Module;
import main.java.modules.financialmodule.model.Category;
import main.java.modules.financialmodule.model.FinancialDAO;
import main.java.modules.financialmodule.model.FinancialEntry;
import main.java.util.ValidatorKotlin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class FinancialModule extends Module {

    Stage window;
    TableView<FinancialEntry> table;
    Button addButton, deleteButton, editButton, returnButton;
    TextField nameInput, valueInput;
    ComboBox<String> categories;
    ValidatorKotlin validator = new ValidatorKotlin();
    FinancialDAO dao = new FinancialDAO();
    private final String FINANCIAL_ENTRY_TABLE_NAME = "financial_entry";
    private final String CATEGORY_TABLE_NAME = "category";

    public void display(Stage primaryStage, Scene previousScene) {
        window = setWindowProperties(primaryStage, "Financial app", 10000, 700 );

        BorderPane layout = new BorderPane();
        VBox excellLayout = new VBox();

        /*
         * Start Excel Sheet
         */

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
        addButton.setOnAction(e -> addEntry());

        HBox inputAndButtonsBox = new HBox();
        inputAndButtonsBox.getChildren().addAll(nameInput, categories, valueInput, addButton);

        excellLayout.getChildren().addAll(table, inputAndButtonsBox);

        /*
         * End Excel Sheet
         * Start Pie and Bar Chart
         */

        PieChart pie = getPieChart();
        BarChart bar = getBarChart();

        VBox pieAndBarChart = new VBox();
        pieAndBarChart.getChildren().addAll(pie, bar);

        /*
         *  End Pie and Bar Chart
         */

        /*
         * button
         */

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
        return true;

    }

    private PieChart getPieChart() {

        ArrayList<FinancialEntry> entries = dao.getAllFinancialEntries();
        ArrayList<Category> categories = dao.getAllCategories();

        Multimap<String, FinancialEntry> entryByCategory = ArrayListMultimap.create();
        ArrayList<PieChart.Data> dataList = new ArrayList<>();
        for (FinancialEntry entry : entries) {
            System.out.println("name =" + entry.getName());
            System.out.println("categorie =" + entry.getCategory());
            entryByCategory.put(entry.getCategory().getName(), entry);
        }

        for (Category category : categories) {
            Collection<FinancialEntry> entrylistOfCategory = entryByCategory.get(category.getName());
            double totalValue = 0;

            for (FinancialEntry entry : entrylistOfCategory) {
                totalValue += entry.getValue();
            }
            dataList.add(new PieChart.Data(category.getName(), totalValue));
        }

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        dataList);
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Monthly overview");
        return chart;
    }

    private BarChart getBarChart() {

        // TODO: get the actual values from the database

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc =
                new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Balance");
        xAxis.setLabel("Month");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Income");
        series1.getData().add(new XYChart.Data("Jan", 25601.34));
        series1.getData().add(new XYChart.Data("Feb", 20148.82));
        series1.getData().add(new XYChart.Data("Mar", 10000));
        series1.getData().add(new XYChart.Data("Apr", 12000));
        series1.getData().add(new XYChart.Data("May", 35407.15));
        series1.getData().add(new XYChart.Data("Jun", 12000));
        series1.getData().add(new XYChart.Data("Jul", 12000));
        series1.getData().add(new XYChart.Data("Aug", 12000));
        series1.getData().add(new XYChart.Data("Sept", 12000));
        series1.getData().add(new XYChart.Data("Oct", 12000));
        series1.getData().add(new XYChart.Data("Nov", 12000));
        series1.getData().add(new XYChart.Data("Dec", 12000));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Costs");
        series2.getData().add(new XYChart.Data("Jan", 57401.85));
        series2.getData().add(new XYChart.Data("Feb", 41941.19));
        series2.getData().add(new XYChart.Data("Mar", 45263.37));
        series2.getData().add(new XYChart.Data("Apr", 117320.16));
        series2.getData().add(new XYChart.Data("May", 35407.15));
        series2.getData().add(new XYChart.Data("Jun", 12000));
        series2.getData().add(new XYChart.Data("Jul", 12000));
        series2.getData().add(new XYChart.Data("Aug", 12000));
        series2.getData().add(new XYChart.Data("Sept", 12000));
        series2.getData().add(new XYChart.Data("Oct", 12000));
        series2.getData().add(new XYChart.Data("Nov", 12000));
        series2.getData().add(new XYChart.Data("Dec", 12000));


        bc.getData().addAll(series1, series2);
        return bc;
    }


}
