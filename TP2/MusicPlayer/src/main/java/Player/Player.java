package Player;

import Controller.Controller;
import Model.Playlist;
import Model.Track;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Stack;
import java.util.ArrayList;

public class Player {
    private Controller controller;

    private Playlist currentPlaylist;
    private Track currentTrack;

    private AdvancedPlayer streamPlayer;
    private InfoListener listener;

    boolean previous_nextTriggered;

    public Player(Controller c) {
        controller = c;
        this.currentPlaylist = null;
        previous_nextTriggered = false;
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
            previous_nextTriggered = false;
            controller.isPlayingNewSong();
        }catch (JavaLayerException | IOException e){
            e.printStackTrace();
            controller.isStopped();
        }
    }

    //Play or pause a track
    public void play_pause(){
        previous_nextTriggered = true;
        if (streamPlayer != null) {
            this.streamPlayer.stop();
            controller.isStopped();
        }
        else {
            this.play(currentTrack);
            controller.isPlaying();
        }
        previous_nextTriggered = false;
    }

    public void playNext(){
        if(currentPlaylist == null){
            System.out.println("No playlist to play");
        }
        else if(currentTrack == null){
            currentTrack = currentPlaylist.getListTrack().get(0);
            previous_nextTriggered = true;
            play(currentTrack);
            System.out.println("Start playlist with :" +currentTrack.getMetadata().toString());
            controller.isPlayingNewSong();
        }
        else{
            int index = currentPlaylist.getListTrack().indexOf(currentTrack);
            ListIterator<Track> iterator = currentPlaylist.getListTrack().listIterator(index);
            previous_nextTriggered = true;
            iterator.next();
            if(iterator.hasNext()){

                currentTrack = iterator.next();
                play(currentTrack);
                System.out.println("next track : "+currentTrack.getMetadata().toString());
                controller.isPlayingNewSong();
            }
            else {
                streamPlayer.stop();
                controller.isStopped();
                currentTrack = null;
                currentPlaylist = null;
                System.out.println("No next track to play");
            }
        }
        previous_nextTriggered = false;
    }

    public void playPrevious(){
        if(currentPlaylist == null){
            System.out.println("No playlist to play");
        }
        else{
            int index = currentPlaylist.getListTrack().indexOf(currentTrack);
            ListIterator<Track> iterator = currentPlaylist.getListTrack().listIterator(index);
            previous_nextTriggered = true;
            if(iterator.hasPrevious()){
                currentTrack = iterator.previous();
                play(currentTrack);
                System.out.println("previous track : " +currentTrack.getMetadata().toString());
                controller.isPlayingNewSong();
            }
            else{
                streamPlayer.stop();
                controller.isStopped();
                currentTrack = null;
                currentPlaylist = null;
                System.out.println("No previous track to play");
            }
            previous_nextTriggered = false;
        }

    }

    public void playPlaylist(Playlist playlist){
        System.out.println("Play playlist :"+playlist.getName());
        currentPlaylist = null;
        currentTrack = null;
        currentPlaylist = playlist;
        playNext();
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
            if (!previous_nextTriggered)
                playNext();
        }
    }
}
