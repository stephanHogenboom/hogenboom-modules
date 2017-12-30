package modules.realestate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modules.Module;
import modules.realestate.model.PropertyEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RealEstateOverView extends Module {
    Stage window;
    Button returnButton = new Button("return");
    TableView<String> propertyInformationTable;
    RealEstateDAO dao = new RealEstateDAO();

    public void display(Stage primaryStage, Scene previousScene) {
        window = primaryStage;
        window.setTitle("Real Estate module");
        window.setMinWidth(1000);
        window.setMinHeight(500);

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

        // set ImageView
        ImageView propertyPortait = getImageView();

        centreInformationContainer.getChildren().addAll(centreInformationTop);
        if (propertyPortait != null) {
            centre.getChildren().addAll(centreInformationContainer, propertyPortait);
        } else {
            centre.getChildren().addAll(centreInformationContainer);
        }


        //initiate house list
        ListView<String> propertyList = new ListView<>();
        List<PropertyEntry> entries = dao.getPropertyEntries();
        List<String> propertyNames = new ArrayList<>();

        entries.forEach(entry -> propertyNames.add(entry.getName()));

        VBox propertyListContainer = new VBox();
        propertyListContainer.setMaxWidth(250);
        propertyList.getItems().addAll(propertyNames);

        HBox propertyListButtonContainer = new HBox();
        Button addButton = new Button("add");
        addButton.setOnAction(e -> {
            AddEntryScreen screen = new AddEntryScreen();
            screen.display();
        });

        Button deleteButton = new Button("delete");

        // add buttons and list to layout
        propertyListButtonContainer.getChildren().addAll(addButton, deleteButton);
        propertyListContainer.getChildren().addAll(propertyList, propertyListButtonContainer);

        returnButton.setOnAction(e -> window.setScene(previousScene));
        ButtonBar bottomButtonBar = new ButtonBar();
        bottomButtonBar.getButtons().addAll(returnButton);

        //add BorderPane for layout
        BorderPane layout = new BorderPane();
        layout.setCenter(centre);
        layout.setLeft(propertyListContainer);
        layout.setBottom(bottomButtonBar);
        Scene scene = new Scene(layout);
        scene.getStylesheets().addAll("index.css");
        window.setScene(scene);
        window.show();
    }

    private ImageView getImageView() {
        Path dir = Paths.get(".").toAbsolutePath().normalize();
        Path file = Paths.get(dir.toString() + "/0.jpg");
        ImageView propertyPortait = null;
        if (Files.exists(file)) {
            Image image = new Image(file.toUri().toString());
            propertyPortait = new ImageView(image);
            propertyPortait.setFitWidth(350);
            propertyPortait.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 2, 2, 2);");
        }
        return propertyPortait;
    }


}
