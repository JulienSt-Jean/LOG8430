package View;

import Controller.Controller;
import Model.Metadata;
import Model.Playlist;
import Model.ServiceProvider;
import Model.Track;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

/**
 * Classe de gestion du Main frame (affichage des tracks et de la barre de recherche
 */
public class MainFrame extends Frame {

    private ArrayList<Track> resultTrack;
    private Playlist selectedPlaylist;

    // Differentes régions du frame
    private Element playlistDiv;
    private Element searchDiv;
    private Element tracks;

    /**
     * Constructeur
     * @param doc Référence sur le DOM du frame
     * @param c Référence sur le Controlleur de l'application
     */
    public MainFrame(Document doc, Controller c){
        super(doc, c);

        resultTrack = new ArrayList<>();
        selectedPlaylist = null;
        playlistDiv = this.DOM.getElementById("playlistDiv");
        searchDiv = this.DOM.getElementById("searchDiv");
        tracks = this.DOM.getElementById("tracks");

        // Bind des contrôles de la playlist courante
        Element playSelectedPlaylist = this.DOM.getElementById("playPlaylist");
        Element removeSelectedPlaylist = this.DOM.getElementById("removePlaylist");
        ((EventTarget) playSelectedPlaylist).addEventListener("click", playPlaylist, false);
        ((EventTarget) removeSelectedPlaylist).addEventListener("click", removePlaylist, false);
    }

    /**
     * Display the current playlist in the main frame
     * @param playlist Playlist courante à afficher
     */
    public void displayPlaylist(Playlist playlist){

        searchDiv.setAttribute("hidden", "");
        playlistDiv.removeAttribute("hidden");

        selectedPlaylist = playlist;

        Element playlistName = this.DOM.getElementById("playlistName");
        playlistName.setTextContent(playlist.getName());

        //On reset la liste des chanson
        tracks.setTextContent("");

        if(playlist.getListTrack().size() == 0)
            tracks.setTextContent(" Cette playlist ne contient pas encore de chanson");
        else {
            for (Track track : playlist.getListTrack()) {
                Element newTrack = createTrackHTML(track, true);
                tracks.appendChild(newTrack);
            }
        }
    }

    /**
     * Display the search Division and hide the division of playlist
     */
    public void displaySearchDiv(){
        searchDiv.removeAttribute("hidden");
        playlistDiv.setAttribute("hidden", "");
        tracks.setTextContent("");
    }

    /**
     * Get the result of the input in the search bar and send the search query to the api
     * @param data
     */
    public void sendInputData(String data){
        System.out.println(data);

        resultTrack.clear();
        resultTrack.addAll(controller.getApiHandler().searchTrack(data));
        System.out.println(resultTrack.toString());


        //On reset les résultats de la recherche
        tracks.setTextContent("");
        for(Track track : resultTrack){

            System.out.println(track.getId());
            tracks.appendChild(createTrackHTML(track, false));
        }
    }

    /**
     * Create an element of the search result list
     * @param track
     * @param playlistView True if we are displaying tracks in a playlist
     * @return Element t
     */
    private Element createTrackHTML(Track track, boolean playlistView){
        Element t = this.DOM.createElement("row");
        t.setAttribute("class","list-group-item list-group-item-action");

        Element addButton;
        Element playButton = createPlayButton(track.getId());
        if (playlistView)
            addButton = createDeleteButton(track.getId());
        else
            addButton = createAddButton(track.getId());


        Element divPlayButton = this.DOM.createElement("entry");
        Element divAddButton = this.DOM.createElement("entry");
        Element divTitle = this.DOM.createElement("entry");
        Element divArtiste = this.DOM.createElement("entry");
        Element divAlbum = this.DOM.createElement("entry");

        divTitle.setAttribute("class", "col-4");
        divArtiste.setAttribute("class", "col-4");
        divAlbum.setAttribute("class", "col-4");


        divTitle.setTextContent(track.getMetadata().getName());
        divAlbum.setTextContent(track.getMetadata().getAlbum());
        divArtiste.setTextContent(track.getMetadata().getArtists());
        divPlayButton.appendChild(playButton);
        divAddButton.appendChild(addButton);

        t.appendChild(divPlayButton);
        t.appendChild(divAddButton);
        t.appendChild(divTitle);
        t.appendChild(divAlbum);
        t.appendChild(divArtiste);
        return t;
    }

