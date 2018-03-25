package modules.realestate;

import elements.AlertBox;
import elements.ConfirmationBox;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modules.Module;
import modules.realestate.model.Address;
import modules.realestate.model.Addressee;
import modules.realestate.model.PropertyEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RealEstateOverView extends Module {
    private TableView<String> propertyInformationTable;
    private final HashMap<String, Addressee> addressees = new HashMap<>();
    private final RealEstateDAO dao = new RealEstateDAO();
    private final ListView<String> addresseeListview = new ListView<>();
    private List<PropertyEntry> entries;
    private Label houseName, askingPrice, sellPrice, emailOfAddressee, numberOfAddressee, nameOfAddressee;
    private PropertyEntry selectedEntry = new PropertyEntry();

    public BorderPane initRealEstateModule() {

        //Adding centre content
        HBox centre = new HBox();
        VBox centreInformationContainer = new VBox();
        GridPane centreInformationTop = new GridPane();
        centreInformationTop.setPadding(new Insets(10, 10, 10, 10));
        centreInformationTop.setVgap(8);
        centreInformationTop.setHgap(10);

        //initiate house list
        ListView<String> propertyList = new ListView<>();
        setPropertyListAndEntriesAndAddressees(propertyList);
        propertyList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        propertyList.setOnMousePressed(e -> {
            List<String> items = (propertyList.getSelectionModel().getSelectedItems());
            if (items == null || items.isEmpty()) {
                return;
            }
            String selectedItemName = propertyList.getSelectionModel().getSelectedItems().get(0);
            selectedEntry = getPropertyEntryByName(selectedItemName);
            setLabels(selectedEntry);
            // add addressees toi Listview
            if (selectedEntry == null) {
                return;
            }
            List<Addressee> addresseeList = selectedEntry.getAddressees().orElse(new ArrayList<>());
            addresseeListview.getItems().removeAll(addresseeListview.getItems());
            for (Addressee addressee : addresseeList) {
                addresseeListview.getItems().add(addressee.getName());
            }
        });

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

        // information in the centre
        HBox centreInfoBox = new HBox();
        centreInfoBox.setPadding(new Insets(10, 10, 10, 10));
        VBox relationInfo = new VBox();
        relationInfo.setPadding(new Insets(0, 10, 10, 10));
        nameOfAddressee = getLabel("name: ");
        numberOfAddressee = getLabel("number: ");
        emailOfAddressee = getLabel("number: ");
        relationInfo.getChildren().addAll(nameOfAddressee, numberOfAddressee, emailOfAddressee);

        addresseeListview.setOnMousePressed(e -> {
            List<String> items = (addresseeListview.getSelectionModel().getSelectedItems());
            if (items == null || items.isEmpty()) {
                return;
            }
            setAddresseeLabels(addressees.get(items.get(0)));
        });



        centreInfoBox.getChildren().addAll(addresseeListview, relationInfo);

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
            setPropertyListAndEntriesAndAddressees(propertyList);
        });


        Button copyButton = new Button("copy");
        copyButton.setOnAction(e -> {
            String name = propertyList.getSelectionModel().getSelectedItem();
            AddEntryScreen screen = new AddEntryScreen();
            screen.display(getPropertyEntryByName(name));
            setPropertyListAndEntriesAndAddressees(propertyList);
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
        infoButton.setOnAction(e -> {});


        // add buttons and list to layout
        buttonBar.getButtons().addAll(addButton, deleteButton, copyButton, infoButton);
        propertyListContainer.getChildren().addAll(propertyList, buttonBar);

        //add BorderPane for layout
        BorderPane layout = new BorderPane();
        layout.setCenter(centre);
        layout.setLeft(propertyListContainer);
        return layout;
    }

    private void setPropertyListAndEntriesAndAddressees(ListView<String> propertyList) {
        List<String> items = propertyList.getItems();
        if (entries == null || entries.isEmpty()) {
            entries = dao.getPropertyEntries();
        }
        List<String> propertyNames = new ArrayList<>();
        entries.stream().filter(e -> !items.contains(e.getName())).forEach(item -> propertyNames.add(item.getName()));
        propertyList.getItems().addAll(propertyNames);
        for (PropertyEntry entry : entries) {
            if (entry.getAddressees().isPresent()) {
                for (Addressee addressee : entry.getAddressees().get()){
                    addressees.put(addressee.getName(), addressee);
                }
            }
        }
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

    private void setAddresseeLabels(Addressee addressee) {
        if (addressee == null) {
            AlertBox.display("Error", "the selected addressee relation was not found!");
            return;
        }
        System.out.println(addressee);
        nameOfAddressee.setText(String.format("name: %s", addressee.getName()));
        numberOfAddressee.setText(String.format("number: %s", addressee.getPhoneNumber().orElse("")));
        emailOfAddressee.setText(String.format("email: %s", addressee.getEmail().orElse("")));
    }
}
