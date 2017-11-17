package View;

import Controller.Controller;
import Model.Track;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.util.Optional;

public class PlayerFrame extends Frame {
    private Boolean play;
    private Track currentTrack;

    public PlayerFrame(Document doc, Controller c){
        super(doc, c);
        play = false;
        currentTrack = null;
        Element play_pause_img = this.DOM.getElementById("play_pause_img");
        play_pause_img.setAttribute("src", "png/014-play-button.png");


        Element play_pauseButton = this.DOM.getElementById("play_pause");
        ((EventTarget) play_pauseButton).addEventListener("click", play_pauseTrack, false);

        Element backButton = this.DOM.getElementById("back");
        ((EventTarget) backButton).addEventListener("click", previousTrack, false);

        Element nextButton = this.DOM.getElementById("next");
        ((EventTarget) nextButton).addEventListener("click", nextTrack, false);
    }

    public void play(){
        play = true;
        Element play_pause_img = DOM.getElementById("play_pause_img");
        play_pause_img.setAttribute("src", "png/016-pause.png");
    }

    public void displayCurrentTrack(){
        Element title = this.DOM.getElementById("title");
        title.setTextContent(currentTrack.getMetadata().getName());
        Element album = this.DOM.getElementById("album");
        album.setTextContent(currentTrack.getMetadata().getAlbum());
        Element artist = this.DOM.getElementById("artist");
        artist.setTextContent(currentTrack.getMetadata().getArtists());
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    // ===================
    // Event Listeners
    EventListener play_pauseTrack = new EventListener() {
        public void handleEvent(Event ev) {
            Element play_pause_img = DOM.getElementById("play_pause_img");
            play = !play;
            if(play){
                play_pause_img.setAttribute("src","png/016-pause.png");
                controller.playMusic();
            }else{
                play_pause_img.setAttribute("src", "png/014-play-button.png");
                controller.pauseMusic();
            }


        }
    };

    EventListener previousTrack = new EventListener() {
        public void handleEvent(Event ev) {
            controller.playPrevious();
        }
    };
    EventListener nextTrack = new EventListener() {
        public void handleEvent(Event ev) {
            controller.playNext();
        }
    };



}
