package View;

import Controller.Controller;
import Model.Track;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.util.Optional;

/**
 * Vue des contrôles liés au Player (play/pause, next...)
 */
public class PlayerFrame extends Frame {

    /**
     * Constructeur
     * @param doc DOM du frame HTML
     * @param c référence sur le Contrôleur de l'application
     */
    public PlayerFrame(Document doc, Controller c){
        super(doc, c);

        Element play_pause_img = this.DOM.getElementById("play_pause_img");
        play_pause_img.setAttribute("src", "png/014-play-button.png");

        Element play_pauseButton = this.DOM.getElementById("play_pause");
        ((EventTarget) play_pauseButton).addEventListener("click", play_pauseClicked, false);

        Element backButton = this.DOM.getElementById("back");
        ((EventTarget) backButton).addEventListener("click", previousTrack, false);

        Element nextButton = this.DOM.getElementById("next");
        ((EventTarget) nextButton).addEventListener("click", nextTrack, false);
    }

    /**
     * Affiche un bouton "Play"
     */
    public void displayPlay(){
        Element play_pause_img = DOM.getElementById("play_pause_img");
        play_pause_img.setAttribute("src", "png/014-play-button.png");
    }

    /**
     * Affiche un bouton "Pause"
     */
    public void displayPause(){
        Element play_pause_img = DOM.getElementById("play_pause_img");
        play_pause_img.setAttribute("src", "png/016-pause.png");
    }

    /**
     * Affiche les métadonnées de la piste jouée
     * @param track piste en train d'être jouée
     */
    public void displayTrackInfo(Track track){
        Element title = this.DOM.getElementById("title");
        title.setTextContent(track.getMetadata().getName());
        Element album = this.DOM.getElementById("album");
        album.setTextContent(track.getMetadata().getAlbum());
        Element artist = this.DOM.getElementById("artist");
        artist.setTextContent(track.getMetadata().getArtists());
    }

    /**
     * Efface les métadonnées de piste affichées
     */
    public void hideTrackInfo(){
        Element title = this.DOM.getElementById("title");
        title.setTextContent("");
        Element album = this.DOM.getElementById("album");
        album.setTextContent("");
        Element artist = this.DOM.getElementById("artist");
        artist.setTextContent("");
    }



    // ===================
    // Event Listeners

    /**
     * Action lors d'un clic sur le bouton "Play/pause"
     */
    EventListener play_pauseClicked = new EventListener() {
        public void handleEvent(Event ev) {
            controller.play_pauseClicked();
        }
    };

    /**
     * Action lors d'un clic sur "Previous"
     */
    EventListener previousTrack = new EventListener() {
        public void handleEvent(Event ev) {
            controller.playPrevious();
        }
    };

    /**
     * Action lors d'un clic sur "Next"
     */
    EventListener nextTrack = new EventListener() {
        public void handleEvent(Event ev) {
            controller.playNext();
        }
    };
}
