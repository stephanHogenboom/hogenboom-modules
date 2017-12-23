import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    Stage window;

    public static void main(String... args) {
        //Start the window
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("modules application");


        // create scene and layout
        VBox layout = new VBox();
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.show();
    }
}
