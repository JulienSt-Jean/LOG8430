package View;

import Controller.Controller;
import Model.Playlist;
import Model.Track;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MainFrame extends Frame {

    public MainFrame(Document doc, Controller c){
        super(doc, c);
    }


    /*
     * Dsiplay playlist in the main Frame
     */
    public void displayPlaylist(Playlist playlist){
        Element playlistName = this.DOM.getElementById("playlistName");
        playlistName.setTextContent(playlist.getName());

        Element tracks = this.DOM.getElementById("tracks");

        if(playlist.getListTrack().size() == 0){
            tracks.setTextContent("Cette playlist ne contient pas encore de chanson");
        }
        else {
            for (Track track : playlist.getListTrack()) {
                Element p = this.DOM.createElement("li");
                p.setTextContent("");
                tracks.appendChild(p);
            }
        }

    }
}
