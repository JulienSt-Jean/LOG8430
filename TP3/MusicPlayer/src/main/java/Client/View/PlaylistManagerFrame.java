package Client.View;

import Client.Controller.Controller;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.util.Optional;

public class PlaylistManagerFrame extends Frame {

    public PlaylistManagerFrame(Document doc, Controller c){
        super(doc, c);
        Element button = this.DOM.getElementById("createPlaylist");
        ((EventTarget) button).addEventListener("click", addClicked, false);
    }

    EventListener addClicked = new EventListener() {
        public void handleEvent(Event ev) {
            Optional<String> name = showCreationDialog();
            if (name.isPresent())
                controller.createPlaylist(name.get());
        }
    };

    public void showCreationWarning(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Avertissement");
        alert.setHeaderText("Une playlist existe déja avec le même nom");
        Optional<ButtonType> r = alert.showAndWait();
    }

    private  Optional<String> showCreationDialog(){
        TextInputDialog dialog = new TextInputDialog("Ma playlist");
        dialog.setTitle("Créer une playlist !");
        dialog.setHeaderText("");
        dialog.setContentText("Donnez un nom à votre playlist");

        // Traditional way to get the response value.
        return dialog.showAndWait();
    }
}
