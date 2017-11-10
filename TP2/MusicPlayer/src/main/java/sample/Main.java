package sample;

import Model.Playlist;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLFrameElement;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        this.controller = new Controller();

        loader.setController(controller);
        primaryStage.setTitle("LOG8430 - TP2 : Test JavaFX web engine");

        // Create scene with web browser
        Browser browser = new Browser();
        primaryStage.setScene(new Scene(browser));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

class Browser extends Region {

    // DOM de chaque frame HTML
    public Document mainDOM;
    public Document leftPanelDOM;
    public Document mainPanelDOM;
    public Document playlistManagerDOM;
    public Document playerDOM;

    private Controller controller;
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public Browser() {
        //apply the styles
        controller = new Controller();
        getStyleClass().add("browser");

        //
        webEngine.getLoadWorker().stateProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        System.out.println("finished loading");

                        //On récupère les DOM de chaque frame HTML
                        mainDOM  = webEngine.getDocument();
                        HTMLFrameElement frame = (HTMLFrameElement) mainDOM.getElementById("left_panel");
                        leftPanelDOM = frame.getContentDocument();
                        frame = (HTMLFrameElement) mainDOM.getElementById("main_panel");
                        mainPanelDOM = frame.getContentDocument();
                        frame = (HTMLFrameElement) mainDOM.getElementById("playlistManager_panel");
                        playlistManagerDOM = frame.getContentDocument();
                        frame = (HTMLFrameElement) mainDOM.getElementById("player_panel");
                        playerDOM = frame.getContentDocument();
                        initiatePlaylists();

                        Element button = playlistManagerDOM.getElementById("createPlaylist");

                        ((EventTarget) button).addEventListener("click", createPlaylist, false);

                    }
                });

        // load the web page
        URL url = null;
        try {
            url = Paths.get("./src/main/java/view/index.html").toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        webEngine.load(url.toExternalForm());

        //add the web view to the scene
        getChildren().add(browser);

    }
    private void initiatePlaylists() {
        // On récupère l'élément à l'Id "playlists" dans le DOM du left panel
        Element playlists = this.leftPanelDOM.getElementById("playlists");


        // Création de playlists bidon
        for (Playlist playlist : controller.getPlaylistHandler().getPlaylists()) {
            Element p = this.leftPanelDOM.createElement("li");
            p.setTextContent(playlist.getName());

            playlists.appendChild(p);
        }
    }

    private void addPlaylist(String title){
        Element playlists = this.leftPanelDOM.getElementById("playlists");
        Element p = this.leftPanelDOM.createElement("li");
        p.setTextContent(title);

        playlists.appendChild(p);
    }

    EventListener createPlaylist = new EventListener() {
        public void handleEvent(Event ev) {
            System.out.println("Create a playlist !");
            TextInputDialog dialog = new TextInputDialog("Ma playlist");
            dialog.setTitle("Créer votre playlist !");
            dialog.setContentText("Donner un nom à votre playlist");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                System.out.println("Your new playlist: " + result.get());
                controller.createPlaylist(result.get().toString());
                addPlaylist(result.get().toString());
            }
        }
    };




    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 1280;
    }

    @Override protected double computePrefHeight(double width) {
        return 1024;
    }

    public class JavaApplication {
        public void helloWorld(){
            System.out.println("Hello World");
        }

    }
}