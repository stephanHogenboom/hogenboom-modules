package main.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.modules.financialmodule.FinancialModule;
import main.java.modules.realestate.RealEstateOverView;
import main.java.modules.toDoList.ToDoListOverview;
import org.flywaydb.core.Flyway;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static main.java.util.EnvironmentUtils.getEnvOrPropertyOrDefault;

public class App extends Application {

    private Stage window;
    private Button menuButton;
    private List<String> buttonlist = new ArrayList<>(Arrays.asList("Financial", "Real estate", "To Do List"));
    private Scene scene;
    private static final String dataBaseUrl = getEnvOrPropertyOrDefault("BASE_URL", "jdbc:sqlite:financial.db");
    private static final String migrationLocation = getEnvOrPropertyOrDefault("MIGRATION_LOCATION", "classpath:db/migration");

    public static void main(String... args) {
        configureFlywayAndMigrateDataBase();
        //Start the window
        launch(args);
    }

    private static void configureFlywayAndMigrateDataBase() {
        try {
            Flyway flyway = new Flyway();
            SQLiteConfig config = new SQLiteConfig();
            config.setJournalMode(SQLiteConfig.JournalMode.WAL);
            config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(dataBaseUrl);
            flyway.setLocations(migrationLocation);
            flyway.setDataSource(dataSource);
            flyway.migrate();
        } catch (Exception e) {
            System.err.println("some thing went wrong during the migration of the dataBase.");
            System.out.println("Does the base url point to your db? Or is your migration out of sync?");
            System.exit(1);
        }
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
        scene.getStylesheets().addAll("main/resources/index.css");

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
            case "To Do List":
                goToToDoListModule();
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
        RealEstateOverView module = new RealEstateOverView();
        module.display(window, scene);
    }

    private void goToToDoListModule() {
        ToDoListOverview module = new ToDoListOverview();
        module.display(window, scene);
    }
}
