package Client.Controller;

import Services.ITunesService.ITunesServer;
import Services.JamendoService.JamendoServer;
import Services.PlaylistHandlerService.PlaylistHandlerServer;
import Services.SpotifyService.SpotifyServer;
import Client.View.Browser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main extends Application {

    private Controller controller;

    private Thread spotifyThread;
    private Thread jamendoThread;
    private Thread playlistHandlerThread;
    private Thread iTunesThread;

    @Override
    public void start(Stage primaryStage) throws Exception {
        launchServices();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("LOG8430 - TP3 : MusicPlayer");

        // Create scene with web browser
        Browser browser = new Browser();
        this.controller = new Controller(browser);
        browser.setController(this.controller);
        loader.setController(controller);
        primaryStage.setScene(new Scene(browser));
        primaryStage.show();
        System.out.println("Client interface is launched");

    }

    @Override
    public void stop() throws InterruptedException {
        System.out.println("Stage is closing");
        spotifyThread.join();
        jamendoThread.join();
        iTunesThread.join();
        playlistHandlerThread.join();
        this.controller.isStopped();
        System.exit(0);
    }



    public static void main(String[] args){

        launch(args);


    }

    public void launchServices() {
        try {
            LocateRegistry.createRegistry(8080);
            LocateRegistry.createRegistry(8081);
            LocateRegistry.createRegistry(8082);
            LocateRegistry.createRegistry(8083);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //Creation des nouveaux services
        SpotifyServer spotifyServer = new SpotifyServer();
        JamendoServer jamendoServer = new JamendoServer();
        ITunesServer iTunesServer = new ITunesServer();

        PlaylistHandlerServer playlistHandlerServer = new PlaylistHandlerServer();


        //Lancement des services dans de nouveaux threads
        spotifyThread =  new Thread(spotifyServer);
        jamendoThread = new Thread(jamendoServer);
        iTunesThread = new Thread(iTunesServer);

        playlistHandlerThread = new Thread(playlistHandlerServer);

        spotifyThread.start();
        jamendoThread.start();
        iTunesThread.start();

        playlistHandlerThread.start();

        System.out.println();
    }
}