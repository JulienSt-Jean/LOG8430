package View;

import Controller.Controller;
import Model.Metadata;
import Model.Playlist;
import Model.Track;
import javafx.scene.control.ChoiceDialog;
import jdk.internal.util.xml.impl.Input;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public class MainFrame extends Frame {

    ArrayList<Track> resultTrack;
    Playlist selectedPlaylist;

    public MainFrame(Document doc, Controller c){
        super(doc, c);
//        Element searchDiv = this.DOM.getElementById("searchDiv");
//        searchDiv.setAttribute("hidden", "");
        resultTrack = new ArrayList<>();
        selectedPlaylist = null;
        Element playlistDiv = this.DOM.getElementById("playlistDiv");
        playlistDiv.setAttribute("hidden",  "");

        Element playSelectedPlaylist = this.DOM.getElementById("playPlaylist");
        Element removeSelectedPlaylist = this.DOM.getElementById("removePlaylist");
        ((EventTarget) playSelectedPlaylist).addEventListener("click", playPlaylist, false);
        ((EventTarget) removeSelectedPlaylist).addEventListener("click", removePlaylist, false);



    }


    /*
     * Display playlist in the main Frame
     */
    public void displayPlaylist(Playlist playlist){


        Element searchDiv = this.DOM.getElementById("searchDiv");
        searchDiv.setAttribute("hidden", "");
        Element playlistDiv = this.DOM.getElementById("playlistDiv");
        playlistDiv.removeAttribute("hidden");

        selectedPlaylist = playlist;


        Element playlistName = this.DOM.getElementById("playlistName");
        playlistName.setTextContent(playlist.getName());

        Node tracks = this.DOM.getElementById("tracks");

        //On reset la list des chanson
        tracks.setTextContent("");



        if(playlist.getListTrack().size() == 0){
            tracks.setTextContent("Cette playlist ne contient pas encore de chanson");
        }
        else {
            for (Track track : playlist.getListTrack()) {
                Element p = this.DOM.createElement("li");
                p.setTextContent(track.getMetadata().getName());
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
        resultTrack.clear();
        resultTrack.addAll(controller.getApiHandler().searchTrack(data));


        resultTrack.add(new Track(new Metadata("Do I wanna Know","Arctic Monkeys", "AM"), "1", "Spotify"));
        resultTrack.add(new Track(new Metadata("Cornerstone","Arctic Monkeys", "AM"), "2", "Spotify"));
        resultTrack.add(new Track(new Metadata("505","Arctic Monkeys", "AM"), "3", "Spotify"));
        resultTrack.add(new Track(new Metadata("Piledriver Waltz","Arctic Monkeys", "AM"), "4", "Spotify"));
        Node searchResult = this.DOM.getElementById("searchResult");

        //On reset les résultats de la recherche
        searchResult.setTextContent("");
        for(Track track : resultTrack){
            System.out.println(track.getId());
            searchResult.appendChild(createTrackHTML(track));
        }

    }

    private Element createTrackHTML(Track track){
        Element t = this.DOM.createElement("a");
        t.setAttribute("class","list-group-item list-group-item-action");

        Element buttonAdd = this.DOM.createElement("button");
        buttonAdd.setAttribute("id", track.getId());
        ((EventTarget) buttonAdd).addEventListener("click", addTrackToPlaylist, false);


        Element plusImg = this.DOM.createElement("img");
        plusImg.setAttribute("src", "png/015-plus-black.png");
        plusImg.setAttribute("id", track.getId());
        buttonAdd.appendChild(plusImg);



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
        t.appendChild(buttonAdd);
        return t;

    }
    EventListener addTrackToPlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            Element button = (Element) ev.getTarget();
//            String trackId = button.getAttribute("id").toString();

            System.out.println("track :" + button.getAttribute("id").toString());
            choosePlaylist(button.getAttribute("id").toString());

        }
    };
    EventListener playPlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            System.out.println("button play selected playlist pressed");
            controller.getPlaylistHandler().playPlaylist(selectedPlaylist);

        }
    };
    EventListener removePlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            System.out.println("button play remove playlist pressed");
            controller.removePlaylist(selectedPlaylist);
            Node tracks = DOM.getElementById("tracks");

            //On reset la list des chanson
            tracks.setTextContent("");


        }
    };

    private void choosePlaylist(String id){
        ArrayList<String> playlistName = new ArrayList<String>();
        for(Playlist p : controller.getPlaylistHandler().getPlaylists()){
            playlistName.add(p.getName());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("", playlistName);
        dialog.setTitle("Choissez la playlist");
        dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Choose your letter:");

        Optional<String> result = dialog.showAndWait();

        //Result de la popup
        if (result.isPresent()){
            System.out.println("Your choice: " + result.get());
            Playlist selectedPlaylist = this.controller.getPlaylistHandler().getPlaylistByName(result.get());
            Track trackToAdd = getTrackById(id);

            controller.addSongtoPlaylist(trackToAdd, selectedPlaylist);
        }

    }

    private Track getTrackById(String id){
        for(Track t : resultTrack){
            if(t.getId().equals(id)){
                return t;
            }
        }
        return null;
    }

}
