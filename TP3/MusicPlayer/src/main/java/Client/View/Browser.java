package Client.View;

import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import Client.Controller.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFrameElement;


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
            System.out.println(System.getProperty("user.dir"));
            url = Paths.get(System.getProperty("user.dir")+"/src/main/java/Client/View/WebViews/index.html").toUri().toURL();
            webEngine.load(url.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //add the web Client.View to the scene
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
