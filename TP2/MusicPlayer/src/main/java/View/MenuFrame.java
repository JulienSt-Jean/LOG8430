package View;

import Controller.Controller;
import Model.Exceptions.PlaylistException;
import Model.Playlist;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.util.Optional;

public class MenuFrame extends Frame {

    public MenuFrame(Document doc, Controller c){
        super(doc, c);
        Element searchButton = DOM.getElementById("browseButton");
        ((EventTarget)searchButton).addEventListener("click", search, false);


    }

    public void addPlaylist(String title){
        Element playlists = DOM.getElementById("playlists");
        Element row = DOM.createElement("tr");
        Element imgP = DOM.createElement("td");
        imgP.setAttribute("class","bullet");
        Element p = DOM.createElement("td");
        Element button = DOM.createElement("button");

        Element playlistImg = DOM.createElement("img");

        button.setAttribute("id", title);

        ((EventTarget) button).addEventListener("click", clickOnPlaylist, false);
        button.setTextContent(title);


        playlistImg.setAttribute("src", "png/playlist.png");
        imgP.appendChild(playlistImg);
        p.appendChild(button);
        row.appendChild(imgP);
        row.appendChild(p);
        playlists.appendChild(row);

    }

    public void updatePlaylists(){
        Element playlists = DOM.getElementById("playlists");
        playlists.setTextContent("");
        for(Playlist playlist : controller.getPlaylistHandler().getPlaylists()){
            addPlaylist(playlist.getName());
        }

    }


//
//    private void initiatePlaylists() {
//        // On récupère l'élément à l'Id "playlists" dans le DOM du left panel
//        Element playlists = this.DOM.getElementById("playlists");
//
//        for (Playlist playlist : controller.getPlaylistHandler().getPlaylists()) {
//            Element p = this.DOM.createElement("tr");
//
//            p.setTextContent(playlist.getName());
//            playlists.appendChild(p);
//        }
//    }

    EventListener clickOnPlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            Element button =(Element) ev.getTarget();

            String playlistName;
            if(!button.hasChildNodes()){
                playlistName = ((Element)button.getParentNode()).getAttribute("id").toString();
            }
            else{
                playlistName = button.getAttribute("id").toString();
            }

            controller.displayPlaylistInMain(playlistName);
            System.out.println(playlistName);
        }
    };

    EventListener search = new EventListener() {
        @Override
        public void handleEvent(Event event) {
            System.out.println("click on searchButton");
            controller.displaySearchDiv();

        }
    };



    public void changeSelectedColor(){

    }

}