    /**
     * Display a popup in which we choose to which playlist add the song id.
     * @param id
     */
    private void choosePlaylist(String id){
        if(controller.getPlaylistHandler().getPlaylists().size() == 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Avertissement");
            alert.setHeaderText("Il n'existe pas encore de playlist");
            Optional<ButtonType> r = alert.showAndWait();
        }
        else{
            ArrayList<String> playlistName = new ArrayList<String>();
            for(Playlist p : controller.getPlaylistHandler().getPlaylists())
                playlistName.add(p.getName());

            ChoiceDialog<String> dialog = new ChoiceDialog<>(playlistName.get(0), playlistName);
            dialog.setTitle("Ajouter à une playlist");
            dialog.setHeaderText("");
            dialog.setContentText("Choisissez une playlist: ");

            Optional<String> result = dialog.showAndWait();

            //Resultat de la popup
            if (result.isPresent()){
                System.out.println("Your choice: " + result.get());
                Playlist selectedPlaylist = this.controller.getPlaylistHandler().getPlaylistByName(result.get());
                Track trackToAdd = getTrackById(id);

                controller.addSongtoPlaylist(trackToAdd, selectedPlaylist);
            }
        }

    }

    /**
     * Trouver une track à partir d'un id
     * @param id
     * @return
     */
    private Track getTrackById(String id){
        for(Track t : resultTrack)
            if(t.getId().equals(id))
                return t;

        return null;
    }

    /**
     * Crée un bouton de type "delete"
     * @param trackId Id de la track associée au bouton à créer
     * @return Le bouton créé
     */
    public Element createDeleteButton(String trackId){
        Element buttonDelete = this.DOM.createElement("button");
        buttonDelete.setAttribute("id", trackId);
        ((EventTarget) buttonDelete).addEventListener("click", deleteTrackFromPlaylist, false);

        Element deleteImg = this.DOM.createElement("img");
        deleteImg.setAttribute("src","png/013-cancel.png");

        deleteImg.setAttribute("id", trackId);
        buttonDelete.appendChild(deleteImg);

        return buttonDelete;
    }

    /**
     * Crée un bouton de type "add"
     * @param trackId Id de la track associée au bouton à créer
     * @return Le bouton créé
     */
    public Element createAddButton(String trackId){
        Element buttonAdd = this.DOM.createElement("button");
        buttonAdd.setAttribute("id", trackId);
        ((EventTarget) buttonAdd).addEventListener("click", addTrackToPlaylist, false);

        Element plusImg = this.DOM.createElement("img");
        plusImg.setAttribute("src", "png/015-plus-black.png");
        plusImg.setAttribute("id", trackId);
        buttonAdd.appendChild(plusImg);

        return buttonAdd;
    }

    /**
     * Crée un bouton de type "play"
     * @param trackId Id de la track associée au bouton à créer
     * @return Le bouton créé
     */
    public Element createPlayButton(String trackId){
        Element buttonAdd = this.DOM.createElement("button");
        buttonAdd.setAttribute("id", trackId);
        ((EventTarget) buttonAdd).addEventListener("click", playTrack, false);

        Element playImg = this.DOM.createElement("img");
        playImg.setAttribute("src", "png/014-play-button-black.png");
        playImg.setAttribute("id", trackId);
        buttonAdd.appendChild(playImg);

        return buttonAdd;
    }


    // ===================
    // Event Listeners

    /**
     * Is called when the + button is click on a search result track
     */
    private EventListener addTrackToPlaylist = new EventListener() {
        public void handleEvent(Event ev) {

            Element button = (Element) ev.getTarget();

            System.out.println("track :" + button.getAttribute("id"));
            choosePlaylist(button.getAttribute("id"));
        }
    };

    /**
     * Is called when the play button is click on a search result track
     */
    private EventListener playTrack = new EventListener() {
        public void handleEvent(Event ev) {

            Element button = (Element) ev.getTarget();

            System.out.println("play track :" + button.getAttribute("id"));

        }
    };


    /**
     * Delete the track corresponding to the target Element
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

            //On reset la list des chanson
            displaySearchDiv();
        }
    };
}
