package View;

import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLFrameElement;
import Controller.Controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;


public class Browser extends Region {

    public Document mainDOM;
    private MainFrame mainFrame;
    private MenuFrame menuFrame;
    private PlaylistManagerFrame playlistManagerFrame;
    private PlayerFrame playerFrame;

    private Controller controller;
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();


    // Override methods
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



    public Browser() {

        this.controller = null;

        getStyleClass().add("browser");

        webEngine.getLoadWorker().stateProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        System.out.println("Web engine finished loading");

                        mainDOM  = webEngine.getDocument();
                        this.findFrameDOMs();




                    }
                    if(Worker.State.SUCCEEDED == newValue){
                        webEngine.setOnStatusChanged(webEvent -> {
                            mainFrame.sendInputData(webEvent.getData());
                        });
                    }
                });

        // load the web page
        URL url = null;
        try {
            url = Paths.get("./src/main/java/View/WebViews/index.html").toUri().toURL();
            webEngine.load(url.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //add the web View to the scene
        getChildren().add(browser);

    }

    private void findFrameDOMs(){
        HTMLFrameElement frame = (HTMLFrameElement) mainDOM.getElementById("frame_menu");
        menuFrame = new MenuFrame(frame.getContentDocument(), controller);
        frame = (HTMLFrameElement) mainDOM.getElementById("frame_main");
        mainFrame = new MainFrame(frame.getContentDocument(), controller);
        frame = (HTMLFrameElement) mainDOM.getElementById("frame_playlistManager");
        playlistManagerFrame = new PlaylistManagerFrame(frame.getContentDocument(), controller);
        frame = (HTMLFrameElement) mainDOM.getElementById("frame_player");
        playerFrame = new PlayerFrame(frame.getContentDocument(), controller);
    }



    /*
    private void initiatePlaylists() {
        // On récupère l'élément à l'Id "playlists" dans le DOM du left panel
        Element playlists = this.leftPanelDOM.getElementById("playlists");


        // Création de playlists bidon
        for (Playlist playlist : controller.getPlaylistHandler().getPlaylists()) {
            Element p = this.leftPanelDOM.createElement("li");
            p.setTextContent(playlist.getName());

            playlists.appendChild(p);
        }
    }*/

    /*
    EventListener handlePlaylist = new EventListener(){
        public void handleEvent(Event ev) {
            System.out.println(ev.getTarget());
            System.out.println(((Element)ev.getTarget()).getAttribute("id"));

        }
    };*/

    /*
    EventListener createPlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            TextInputDialog dialog = new TextInputDialog("Ma playlist");
            dialog.setTitle("Créer votre playlist !");
            dialog.setContentText("Donner un nom à votre playlist");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){


                try {
                    controller.createPlaylist(result.get());
                    leftPanel.addPlaylist(result.get());
                } catch (PlaylistException e) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText("Une playlist existe déja avec le même nom");


                    Optional<ButtonType> r = alert.showAndWait();
                    if (r.get() == ButtonType.OK){
                        // ... user chose OK
                    } else {
                        // ... user chose CANCEL or closed the dialog
                    }
                }

            }
        }
    };
    */

    public Document getMainDOM() {
        return mainDOM;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public MenuFrame getMenuFrame() {
        return menuFrame;
    }

    public PlaylistManagerFrame getPlaylistManagerFrame() {
        return playlistManagerFrame;
    }

    public PlayerFrame getPlayerFrame() {
        return playerFrame;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
}
