package View;

import Controller.Controller;
import Model.Exceptions.PlaylistException;
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
        ((EventTarget) button).addEventListener("click", createPlaylist, false);
    }




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

}
