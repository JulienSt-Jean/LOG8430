package Controller;

import Api.Spotify.SpotifyHandler;
import SOA.JamendoServer;
import SOA.SpotifyServer;
import View.Browser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        launchServices();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("LOG8430 - TP2 : Music Player");

        // Create scene with web browser
        Browser browser = new Browser();
        this.controller = new Controller(browser);
        browser.setController(this.controller);
        loader.setController(controller);
        primaryStage.setScene(new Scene(browser));
        primaryStage.show();
        System.out.println("Client interface is launched");

    }



    public static void main(String[] args) {

        launch(args);
    }

    public void launchServices() {
        try {
            LocateRegistry.createRegistry(8080);
            LocateRegistry.createRegistry(8081);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        SpotifyServer spotifyServer = new SpotifyServer();
        JamendoServer jamendoServer = new JamendoServer();


        Thread spotifyThread =  new Thread(spotifyServer);
        Thread jamendoThread = new Thread(jamendoServer);

        spotifyThread.start();
        jamendoThread.start();

        System.out.println();
    }
}