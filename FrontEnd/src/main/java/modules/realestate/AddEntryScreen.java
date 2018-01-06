package main.java.modules.realestate;

import com.google.common.annotations.VisibleForTesting;
import main.java.elements.AlertBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.modules.Module;
import main.java.modules.realestate.model.Address;
import main.java.modules.realestate.model.AddressBuilder;
import main.java.modules.realestate.model.PriceHistoryEntry;
import main.java.modules.realestate.model.PropertyEntry;
import main.java.util.ValidatorKotlin;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEntryScreen extends Module {

    TextField streetNameEntry, houseNumber, extension, postalCode, askingPrice, sellPrice, city;
    ValidatorKotlin validator = new ValidatorKotlin();
    CheckBox isSold;
    Stage window;

    public void display(PropertyEntry entry) {
        window = new Stage();

        if (!window.getModality().equals(Modality.APPLICATION_MODAL)) {
            window.initModality(Modality.APPLICATION_MODAL);
        }

        window.setTitle("add property");
        window.setMinWidth(1000);
        window.setMinHeight(1000);


        VBox vbox = new VBox();


        //is money properties

        isSold = new CheckBox("is sold");
        askingPrice = getTextField("asking price");
        sellPrice = getTextField("sell price");

        //Address

        streetNameEntry = getTextField("street name");
        HBox numberExtension = new HBox();
        houseNumber = getTextField("house number");
        extension = getTextField("extension");
        numberExtension.getChildren().addAll(houseNumber, extension, isSold);
        postalCode = getTextField("postal code");
        city = getTextField("city");

        Button relation = new Button("add relation");
        relation.setOnAction(e -> {
            AddOrEditRelationScreen screen = new AddOrEditRelationScreen();
            Address address = validateAddSetAddress();
            if (address == null) return;
            screen.display(address, (entry != null)? entry : new PropertyEntry());
        });

        Button saveEntry = new Button("save");
        saveEntry.setOnAction(e -> addProperty(entry));

        Button cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());


        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(relation, saveEntry, cancelButton);


        vbox.getChildren().addAll(streetNameEntry, numberExtension, postalCode, city, askingPrice, sellPrice, buttonBar);
        Scene scene = new Scene(vbox);
        if (entry != null) {
            setTextFieldValues(entry);
        }
        window.setScene(scene);
        window.showAndWait();
    }

    public void setTextFieldValues(PropertyEntry entry) {
        Address address = entry.getAddress();
        isSold.setSelected(entry.isSold());
        houseNumber.setText(String.valueOf(address.getHouseNumber()));
        extension.setText(address.getExtension());
        streetNameEntry.setText(address.getStreet());
        sellPrice.setText(String.valueOf(entry.getSellPrice()));
        askingPrice.setText(entry.getLatestPriceHistoryEntry().getAskingPrice().toString());
        postalCode.setText(address.getPostalCode());
        city.setText(address.getCity());
    }

    private Address validateAddSetAddress() {
        Address address = new Address();
        //All mandatory address information should be present
        List<TextField> list = new ArrayList<>(Arrays.asList(streetNameEntry, houseNumber, postalCode, city));
        for (TextField field : list) {
            if (field.getText() == null || field.getText().trim().isEmpty()) {
                AlertBox.display("error", field.getPromptText() + " must be non empty!");
                return null;
            }
        }

        // postal code should match "\d{4}"
        String postalCodeText = postalCode.getText().toUpperCase();
        if (!isValidPostalCode(postalCodeText)) {
            AlertBox.display("error", postalCode.getText() + "is not a valid postalcode!");
            return null;
        }

        AddressBuilder bldr = new AddressBuilder();

        if (!validator.isNumeric(houseNumber.getText())) {
            AlertBox.display("error", "house number is not numeric!");
            return null;
        }

        String ext = (extension.getText() != null || !extension.getText().isEmpty()) ? extension.getText() : "";
        bldr.setCountry("NL")
                .setPostalCode(postalCodeText)
                .setStreet(streetNameEntry.getText())
                .setHouseNumber(Integer.parseInt(houseNumber.getText()))
                .setExtension(ext)
                .setCity(city.getText())
                .setKixCode();
        return bldr.build();
    }


    private void addProperty(PropertyEntry oldEntry) {
        Address address = validateAddSetAddress();
        if (address == null) return;

        PropertyEntry entry = new PropertyEntry();
        entry.setAddress(address);
        entry.setDate(LocalDate.now());
        entry.setSold(isSold.isSelected());
        PriceHistoryEntry priceHistoryEntry = new PriceHistoryEntry();

        if (oldEntry != null && oldEntry.getAddressees().isPresent()) {
            entry.setAddressees(oldEntry.getAddressees());
        }

        // prices should be numeric
        if (!validator.isNumeric(askingPrice.getText())) {
            AlertBox.display("error", "asking price is not numeric!");
            return;
        }
        priceHistoryEntry.setAskingPrice(Long.parseLong(askingPrice.getText()));

        if (isSold.isSelected()) {
            if (!validator.isNumeric(askingPrice.getText())) {
                AlertBox.display("error", "selling price is not numeric!");
                return;
            }
            entry.setSellPrice(Long.parseLong(sellPrice.getText()));
        }

        // you cannot add a property with the same address twice on one day
        if (oldEntry != null && oldEntry.getDate().equals(LocalDate.now()) && entry.getAddress().getKixCode().equals(oldEntry.getAddress().getKixCode())) {
            AlertBox.display("error", "an Property Entree for this address on this date already exists!");
            return;
        }

        List<PriceHistoryEntry> priceList = new ArrayList<>();
        priceList.add(priceHistoryEntry);
        entry.setPriceHistories(priceList);

        RealEstateDAO dao = new RealEstateDAO();
        try {
            window.close();
            dao.insertPropertyEntry(entry);
        } catch (SQLException e) {
            AlertBox.display("error", e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
            window.close();
        }
    }

    @VisibleForTesting
    boolean isValidPostalCode(String postalCode) {
        //https://stackoverflow.com/questions/578406/what-is-the-ultimate-postal-code-and-zip-regex
        return postalCode.matches("\\d{4}[ ]?[A-Z]{2}");
    }
}
