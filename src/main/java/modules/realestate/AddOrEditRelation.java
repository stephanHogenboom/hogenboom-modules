package modules.realestate;

import com.google.common.annotations.VisibleForTesting;
import elements.AlertBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.Module;
import modules.realestate.model.Address;
import modules.realestate.model.Addressee;
import modules.realestate.model.PropertyEntry;
import util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddOrEditRelation extends Module {
    Stage window;
    TextField relationName, relationPhone, relationEmail;
    Validator validator = new Validator();

    public void display(Address address, PropertyEntry entry) {
        window = new Stage();
        window.setTitle("add a relation");

        window.setMinWidth(1000);
        window.setMinHeight(1000);


        VBox vbox = new VBox();
        relationName = getTextField("full name");
        relationPhone = getTextField("telephone number");
        relationEmail = getTextField("email address");
        Label addresLabel = getLabel(String.format("%s %s%s, %s", address.getStreet(), address.getHouseNumber(), address.getExtension(), address.getPostalCode()));
        ButtonBar buttonBar = new ButtonBar();
        Button save = new Button("save");
        save.setOnAction(e -> saveRelation(address, entry));
        Button returnButton = new Button("return");
        returnButton.setOnAction(e -> window.close());
        buttonBar.getButtons().addAll(save, returnButton);
        vbox.getChildren().addAll(relationName, relationPhone, relationEmail, addresLabel, buttonBar);

        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();
    }

    private void saveRelation(Address address, PropertyEntry entry) {
        Addressee addressee = new Addressee();
        // name of relation cannot be empty
        if (relationName.getText().trim().isEmpty()) {
            AlertBox.display("error", "name cannot be empty!");
            return;
        }
        // if an email is provided it should be legit
        if (relationEmail.getText() != null && !relationEmail.getText().trim().isEmpty() && !isValidEmail(relationEmail.getText())) {
            AlertBox.display("error", "email not valid!");
            return;
        }

        // if an phone number is provided it should bew numeric
        String phone = relationPhone.getText();
        if (phone != null && !phone.trim().isEmpty() && !validator.isNumeric(phone)) {
            AlertBox.display("error", String.format( "phone number: %s is not numeric!", phone));
            return;
        }

        addressee.setName(relationName.getText());
        addressee.setAddress(address);
        addressee.setEmail(Optional.ofNullable(relationEmail.getText()));
        addressee.setPhoneNumber(Optional.ofNullable(relationPhone.getText()));
        List<Addressee> addresseeList = entry.getAddressees().orElse(new ArrayList<>());
        addresseeList.add(addressee);
        entry.setAddressees(Optional.of(addresseeList));
        window.close();
    }

    @VisibleForTesting
    boolean isValidEmail(String email) {
        // pattern found at https://stackoverflow.com/questions/8204680/java-regex-email
        // also cool site for regex regarding mail: http://emailregex.com/
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
