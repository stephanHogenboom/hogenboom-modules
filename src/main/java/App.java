import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.financialmodule.FinancialModule;
import modules.financialmodule.model.FinancialDAO;
import modules.realestate.RealEstateModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App extends Application {

    Stage window;
    Button menuButton;
    List<String> buttonlist = new ArrayList<>( Arrays.asList( "Finacial", "Real estate") );

    public static void main(String... args) {
        FinancialDAO dao = new FinancialDAO();
        dao.createEntryTableIfNotExist();
        dao.createCategorieTableIfNotExist();

        //Start the window
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle( "modules application" );
        window.setWidth( 500 );
        window.setHeight( 500 );

        // create layout
        VBox layout = new VBox();

        for( String button : buttonlist ){
            menuButton = new Button( button );
            menuButton.setOnAction( e -> getModuleAction( button ) );
            layout.getChildren().add( menuButton );
        }

        //create scene
        Scene scene = new Scene( layout );

        window.setScene(scene);
        window.show();
    }

    private void getModuleAction( String action ) {
        switch( action ){
            case "Financial": goToFinancialModule();
                break;
            case "Real estate": goToRealEstateModule();
                break;
            default: goToFinancialModule();
        }
    }

    private void goToFinancialModule() {
        FinancialModule module = new FinancialModule();
        module.display(window);
    }

    private void goToRealEstateModule() {
        RealEstateModule module = new RealEstateModule();
        module.display(window);
    }
}
