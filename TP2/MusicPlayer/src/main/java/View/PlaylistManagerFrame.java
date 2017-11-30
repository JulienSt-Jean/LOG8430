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

/**
 * Affiche le bouton d'ajout d'une playlist et les pop-up liées à cette action
 */
public class PlaylistManagerFrame extends Frame {

    /**
     * Constructeur
     * @param doc DOM du frame HTML
     * @param c référence sur le Contrôleur de l'application
     */
    public PlaylistManagerFrame(Document doc, Controller c){
        super(doc, c);
        Element button = this.DOM.getElementById("createPlaylist");
        ((EventTarget) button).addEventListener("click", addClicked, false);
    }

    /**
     * Action à effectuer pour l'ajout d'une playlist
     */
    EventListener addClicked = new EventListener() {
        public void handleEvent(Event ev) {
            Optional<String> name = showCreationDialog();
            if (name.isPresent())
                controller.createPlaylist(name.get());
        }
    };

    /**
     * Affiche un message d'erreur lorsque le nom de la playlist à créer existe déjà
     */
    public void showCreationWarning(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Avertissement");
        alert.setHeaderText("Une playlist existe déja avec le même nom");
        Optional<ButtonType> r = alert.showAndWait();
    }

    /**
     * Affiche un champ de saisie pour le nom de la playlist à créer
     * @return nom choisi
     */
    private  Optional<String> showCreationDialog(){
        TextInputDialog dialog = new TextInputDialog("Ma playlist");
        dialog.setTitle("Créer une playlist !");
        dialog.setHeaderText("");
        dialog.setContentText("Donnez un nom à votre playlist");

        // Traditional way to get the response value.
        return dialog.showAndWait();
    }
}
