package modules.realestate;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.Module;

public class AddOrEditRelation extends Module {
    Stage window;

    public void display() {
        window = new Stage();
        window.setTitle("add a relation");

        window.setMinWidth(1000);
        window.setMinHeight(1000);


        VBox vbox = new VBox();



        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();
    }
}
