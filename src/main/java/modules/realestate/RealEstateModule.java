package modules.realestate;

import elements.AlertBox;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RealEstateModule {
    Stage window;
    Button returnButton = new Button("return");
    TableView<String> propertyInformationTable;

    public void display(Stage primaryStage, Scene previousScene) {
        window = primaryStage;
        window.setTitle("Real Estate module");
        window.setMinWidth(1000);
        window.setMinHeight(500);

        // the menu
        Menu fileMenu = new Menu("_file");

        //the items
        MenuItem newProperty = new MenuItem("new property...");
        newProperty.setOnAction(e -> System.out.println("new property"));
        MenuItem swapModule = new MenuItem("swap module");
        swapModule.setOnAction(e -> AlertBox.display("invalid", "not implemented yet"));
        fileMenu.getItems().addAll(newProperty, new SeparatorMenuItem(), swapModule);

        // main menu bar
        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(fileMenu);

        //Adding centre content
        HBox centre = new HBox();
        VBox centreInformationContainer = new VBox();
        GridPane centreInformationTop = new GridPane();
        centreInformationTop.setPadding(new Insets(10,10,10,10));
        centreInformationTop.setVgap(8);
        centreInformationTop.setHgap(10);



        // Name of the property
        Label propertyNameTitle = getLabel("property name", "BOLD");
        GridPane.setConstraints(propertyNameTitle, 0 ,0);

        Label houseName = getLabel("Staringkade 7, 2273RN");
        GridPane.setConstraints(houseName, 0 ,1);

        //price of the property
        Label askingPriceHolder = getLabel("asking price", "BOLD");
        GridPane.setConstraints(askingPriceHolder, 1 ,0);

        Label askingPrice = getLabel("125000");
        GridPane.setConstraints(askingPrice, 1 ,1);

        //price of the property
        Label sellPriceHolder = getLabel("sell price", "BOLD");
        GridPane.setConstraints(sellPriceHolder, 2 ,0);

        Label sellPrice = getLabel("90.000");
        GridPane.setConstraints(sellPrice, 2 ,1);

        centreInformationTop.getChildren().addAll(propertyNameTitle, houseName, askingPriceHolder, askingPrice, sellPriceHolder, sellPrice);

        centreInformationContainer.getChildren().addAll(centreInformationTop);
        centre.getChildren().addAll(centreInformationContainer);



        //initiate house list
        ListView<String> propertyList = new ListView<>();
        VBox propertyListContainer = new VBox();
        propertyListContainer.setMaxWidth(250);
        propertyList.getItems().addAll("Staringkade 7, Voorburg", "van der Marckstaat 17, voorburg");

        HBox propertyListButtonContainer = new HBox();
        Button addButton = new Button("add");

        Button deleteButton = new Button("delete");

        // add buttons and list to layout
        propertyListButtonContainer.getChildren().addAll(addButton, deleteButton);
        propertyListContainer.getChildren().addAll(propertyList, propertyListButtonContainer);

        returnButton.setOnAction(e -> window.setScene(previousScene));

        //add BorderPane for layout
        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(centre);
        layout.setLeft(propertyListContainer);
        layout.setBottom(returnButton);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    private Label getLabel(String name, String option) {
        Label label = new Label(name);
        switch (option) {
            case "BOLD" : label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                break;
            default:
        }
        return getLabel(label);
    }

    private Label getLabel(String name) {
        Label label = new Label(name);
        return getLabel(label);
    }

    private Label getLabel(Label label) {
        label.setPadding(new Insets(5,5,5,5));
        label.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return label;
    }

    private TextField getTextField(String name) {
        TextField textField = new TextField();
        textField.setPromptText(name);
        textField.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return textField;
    }
}
