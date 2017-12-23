package modules.realestate;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RealEstateModule {
    Stage window;

    public void display(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Real Estate module");

        // the menu
        Menu fileMenu = new Menu("file");

        //the items
        fileMenu.getItems().add(new MenuItem("new property..."));
        fileMenu.getItems().add(new MenuItem("swap module"));

        // main menu bar
        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(fileMenu);


        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }
}
