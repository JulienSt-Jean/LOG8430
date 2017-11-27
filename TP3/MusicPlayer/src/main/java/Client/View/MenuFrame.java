package Client.View;

import Client.Controller.Controller;
import Shared.Model.Playlist;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.util.ArrayList;

public class MenuFrame extends Frame {

    public MenuFrame(Document doc, Controller c){
        super(doc, c);
        Element searchButton = DOM.getElementById("browseButton");
        ((EventTarget)searchButton).addEventListener("click", browse, false);
    }

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

    public void updatePlaylists(ArrayList<Playlist> playlists){
        Element p = DOM.getElementById("playlists");
        p.setTextContent("");
        for(Playlist playlist : playlists){
            addPlaylist(playlist.getName());
        }

    }

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

    EventListener browse = new EventListener() {
        @Override
        public void handleEvent(Event event) {
            controller.browseClicked();
        }
    };
}