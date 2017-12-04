package Player;

import Controller.Controller;
import Handler.ApiHandler;
import Model.Playlist;
import Model.Track;
import View.Browser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerTest {

    Player player = null;
    Track aItuneSong = null;
    Track aSpotifySong = null;
    Track aJamendoSong = null;
    Playlist aPlayList = null;

    //does nothing
    private class StubController extends Controller{
        public StubController(Browser browser){
            super(browser);
        }

        @Override
        public void isPlayingNewSong(){}

        @Override
        public void isStopped(){};

        @Override
        public void isPlaying(){};
    }

    @BeforeAll
    void SetUpOnce(){
        ApiHandler handler = new ApiHandler();
        ArrayList<Track> tracks = handler.searchTrack("skrillex");

        //we check if we have of each provider
        //if all song have a song url
        //if all song have all the meta data set
        boolean hasSeenItune = false;
        boolean hasSeenJamendo = false;
        boolean hasSeenSpotify = false;

        //a bit overkill
        for (Track track : tracks) {
            switch (track.getServiceProvider()) {
                case ITUNES:
                    hasSeenItune = true;
                    aItuneSong = track;
                    break;
                case SPOTIFY:
                    hasSeenSpotify = true;
                    aSpotifySong = track;
                    break;
                case JAMENDO:
                    hasSeenJamendo = true;
                    aJamendoSong = track;
                    break;
            }
        }
    }

    @BeforeEach
    void SetUpEachTest(){
        player = new Player(new StubController(null));
        ArrayList<Track> list = new ArrayList<>();
        list.add(aItuneSong);
        list.add(aSpotifySong);
        list.add(aJamendoSong);
        aPlayList = new Playlist("test",list);
    }


    @Test
    void play() throws InterruptedException {
        player.play(aItuneSong);
        TimeUnit.MILLISECONDS.sleep(25);
        player.play(aSpotifySong);
        TimeUnit.MILLISECONDS.sleep(25);
        player.play(aJamendoSong);
        TimeUnit.MILLISECONDS.sleep(100);
        player.stop();
    }

    @Test
    void play_pause() throws InterruptedException {
        player.play(aSpotifySong);
        player.play_pause();
        player.play_pause();
        player.play(aItuneSong);
        player.play_pause();
        player.play(aSpotifySong);
        player.play_pause();
        player.play_pause();
        player.play_pause();
        TimeUnit.MILLISECONDS.sleep(100);
        player.stop();
    }

    @Test
    void playNext() throws InterruptedException {
        player.playNext();
        player.play(aSpotifySong);
        player.playPlaylist(aPlayList);
        player.playNext();
        player.playNext();
        player.playNext();
        player.playNext();
        TimeUnit.MILLISECONDS.sleep(25);
        player.stop();
    }

    @Test
    void playNext1() {
        player.playNext(true);
        player.playNext(false);
        player.stop();
    }

    @Test
    void playPrevious() throws InterruptedException {
        player.playPrevious();
        player.playPlaylist(aPlayList);
        player.playNext();
        player.playNext();
        player.playPrevious();
        player.playPrevious();
        player.playPrevious();
        player.stop();
        TimeUnit.MILLISECONDS.sleep(25);
        player.stop();
    }

    @Test
    void playPlaylist() throws InterruptedException {
        player.playPlaylist(aPlayList);
        TimeUnit.SECONDS.sleep(70);
        player.stop();
    }

    @Test
    void getCurrentTrack() throws InterruptedException {
        assertNull(player.getCurrentTrack(), "Supposed to be null");
        player.play(aItuneSong);
        assertEquals(player.getCurrentTrack(), aItuneSong);
        TimeUnit.MILLISECONDS.sleep(25);
        player.stop();
    }

    @Test
    void stop() throws InterruptedException {
        player.stop();
        player.play(aItuneSong);
        player.stop();
        player.play(aSpotifySong);
        TimeUnit.MILLISECONDS.sleep(50);
        player.stop();
    }

}