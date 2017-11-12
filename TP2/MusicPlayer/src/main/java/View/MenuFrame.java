package View;

import Controller.Controller;
import Model.Exceptions.PlaylistException;
import Model.Playlist;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.util.Optional;

public class MenuFrame extends Frame {

    public MenuFrame(Document doc, Controller c){
        super(doc, c);
    }

    public void addPlaylist(String title){
        Element playlists = DOM.getElementById("playlists");
        Element p = DOM.createElement("li");
        Element button = DOM.createElement("button");
        button.setTextContent(title);
        button.setAttribute("id", title);
//        p.setAttribute("style", "background-color: #002233");
        p.appendChild(button);
        playlists.appendChild(p);
        ((EventTarget) button).addEventListener("click", clickOnPlaylist, false);
    }



    private void initiatePlaylists() {
        // On récupère l'élément à l'Id "playlists" dans le DOM du left panel
        Element playlists = this.DOM.getElementById("playlists");

        for (Playlist playlist : controller.getPlaylistHandler().getPlaylists()) {
            Element p = this.DOM.createElement("li");

            p.setTextContent(playlist.getName());
            playlists.appendChild(p);
        }
    }

    EventListener clickOnPlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            Element button =(Element) ev.getTarget();
            Element li = (Element) button.getParentNode();
//            li.setAttribute("style", "background-color: #009999;");
            String playlistName = button.getAttribute("id").toString();
            controller.displayPlaylistInMain(playlistName);
            System.out.println(playlistName);
        }
    };

    public void changeSelectedColor(){

    }

}
