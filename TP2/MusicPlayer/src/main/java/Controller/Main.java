package Controller;

import View.Browser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        this.controller = new Controller();

        loader.setController(controller);
        primaryStage.setTitle("LOG8430 - TP2 : Music Player");

        // Create scene with web browser
        Browser browser = new Browser();
        primaryStage.setScene(new Scene(browser));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}