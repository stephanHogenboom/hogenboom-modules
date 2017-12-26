import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.financialmodule.FinancialModule;
import modules.financialmodule.model.FinancialDAO;
import modules.realestate.RealEstateDAO;
import modules.realestate.RealEstateModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App extends Application {

    Stage window;
    Button menuButton;
    List<String> buttonlist = new ArrayList<>(Arrays.asList("Financial", "Real estate"));
    Scene scene;

    public static void main(String... args) {
        FinancialDAO fdao = new FinancialDAO();
        fdao.createEntryTableIfNotExist();
        fdao.createCategorieTableIfNotExist();

        RealEstateDAO rdao = new RealEstateDAO();
        rdao.createPropertyEntryTableIfNotExist();
        rdao.createAddressTableIfNotExist();
        rdao.createTableAddresseeIfNotExist();
        rdao.createPriceHistoryEntryTableIfNotExist();

        //Start the window
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("modules application");
        window.setWidth(250);
        window.setHeight(200);

        // create layout
        VBox layout = new VBox();

        for (String button : buttonlist) {
            menuButton = new Button(button);
            menuButton.setOnAction(e -> getModuleAction(button));
            layout.getChildren().add(menuButton);
        }

        //create scene
        scene = new Scene(layout);
        scene.getStylesheets().addAll("index.css");

        window.setScene(scene);
        window.show();
    }

    private void getModuleAction(String action) {
        switch (action) {
            case "Financial":
                goToFinancialModule();
                break;
            case "Real estate":
                goToRealEstateModule();
                break;
            default:
                goToFinancialModule();
        }
    }

    private void goToFinancialModule() {
        FinancialModule module = new FinancialModule();
        module.display(window, scene);
    }

    private void goToRealEstateModule() {
        RealEstateModule module = new RealEstateModule();
        module.display(window, scene);
    }
}
