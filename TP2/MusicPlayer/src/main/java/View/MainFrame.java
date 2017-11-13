package View;

import Controller.Controller;
import Model.Metadata;
import Model.Playlist;
import Model.ServiceProvider;
import Model.Track;
import jdk.internal.util.xml.impl.Input;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends Frame {

    public MainFrame(Document doc, Controller c){
        super(doc, c);
//        Element searchDiv = this.DOM.getElementById("searchDiv");
//        searchDiv.setAttribute("hidden", "");
        Element playlistDiv = this.DOM.getElementById("playlistDiv");
        playlistDiv.setAttribute("hidden",  "");
    }


    /*
     * Dsiplay playlist in the main Frame
     */
    public void displayPlaylist(Playlist playlist){
        Element searchDiv = this.DOM.getElementById("searchDiv");
        searchDiv.setAttribute("hidden", "");
        Element playlistDiv = this.DOM.getElementById("playlistDiv");
        playlistDiv.removeAttribute("hidden");


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

    public void displaySearchDiv(){
        Element searchDiv = this.DOM.getElementById("searchDiv");
        searchDiv.removeAttribute("hidden");
        Element playlistDiv = this.DOM.getElementById("playlistDiv");
        playlistDiv.setAttribute("hidden", "");

    }


    public void sendInputData(String data){
        System.out.println(data);
        ArrayList<Track> results = controller.getApiHandler().searchTrack(data);

        results.add(new Track(new Metadata("Do I wanna Know","Arctic Monkeys", "AM"), "1", ServiceProvider.SPOTIFY));
        results.add(new Track(new Metadata("Do I wanna Know","Arctic Monkeys", "AM"), "2", ServiceProvider.SPOTIFY));
        results.add(new Track(new Metadata("Do I wanna Know","Arctic Monkeys", "AM"), "2", ServiceProvider.SPOTIFY));
        results.add(new Track(new Metadata("Do I wanna Know","Arctic Monkeys", "AM"), "2", ServiceProvider.SPOTIFY));
        Element searchResult = this.DOM.getElementById("searchResult");
        for(Track track : results){
            System.out.println(track.getId());
            searchResult.appendChild(createTrackHTML(track));
        }

    }

    private Element createTrackHTML(Track track){
        Element t = this.DOM.createElement("a");
        t.setAttribute("class","list-group-item list-group-item-action");

//        Element row = this.DOM.createElement("div");
//        row.setAttribute("class","row");

        Element divTitle = this.DOM.createElement("div");
        Element divOther = this.DOM.createElement("div");

        Element divArtiste = this.DOM.createElement("div");
        Element divAlbum = this.DOM.createElement("div");

        divTitle.setAttribute("class", "col-4");
        divOther.setAttribute("class", "col-4");
        divArtiste.setAttribute("class", "row");
        divAlbum.setAttribute("class", "row");

        divTitle.setTextContent(track.getMetadata().getName());
        divAlbum.setTextContent(track.getMetadata().getAlbum());
        divArtiste.setTextContent(track.getMetadata().getArtists());

        divOther.appendChild(divArtiste);
        divOther.appendChild(divAlbum);

        t.appendChild(divTitle);
        t.appendChild(divOther);



        return t;

    }
}
