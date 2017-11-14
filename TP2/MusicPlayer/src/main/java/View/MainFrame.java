package View;

import Controller.Controller;
import Model.Metadata;
import Model.Playlist;
import Model.Track;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.SelectionMode;
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


    /**
     * Display the playlist in the main frame
     * @param playlist
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
                Element p = this.DOM.createElement("a");
                p.setAttribute("class","list-group-item list-group-item-action");

                p.setTextContent(track.getMetadata().getName());
                Element deleteButton = this.DOM.createElement("button");
                deleteButton.setAttribute("id", track.getId());
                ((EventTarget) deleteButton).addEventListener("click", deleteTrackFromPlaylist, false);


                Element deleteImg = this.DOM.createElement("img");
                deleteImg.setAttribute("src","png/013-cancel.png");
                deleteImg.setAttribute("width","30%");
                deleteImg.setAttribute("height","30%");
                deleteImg.setAttribute("id", track.getId());
                deleteButton.appendChild(deleteImg);
                p.appendChild(deleteButton);
                tracks.appendChild(p);
            }
        }

    }

    /**
     *
     */
    EventListener deleteTrackFromPlaylist = new EventListener() {
        public void handleEvent(Event ev) {
            System.out.println("button delete track from selected playlist pressed");
            String id = ((Element)ev.getTarget()).getAttribute("id");
            System.out.println("Track : "+id+" to be removed");
            controller.removeTrackFromPlaylist(id, selectedPlaylist.getName());

        }
    };



    /**
     * Display the search Division and hide the division of playlist
     */
    public void displaySearchDiv(){
        Element searchDiv = this.DOM.getElementById("searchDiv");
        searchDiv.removeAttribute("hidden");
        Element playlistDiv = this.DOM.getElementById("playlistDiv");
        playlistDiv.setAttribute("hidden", "");

    }


    /**
     * Get the result of the input in the search bar and send the search query to the api
     * @param data
     */
    public void sendInputData(String data){
        System.out.println(data);
        resultTrack.clear();
        resultTrack.addAll(controller.getApiHandler().searchTrack(data));


        //Creation de resultats bidons pour les tests
        resultTrack.add(new Track(new Metadata("Do I wanna Know","Arctic Monkeys", "AM"), "1", "Spotify"));
        resultTrack.add(new Track(new Metadata("Cornerstone","Arctic Monkeys", "AM"), "2", "Spotify"));
        resultTrack.add(new Track(new Metadata("505","Arctic Monkeys", "AM"), "3", "Spotify"));
        resultTrack.add(new Track(new Metadata("Piledriver Waltz","Arctic Monkeys", "AM"), "4", "Spotify"));
        resultTrack.add(new Track(new Metadata("vhkbjnf","Afvrg", "cderf"), "5", "Spotify"));
        resultTrack.add(new Track(new Metadata("cdfv","Afvrfvgbg", "gvbrf"), "6", "Spotify"));
        resultTrack.add(new Track(new Metadata("erftg","zedfrrfvgbg", "brf"), "7", "Spotify"));


        Node searchResult = this.DOM.getElementById("searchResult");

        //On reset les résultats de la recherche
        searchResult.setTextContent("");
        for(Track track : resultTrack){
            System.out.println(track.getId());
            searchResult.appendChild(createTrackHTML(track));
        }

    }

    /**
     * Create an element of the searsh result list
     * @param track
     * @return
     */
    private Element createTrackHTML(Track track){
        Element t = this.DOM.createElement("row");
        t.setAttribute("class","list-group-item list-group-item-action");

        Element buttonAdd = this.DOM.createElement("button");
        buttonAdd.setAttribute("id", track.getId());
        ((EventTarget) buttonAdd).addEventListener("click", addTrackToPlaylist, false);


        Element plusImg = this.DOM.createElement("img");
        plusImg.setAttribute("src", "png/015-plus-black.png");
        plusImg.setAttribute("id", track.getId());
        buttonAdd.appendChild(plusImg);


        Element divTitle = this.DOM.createElement("div");
        Element divOther = this.DOM.createElement("div");

        Element divArtiste = this.DOM.createElement("div");
        Element divAlbum = this.DOM.createElement("div");

        Element divButton = this.DOM.createElement("div");

        divTitle.setAttribute("class", "col-4");

        divArtiste.setAttribute("class", "col-4");
        divAlbum.setAttribute("class", "col-4");
        divButton.setAttribute("class", "col-4");


        divTitle.setTextContent(track.getMetadata().getName());
        divAlbum.setTextContent(track.getMetadata().getAlbum());
        divArtiste.setTextContent(track.getMetadata().getArtists());
        divButton.appendChild(buttonAdd);

        t.appendChild(divTitle);
        t.appendChild(divAlbum);
        t.appendChild(divArtiste);
        t.appendChild(divButton);
        return t;

    }

    /**
     * Is called when the + button is click on a search result track
     */
    EventListener addTrackToPlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            Element button = (Element) ev.getTarget();
//            String trackId = button.getAttribute("id").toString();

            System.out.println("track :" + button.getAttribute("id").toString());
            choosePlaylist(button.getAttribute("id").toString());

        }
    };

    /**
     * Play the current playlist
     * Is called when the play button is pressed  is the playlist div
     */
    EventListener playPlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            System.out.println("button play selected playlist pressed");
            controller.playPlaylist(selectedPlaylist);

        }
    };

    /**
     * Delete the current playlist
     * Is called when the delete button is pressed  is the playlist div
     */
    EventListener removePlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            System.out.println("button play remove playlist pressed");
            controller.removePlaylist(selectedPlaylist);
            Node tracks = DOM.getElementById("tracks");

            //On reset la list des chanson
            tracks.setTextContent("");


        }
    };

    /**
     * Display a popup in which we choose to which playlist add the song id.
     * @param id
     */
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
