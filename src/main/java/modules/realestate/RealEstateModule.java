package modules.realestate;

import elements.AlertBox;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RealEstateModule {
    Stage window;
    Button returnButton = new Button("return");

    public void display(Stage primaryStage, Scene previousScene) {
        window = primaryStage;
        window.setTitle("Real Estate module");

        // the menu
        Menu fileMenu = new Menu("file");

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
        VBox centreDivider = new VBox();
        HBox header = new HBox(10);

        // Name and price of the property
        Label houseName = getLabel();
        houseName.setText("Staringkade 7, 2273RN");

        Label askingPrice = getLabel();
        askingPrice.setText("125000");

        header.getChildren().addAll(houseName, askingPrice);

        centreDivider.getChildren().addAll(header);

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
        layout.setCenter(centreDivider);
        layout.setLeft(propertyListContainer);
        layout.setBottom(returnButton);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    private Label getLabel() {
        Label houseName = new Label();
        houseName.setPadding(new Insets(5,5,5,5));
        houseName.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return houseName;
    }

    private TextField getTextField(String name) {
        TextField textField = new TextField();
        textField.setPromptText(name);
        textField.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return textField;
    }
}
