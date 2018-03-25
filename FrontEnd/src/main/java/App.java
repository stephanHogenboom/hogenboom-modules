import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.financialmodule.FinancialModule;
import modules.realestate.RealEstateOverView;
import modules.toDoList.ToDoListOverview;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.EnvironmentUtils.getEnvOrPropertyOrDefault;

@SpringBootApplication
public class App extends Application {

    private Stage window;
    private Button menuButton;
    private List<String> buttonlist = new ArrayList<>(Arrays.asList("Financial"));
    private Scene scene;
    private final BorderPane main = new BorderPane();
    private TabPane tabMainScreen = new TabPane();
    private static final String dataBaseUrl = getEnvOrPropertyOrDefault("CONNECTION_STRING", "jdbc:sqlite:financial.db");
    private static final String migrationLocation = getEnvOrPropertyOrDefault("MIGRATION_LOCATION", "classpath:db/migration");
    private final RealEstateOverView realEstateOverViewInitObject = new RealEstateOverView();
    private final ToDoListOverview toDoListOverviewInitObject = new ToDoListOverview();

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
        window.setWidth(1250);
        window.setHeight(800);

        // create layout
        VBox layout = new VBox();

        Tab tab = new Tab();
        tab.setText("real estate");
        BorderPane realEstateLayout = realEstateOverViewInitObject.initRealEstateModule();
        tab.setContent(realEstateLayout);
        tabMainScreen.getTabs().add(tab);

        Tab tab2 = new Tab();
        tab2.setText("to do list");
        BorderPane toDoList = toDoListOverviewInitObject.display();
        tab2.setContent(toDoList);
        tabMainScreen.getTabs().add(tab2);

        for (String button : buttonlist) {
            menuButton = new Button(button);
            menuButton.setOnAction(e -> getModuleAction(button));
            layout.getChildren().add(menuButton);
        }

        main.setLeft(layout);
        main.setCenter(tabMainScreen);
        //create scene
        scene = new Scene(main);
        scene.getStylesheets().addAll("index.css");

        window.setScene(scene);
        window.show();
    }

    private void getModuleAction(String action) {
        switch (action) {
            case "Financial":
                goToFinancialModule();
                break;
            default:
                goToFinancialModule();
        }
    }

    private void goToFinancialModule() {
        FinancialModule module = new FinancialModule();
        module.display(window, scene);
    }

}
