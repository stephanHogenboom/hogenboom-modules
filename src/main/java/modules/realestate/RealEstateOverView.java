package modules.realestate;

import elements.AlertBox;
import elements.ConfirmationBox;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modules.Module;
import modules.realestate.model.Address;
import modules.realestate.model.Addressee;
import modules.realestate.model.PropertyEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RealEstateOverView extends Module {
    Stage window;
    private final Button returnButton = new Button("return");
    TableView<String> propertyInformationTable;
    private final RealEstateDAO dao = new RealEstateDAO();
    private List<PropertyEntry> entries;
    private Label houseName, askingPrice, sellPrice;

    public void display(Stage primaryStage, Scene previousScene) {
        window = primaryStage;
        window.setTitle("Real Estate module");
        window.setMinWidth(1000);
        window.setMinHeight(500);

        //Adding centre content
        HBox centre = new HBox();
        VBox centreInformationContainer = new VBox();
        GridPane centreInformationTop = new GridPane();
        centreInformationTop.setPadding(new Insets(10, 10, 10, 10));
        centreInformationTop.setVgap(8);
        centreInformationTop.setHgap(10);

        //initiate house list
        ListView<String> propertyList = new ListView<>();
        setPropertyList(propertyList);
        propertyList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        propertyList.setOnMousePressed(e -> {
                    String selectedItemName = propertyList.getSelectionModel().getSelectedItems().get(0);
                    PropertyEntry entry = getPropertyEntryByName(selectedItemName);
                    setLabels(entry);
                }

        );

        VBox propertyListContainer = new VBox();
        propertyListContainer.setMaxWidth(250);

        ButtonBar buttonBar = new ButtonBar();

        // Name of the property
        Label propertyNameTitle = getLabel("property name", "BOLD");
        GridPane.setConstraints(propertyNameTitle, 0, 0);

        houseName = getLabel("house_name");
        GridPane.setConstraints(houseName, 0, 1);

        //price of the property
        Label askingPriceHolder = getLabel("asking price", "BOLD");
        GridPane.setConstraints(askingPriceHolder, 1, 0);

        askingPrice = getLabel("house_name");
        GridPane.setConstraints(askingPrice, 1, 1);

        //price of the property
        Label sellPriceHolder = getLabel("sell price", "BOLD");
        GridPane.setConstraints(sellPriceHolder, 2, 0);

        sellPrice = getLabel("sell_price");
        GridPane.setConstraints(sellPrice, 2, 1);

        centreInformationTop.getChildren().addAll(propertyNameTitle, houseName, askingPriceHolder, askingPrice, sellPriceHolder, sellPrice);


        //set ImageView
        ImageView propertyPortait = getImageView();

        HBox centreInfoBox = new HBox();
        ListView<String> addressees = new ListView<>();
        centreInfoBox.getChildren().addAll(addressees);

        centreInformationContainer.getChildren().addAll(centreInformationTop, centreInfoBox);
        if (propertyPortait != null) {
            centre.getChildren().addAll(centreInformationContainer, propertyPortait);
        } else {
            centre.getChildren().addAll(centreInformationContainer);
        }

        //Define Buttons
        Button addButton = new Button("add");
        addButton.setOnAction(e -> {
            AddEntryScreen screen = new AddEntryScreen();
            screen.display(null);
            setPropertyList(propertyList);
        });


        Button copyButton = new Button("copy");
        copyButton .setOnAction(e -> {
            String name = propertyList.getSelectionModel().getSelectedItem();
            AddEntryScreen screen = new AddEntryScreen();
            screen.display(getPropertyEntryByName(name));
            setPropertyList(propertyList);
        });

        Button deleteButton = new Button("delete");
        deleteButton.setOnAction(e -> {
            String name = propertyList.getSelectionModel().getSelectedItem();
            // confirming that the items should be deleted
            if (!ConfirmationBox.display("Deleting property", String.format("are you sure you want to delete %s", name))) {
                return;
            }
            //finding the kix of an entry by its name and remove the string out of the list
            PropertyEntry entry = getPropertyEntryByName(name);
            if (entry != null) {
                dao.deletePropertyByKixcode(entry.getAddress().getKixCode());
                propertyList.getItems().remove(entry.getName());
            } else {
                AlertBox.display("Error", "the property was not found");
            }
        });

        Button infoButton = new Button("info");
        infoButton.setOnAction(e ->  {
            addressees.getItems().removeAll(addressees.getItems());
            String selectedItem = propertyList.getSelectionModel().getSelectedItems().get(0);
            PropertyEntry entry = getPropertyEntryByName(selectedItem);
            List<Addressee> addresseeList= entry.getAddressees().orElse(new ArrayList<>());
            for (Addressee addressee : addresseeList) {
                addressees.getItems().add(addressee.getName());
            }
        });





        // add buttons and list to layout
        buttonBar.getButtons().addAll(addButton, deleteButton, copyButton, infoButton);
        propertyListContainer.getChildren().addAll(propertyList, buttonBar);

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

    private void setPropertyList(ListView<String> propertyList) {
        List<String> items = propertyList.getItems();
        entries = dao.getPropertyEntries();
        List<String> propertyNames = new ArrayList<>();
        entries.stream().filter(e -> !items.contains(e.getName())).forEach(item -> propertyNames.add(item.getName()));
        propertyList.getItems().addAll(propertyNames);
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

    private void setLabels(PropertyEntry entry) {
        Address address = entry.getAddress();
        houseName.setText(String.format("%s %s%s", address.getStreet(), address.getHouseNumber(), address.getExtension()));
        askingPrice.setText(entry.getLatestPriceHistoryEntry().getAskingPrice().toString());
        if (entry.getSellPrice() == null || entry.getSellPrice().toString().trim().isEmpty()) {
            sellPrice.setText("not sold yet");
        } else {
            sellPrice.setText(entry.getSellPrice().toString());
        }
    }

    private PropertyEntry getPropertyEntryByName(String name) {
        for (PropertyEntry entry : entries) {
            if (entry.getName().equals(name)) {
                return entry;
            }
        }
        return null;
    }
}
