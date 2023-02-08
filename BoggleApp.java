import javafx.application.Application;
import javafx.scene.image.Image;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

import java.util.Objects;

/**
 * The main class of BoggleUI, used to start the game.
 */
public class BoggleApp extends Application {

    /**
     * Start method.  Control of application flow is dictated by JavaFX framework
     *
     * @param primaryStage stage upon which to load GUI elements
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("BoggleUI.fxml")));
            Scene scene = new Scene(root, 1200, 900);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());

            Image icon = new Image("TAAKicon.png");
            primaryStage.getIcons().add(icon);

            primaryStage.setTitle("TAAKtical Word Game v0.5");

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);


            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}