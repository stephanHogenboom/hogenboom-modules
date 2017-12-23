import elements.AlertBox;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.FinancialModule;

public class App extends Application {

    Stage window;
    Button moduleOneButton;

    public static void main(String... args) {
        //Start the window
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("modules application");


        // create layout
        VBox layout = new VBox();

        moduleOneButton = new Button("to financial module");
        moduleOneButton.setOnAction(e -> goToFinancialModule());


        layout.getChildren().add(moduleOneButton);
        //create scene
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.show();
    }


    private void goToFinancialModule() {
        FinancialModule module = new FinancialModule();
        module.display(window);
    }
}
