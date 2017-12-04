package View;

import Controller.Controller;
import Model.Playlist;
import Model.Track;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

/**
 * Classe de gestion du Main frame (affichage des tracks et de la barre de recherche
 */
public class MainFrame extends Frame {

    private ArrayList<Track> resultTrack;

    // Differentes régions du frame
    private Element playlistDiv;
    private Element searchDiv;
    private Element tracks;

    /**
     * Constructeur
     * @param doc DOM du frame HTML
     * @param c référence sur le Contrôleur de l'application
     */
    public MainFrame(Document doc, Controller c){
        super(doc, c);

        resultTrack = new ArrayList<>();
        playlistDiv = this.DOM.getElementById("playlistDiv");
        searchDiv = this.DOM.getElementById("searchDiv");
        tracks = this.DOM.getElementById("tracks");
        initiateTable();
        displaySearchField();

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
        displayPlaylistField();

        Element playlistName = this.DOM.getElementById("playlistName");
        playlistName.setTextContent(playlist.getName());

        if(playlist.getListTrack().size() != 0) {

            displayTracks(playlist.getListTrack(), true);
        }
    }

    /**
     * Display the search Division and hide the division of playlist
     */
    public void displaySearchField(){
        searchDiv.removeAttribute("hidden");
        playlistDiv.setAttribute("hidden", "");
        initiateTable();
    }

    public void displayPlaylistField(){
        searchDiv.setAttribute("hidden", "");
        playlistDiv.removeAttribute("hidden");
        initiateTable();
    }

    /**
     * Get the result of the input in the search bar and send the search query to the api
     * @param data
     */
    public void sendInputData(String data){
        controller.searchTrack(data);
    }

    /**
     * Affiche les résultats d'une recherche
     * @param trackToAdd liste de pistes
     */
    public void displaySearchResults(ArrayList<Track> trackToAdd){
        resultTrack.clear();
        resultTrack.addAll(trackToAdd);
        displayTracks(trackToAdd, false);
    }

    /**
     * Affiche une liste de pistes
     * @param trackToAdd liste de pistes à afficher
     * @param playlistView true si l'on affiche le contenu d'une playlist, false si résultat de recherche
     */
    private void displayTracks(ArrayList<Track> trackToAdd, boolean playlistView){
        initiateTable();
        for(Track track : trackToAdd)
            tracks.appendChild(createTrackHTML(track, playlistView));
    }

    /**
     * Clear de la zone d'affichage des pistes
     */
    private void initiateTable(){
        tracks.setTextContent("");
    }

    /**
     * Créé un élément HTML correspondant à l'affichage d'une piste
     * @param track piste à afficher
     * @param playlistView true si l'on affiche le contenu d'une playlist
     * @return Element HTMTL correspondant à une piste
     */
    private Element createTrackHTML(Track track, boolean playlistView){
        Element t = this.DOM.createElement("tr");
        Element addButton;
        Element playButton = createPlayButton(track.getId());
        if (playlistView)
            addButton = createDeleteButton(track.getId());
        else
            addButton = createAddButton(track.getId());

        Element divPlayButton = this.DOM.createElement("td");
        Element divAddButton = this.DOM.createElement("td");
        Element divTitle = this.DOM.createElement("td");
        Element divArtiste = this.DOM.createElement("td");
        Element divAlbum = this.DOM.createElement("td");
        Element provider = this.DOM.createElement("td");
        Element providerImg = this.DOM.createElement("img");

        switch(track.getServiceProvider()){
            case SPOTIFY:
                providerImg.setAttribute("src", "png/spotify.png");
                break;
            case JAMENDO:
                providerImg.setAttribute("src", "png/jamendo.png");
                break;
            case ITUNES:
                providerImg.setAttribute("src", "png/itunes.png");
                break;
        }

        providerImg.setAttribute("style", "width: 20px;");
        provider.appendChild(providerImg);

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
        t.appendChild(provider);

        return t;
    }

    /**
     * Display a popup in which we choose to which playlist add the song id.
     * @return the name of the chosen playlist
     */
    public Optional<String> choosePlaylist(ArrayList<String> playlistNames){
        ChoiceDialog<String> dialog = new ChoiceDialog<>(playlistNames.get(0), playlistNames);
        dialog.setTitle("Ajouter à une playlist");
        dialog.setHeaderText("");
        dialog.setContentText("Choisissez une playlist: ");

        return dialog.showAndWait();
    }

    /**
     * Affiche un message d'erreur lors si aucune playlist n'a été créée
     */
    public void showAddTrackWarning(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Avertissement");
        alert.setHeaderText("Il n'existe pas encore de playlist");
        alert.showAndWait();
    }

    /**
     * Trouver une piste à partir d'un id
     * @param id
     * @return piste correspondant à l'id
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
    private Element createDeleteButton(String trackId){
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
    private Element createAddButton(String trackId){
        Element buttonAdd = this.DOM.createElement("button");
        buttonAdd.setAttribute("id", trackId);
        ((EventTarget) buttonAdd).addEventListener("click", addTrack, false);

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
    private Element createPlayButton(String trackId){
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
    private EventListener addTrack = new EventListener() {
        public void handleEvent(Event ev) {
            Element button = (Element) ev.getTarget();
            Track track = getTrackById(button.getAttribute("id"));
            controller.addTrack(track);
        }
    };

    /**
     * Is called when the play button is click on a search result track
     */
    private EventListener playTrack = new EventListener() {
        public void handleEvent(Event ev) {

            Element button = (Element) ev.getTarget();

            String id = button.getAttribute("id");
            System.out.println("track :" + id +" to be played");
            for(Track track: resultTrack){
                if(track.getId().equals(id))
                    controller.playTrack(track);
            }
        }
    };


    /**
     * Delete the track corresponding to the target Element
     */
    private EventListener deleteTrackFromPlaylist = new EventListener() {
        public void handleEvent(Event ev) {
            String id = ((Element)ev.getTarget()).getAttribute("id");
            controller.removeTrackFromCurrentPlaylist(id);
        }
    };

    /**
     * Play the current playlist
     * Is called when the play button is pressed  is the playlist div
     */
    private EventListener playPlaylist = new EventListener() {
        public void handleEvent(Event ev) {
            controller.playCurrentPlaylist();
        }
    };

    /**
     * Delete the current playlist
     * Is called when the delete button is pressed  is the playlist div
     */
    private EventListener removePlaylist = new EventListener() {
        public void handleEvent(Event ev) {
            controller.removeCurrentPlaylist();
        }
    };
}
