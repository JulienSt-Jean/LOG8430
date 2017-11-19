package Player;

import Model.Track;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.sound.sampled.Line;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private Stack<Track> previous;
    private Stack<Track> next;
    private Track currentTrack;

    private AdvancedPlayer streamPlayer;
    private InfoListener listener;

    public Player() {
        this.currentTrack = null;
        this.previous = new Stack<Track>();
        this.next = new Stack<Track>();
    }

    //Plays a track
    public void play(Track track){
        currentTrack = track;

        if (streamPlayer != null)
            pause();
        try {
            //TODO : éviter Null ptr except...
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
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (JavaLayerException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Pause a track
    public void pause(){
        this.streamPlayer.stop();
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
        }else{
            Track track = next.pop();
            currentTrack = track;
            previous.push(track);
            play(track);
            System.out.println("Play next song");
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

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }


    public class InfoListener extends PlaybackListener {
        public InfoListener() {
        }

        public void playbackStarted(PlaybackEvent var1) {
            System.out.println("Play started from frame " + var1.getFrame());
        }

        public void playbackFinished(PlaybackEvent var1) {
            System.out.println("Play completed at frame " + var1.getFrame());
        }
    }
}
