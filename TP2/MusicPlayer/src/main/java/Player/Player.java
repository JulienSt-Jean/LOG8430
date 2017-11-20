package Player;

import Controller.Controller;
import Model.Track;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.IOException;
import java.net.URL;
import java.util.Stack;
import java.util.ArrayList;

public class Player {
    private Controller controller;

    private Stack<Track> previous;
    private Stack<Track> next;
    private Track currentTrack;

    private AdvancedPlayer streamPlayer;
    private InfoListener listener;

    boolean playNextTriggered;

    public Player(Controller c) {
        controller = c;
        this.currentTrack = null;
        this.previous = new Stack<Track>();
        this.next = new Stack<Track>();
        playNextTriggered = false;
    }

    //Plays a track
    public void play(Track track){
        if (this.streamPlayer != null)
            this.streamPlayer.stop();
        currentTrack = track;

        try {
            this.streamPlayer = new AdvancedPlayer(new URL(track.getAudioURL().toString()).openStream(), FactoryRegistry.systemRegistry().createAudioDevice());
            listener = new InfoListener();
            this.streamPlayer.setPlayBackListener(listener);
            (new Thread() {
                public void run() {
                try {
                    streamPlayer.play();
                } catch (Exception var2x) {
                    throw new RuntimeException(var2x.getMessage());
                }
                }
            }).start();
            playNextTriggered = false;
            controller.isPlayingNewSong();
        }catch (JavaLayerException | IOException e){
            e.printStackTrace();
            controller.isStopped();
        }
    }

    //Play or pause a track
    public void play_pause(){
        if (streamPlayer != null) {
            this.streamPlayer.stop();
            controller.isStopped();
        }
        else {
            this.play(currentTrack);
            controller.isPlaying();;
        }
    }

    public void addToQueue(ArrayList<Track> tracks){
        for(Track t : tracks){
            next.push(t);
        }
    }

    public void clearQueue(){
        previous.clear();
        next.clear();
    }

    public void playNext(){
        if(next.isEmpty()){
            System.out.println("No next song to play");
            controller.isStopped();
        }else{
            playNextTriggered = true;
            Track track = next.pop();
            previous.push(track);
            play(track);
            System.out.println("Play next song");
            controller.isPlayingNewSong();
        }
    }

    public void playPrevious(){
        if(previous.isEmpty()){
            System.out.println("No previous song to play");
        }
        else{
            Track track = previous.pop();
            currentTrack = track;
            next.push(track);
            play(track);
            System.out.println("Play previous song");
        }

    }

    public Track getCurrentTrack(){
        return currentTrack;
    }

    public class InfoListener extends PlaybackListener {
        public InfoListener() {
        }

        public void playbackStarted(PlaybackEvent ev) {
            System.out.println("Play started from frame " + ev.getFrame());
        }

        public void playbackFinished(PlaybackEvent ev) {
            System.out.println("Play completed at frame " + ev.getFrame());
            streamPlayer = null;
            listener = null;
            if (!playNextTriggered)
                playNext();
        }
    }
}
