package Player;

import Controller.Controller;
import Model.Playlist;
import Model.ServiceProvider;
import Model.Track;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import net.sourceforge.jaad.aac.AACException;
import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import net.sourceforge.jaad.mp4.MP4Container;
import net.sourceforge.jaad.mp4.api.AudioTrack;
import net.sourceforge.jaad.mp4.api.Frame;
import net.sourceforge.jaad.mp4.api.Movie;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;


public class Player {
    private Controller controller;

    private Playlist currentPlaylist;
    private Track currentTrack;

    private AdvancedPlayer streamPlayer;
    private AacInMp4Player aacPlayer;

    private InfoListener listener;

    boolean previous_nextTriggered;

    public Player(Controller c) {
        controller = c;
        this.currentPlaylist = null;
        previous_nextTriggered = false;
        aacPlayer = null;
    }

    //Plays a track
    public void play(Track track){

        if (aacPlayer != null){
            this.aacPlayer.stop();
        }

        if (this.streamPlayer != null)
            this.streamPlayer.stop();

        currentTrack = track;

        try {
            if(track.getServiceProvider() == ServiceProvider.ITUNES) {


                aacPlayer = new AacInMp4Player(new InfoListener());
                (new Thread() {
                    public void run() {
                        try {
                            aacPlayer.CreateAudioBufferAndPlaySongFromAacInMp4(track.getAudioURL().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }).start();


            }
            else {
                this.streamPlayer = new AdvancedPlayer(new URL(track.getAudioURL().toString()).openStream(), FactoryRegistry.systemRegistry().createAudioDevice());

                listener = new InfoListener();
                this.streamPlayer.setPlayBackListener(listener);
                (new Thread() {
                    public void run() {
                        try {
                            streamPlayer.play();
                        } catch (Exception var2x) {
                            var2x.printStackTrace();
                            throw new RuntimeException(var2x.getMessage());
                        }
                    }
                }).start();
            }


            previous_nextTriggered = false;
            controller.isPlayingNewSong();
        }
        catch (JavaLayerException | IOException e){
            controller.isStopped();
        }
    }

    //Play or pause a track
    public void play_pause(){
        previous_nextTriggered = true;
        if ((aacPlayer != null && !aacPlayer.hasBeenStop)|| streamPlayer != null ) {

            if (aacPlayer != null){
                aacPlayer.stop();
            }

            if (streamPlayer != null) {
                this.streamPlayer.stop();
            }

            controller.isStopped();
        }
        else {
            this.play(currentTrack);
            controller.isPlaying();
        }
        previous_nextTriggered = false;
    }

    public void playNext(){
        playNext(false);
    }

    public void playNext(boolean isLastTrackDonePlaying){
        if(currentPlaylist == null){
            System.out.println("No playlist to play");
            if (isLastTrackDonePlaying){
                controller.isStopped();
            }

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
                if(streamPlayer != null) {
                    streamPlayer.stop();
                }
                if(aacPlayer != null){
                    aacPlayer.stop();
                }
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
                if(streamPlayer != null )
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
        currentTrack = null;
        currentPlaylist = playlist;
        playNext();
    }

    public Track getCurrentTrack(){
        return currentTrack;
    }


    public void stop()  {
        currentPlaylist = null;
        if (aacPlayer != null) {
            aacPlayer.stop();
        }
        if (streamPlayer != null) {
            streamPlayer.stop();
        }
    }

    public class InfoListener extends PlaybackListener {
        public InfoListener() {
        }

        public void playbackStarted(PlaybackEvent ev) {
            System.out.println("Play started from frame " + ev.getFrame());
        }

        public void playbackStarted() {
            System.out.println("Play started from a aac song");
        }

        public void playbackFinished(PlaybackEvent ev) {
            System.out.println("Play completed at frame " + ev.getFrame());
            streamPlayer = null;
            listener = null;
            if (!previous_nextTriggered)
                playNext(true);


        }

        public void playbackFinished() {
            System.out.println("Play completed");
            if (!previous_nextTriggered)
                playNext(true);

        }
    }

    private class AacInMp4Player {

        private SourceDataLine line = null;
        public  InfoListener callBack = null;
        private boolean hasBeenStop = false;

        public AacInMp4Player(InfoListener callBacks){
            callBack = callBacks;
        }

        public void stop(){
            if (line != null){
                line.close();
                hasBeenStop = true;
            }

        }

        public void CreateAudioBufferAndPlaySongFromAacInMp4(String url){

            byte[] b;


            try {
                //create container

                final MP4Container cont = new MP4Container(new URL(url).openStream());
                final Movie movie = cont.getMovie();
                //find AAC track
                final List<net.sourceforge.jaad.mp4.api.Track> tracks = movie.getTracks(AudioTrack.AudioCodec.AAC);
                if (tracks.isEmpty()) throw new Exception("movie does not contain any AAC track");
                final AudioTrack track = (AudioTrack) tracks.get(0);

                //create audio format
                final AudioFormat aufmt = new AudioFormat(track.getSampleRate(), track.getSampleSize(), track.getChannelCount(), true, true);
                if(line != null){
                    line.stop();
                }
                line = AudioSystem.getSourceDataLine(aufmt);

                line.open();
                line.start();

                //create AAC decoder
                final Decoder dec = new Decoder(track.getDecoderSpecificInfo());
                //decode
                Frame frame;
                final SampleBuffer buf = new SampleBuffer();

                callBack.playbackStarted();


                while (track.hasMoreFrames()) {
                    frame = track.readNextFrame();
                    try {
                        dec.decodeFrame(frame.getData(), buf);
                        b = buf.getData();
                        line.write(b, 0, b.length);
                    } catch (AACException e) {
                        e.printStackTrace();
                        //since the frames are separate, decoding can continue if one fails
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                ;
            } finally {
                if (line != null) {
                    line.stop();
                    line.close();
                    if(!hasBeenStop) {
                        callBack.playbackFinished();
                    }
                }
            }
        }
    }

}
