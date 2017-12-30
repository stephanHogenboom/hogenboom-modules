package modules.realestate;

import elements.AlertBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modules.Module;
import modules.realestate.model.Address;
import modules.realestate.model.PriceHistoryEntry;
import modules.realestate.model.PropertyEntry;
import util.Validator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEntryScreen extends Module {

    TextField streetNameEntry, houseNumber, extension, postalCode, askingPrice, sellPrice;
    Validator validator = new Validator();
    CheckBox isSold;

    public void display() {
        Stage window = new Stage();

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

        Button occupant = new Button("add occupant");
        Button addAddress = new Button("add address");
        addAddress.setOnAction(e -> addProperty());


        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(occupant, addAddress);

        vbox.getChildren().addAll(streetNameEntry, numberExtension, postalCode, askingPrice, sellPrice, buttonBar);
        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();

    }

    private void addProperty() {
        Address address = new Address();
        List<TextField> list = new ArrayList<>(Arrays.asList(streetNameEntry, houseNumber,  postalCode));
        for (TextField field : list) {
            if (field.getText() == null || field.getText().trim().isEmpty()) {
                AlertBox.display("error", field.getPromptText() + " must be non empty!" );
                return;
            }
        }
        address.setStreet(streetNameEntry.getText());
        if (!validator.isNumeric(houseNumber.getText())) {
            AlertBox.display("error", "house number is not numeric!");
            return;
        }
        address.setHouseNumber(Integer.parseInt(houseNumber.getText()));
        if (extension.getText() == null || extension.getText().isEmpty()) {
            address.setExtension(extension.getText());
        }
        address.setPostalCode(postalCode.getText());

        PropertyEntry entry = new PropertyEntry();
        entry.setAddress(address);
        entry.setDate(LocalDate.now());
        entry.setSold(isSold.isSelected());
        PriceHistoryEntry priceHistoryEntry = new PriceHistoryEntry();
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

        List<PriceHistoryEntry> priceList = new ArrayList<>();
        priceList.add(priceHistoryEntry);
        entry.setPriceHistories(priceList);

        RealEstateDAO dao = new RealEstateDAO();
        try {
            dao.insertPropertyEntry(entry);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
}
