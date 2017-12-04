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

/**
 * Classe "racine" de la Vue. Instancie le Java web engine, charge le code HTML/CSS et récupère les DOM de chaque frame
 */
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

    /**
     * Constructeur
     */
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

    /**
     * Récupère le DOM de chaque frame HTML et instancie les objets Frames correspondant
     */
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

    /**
     * Le MainFrame est la zone d'affichage des pistes et de la recherche
     * @return le DOM du MainFrame
     */
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Le MenuFrame est la zone d'affichage des playlists
     * @return le DOM du MainFrame
     */
    public MenuFrame getMenuFrame() {
        return menuFrame;
    }

    /**
     * Le PlaylistManagerFrame est la zone responsable de l'ajout des playlists
     * @return le DOM du MainFrame
     */
    public PlaylistManagerFrame getPlaylistManagerFrame() {
        return playlistManagerFrame;
    }

    /**
     * Le PlayerFrame est la zone d'affichage du lecteur
     * @return le DOM du MainFrame
     */
    public PlayerFrame getPlayerFrame() {
        return playerFrame;
    }

    /**
     * Set le controlleur
     * @param controller référence sur le contrôleur de l'application
     */
    public void setController(Controller controller){
        this.controller = controller;
    }
}
