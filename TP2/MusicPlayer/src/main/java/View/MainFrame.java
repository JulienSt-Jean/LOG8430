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

        //Creation de resultats bidons pour les tests
        resultTrack.add(new Track(new Metadata("Do I wanna Know","Arctic Monkeys", "AM"), "1", "Spotify"));
        resultTrack.add(new Track(new Metadata("Cornerstone","Arctic Monkeys", "AM"), "2", "Spotify"));
        resultTrack.add(new Track(new Metadata("505","Arctic Monkeys", "AM"), "3", "Spotify"));
        resultTrack.add(new Track(new Metadata("Piledriver Waltz","Arctic Monkeys", "AM"), "4", "Spotify"));
        resultTrack.add(new Track(new Metadata("vhkbjnf","Afvrg", "cderf"), "5", "Spotify"));
        resultTrack.add(new Track(new Metadata("cdfv","Afvrfvgbg", "gvbrf"), "6", "Spotify"));
        resultTrack.add(new Track(new Metadata("erftg","zedfrrfvgbg", "brf"), "7", "Spotify"));

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

        Element button;
        if (playlistView)
            button = createDeleteButton(track.getId());
        else
            button = createAddButton(track.getId());

        Element divButton = this.DOM.createElement("entry");
        Element divTitle = this.DOM.createElement("entry");
        Element divArtiste = this.DOM.createElement("entry");
        Element divAlbum = this.DOM.createElement("entry");

        divTitle.setAttribute("class", "col-4");
        divArtiste.setAttribute("class", "col-4");
        divAlbum.setAttribute("class", "col-4");
        divButton.setAttribute("class", "col-4");

        divTitle.setTextContent(track.getMetadata().getName());
        divAlbum.setTextContent(track.getMetadata().getAlbum());
        divArtiste.setTextContent(track.getMetadata().getArtists());
        divButton.appendChild(button);

        t.appendChild(divButton);
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
        deleteImg.setAttribute("width","30%");
        deleteImg.setAttribute("height","30%");
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
