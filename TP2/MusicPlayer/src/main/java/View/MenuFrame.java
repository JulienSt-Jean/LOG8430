package View;

import Controller.Controller;
import Model.Playlist;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * Vue du Menu de l'application, pour choisir d'afficher une playlist ou l'onglet de recherche
 */
public class MenuFrame extends Frame {

    /**
     * Constructeur
     * @param doc DOM du frame HTML
     * @param c référence sur le Contrôleur de l'application
     */
    public MenuFrame(Document doc, Controller c){
        super(doc, c);
        Element searchButton = DOM.getElementById("browseButton");
        ((EventTarget)searchButton).addEventListener("click", browse, false);
    }

    /**
     * Ajout d'une playlist dans le menu
     * @param title titre de la playlist
     */
    public void addPlaylist(String title){
        Element playlists = DOM.getElementById("playlists");
        Element row = DOM.createElement("tr");
        Element imgP = DOM.createElement("td");
        Element p = DOM.createElement("td");
        Element button = DOM.createElement("button");
        Element playlistImg = DOM.createElement("img");

        imgP.setAttribute("class","bullet");
        playlistImg.setAttribute("src", "png/playlist.png");
        button.setAttribute("id", title);
        button.setTextContent(title);

        ((EventTarget) button).addEventListener("click", clickOnPlaylist, false);

        imgP.appendChild(playlistImg);
        p.appendChild(button);
        row.appendChild(imgP);
        row.appendChild(p);
        playlists.appendChild(row);
    }

    /**
     * Met à jour la liste de playlists présentes dans le menu
     */
    public void updatePlaylists(){
        Element playlists = DOM.getElementById("playlists");
        playlists.setTextContent("");
        for(Playlist playlist : controller.getPlaylistHandler().getPlaylists()){
            addPlaylist(playlist.getName());
        }
    }

    /**
     * Action à effectuer lors du clic sur une playlist
     */
    EventListener clickOnPlaylist = new EventListener() {
        public void handleEvent(Event ev) {
            Element button =(Element) ev.getTarget();
            String playlistName;

            if(!button.hasChildNodes())
                playlistName = ((Element)button.getParentNode()).getAttribute("id");
            else
                playlistName = button.getAttribute("id");

            controller.selectPlaylist(playlistName);
        }
    };

    /**
     * Action à effectuer lors du clic sur l'onglet browse
     */
    EventListener browse = new EventListener() {
        @Override
        public void handleEvent(Event event) {
            controller.browseClicked();
        }
    };
}