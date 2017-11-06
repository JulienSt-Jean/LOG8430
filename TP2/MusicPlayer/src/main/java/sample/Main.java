package sample;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application {

    private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        loader.setController(new Controller());
        primaryStage.setTitle("LOG8430 - TP2 : Test JavaFX web engine");

        // Create scene with web browser
        Browser browser = new Browser();
        primaryStage.setScene(new Scene(browser));

        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }


    class Browser extends Region {

        public Document currentDOM;

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        public Browser() {
            //apply the styles
            getStyleClass().add("browser");

            //
            webEngine.getLoadWorker().stateProperty()
                    .addListener((obs, oldValue, newValue) -> {
                        if (newValue == Worker.State.SUCCEEDED) {
                            System.out.println("finished loading");

                            currentDOM  = webEngine.getDocument();

                            Element button = currentDOM.getElementById("hellobutton");

                            ((EventTarget) button).addEventListener("click", helloClick, false);
                        }
                    }); // addListener()

            // load the web page
            URL urlHello = null;
            try {
                urlHello = Paths.get("./src/main/java/view/index.html").toUri().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            webEngine.load(urlHello.toExternalForm());
            //add the web view to the scene
            getChildren().add(browser);

        }

        EventListener helloClick = new EventListener() {
            public void handleEvent(Event ev) {
                System.out.println("Hello World");

                Element counter = currentDOM.getElementById("counter");
                Integer nb = Integer.parseInt(counter.getTextContent()) + 1;
                counter.setTextContent(nb.toString());
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
}
