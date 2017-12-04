package Handler;

import Model.Metadata;
import Model.Track;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ApiHandlerTest {
    @Test
    void searchTrack() {
        ApiHandler handler = new ApiHandler();
        ArrayList<Track> tracks = handler.searchTrack("skrillex");

        //we check if we have of each provider
        //if all song have a song url
        //if all song have all the meta data set
        boolean hasSeenItune = false;
        boolean hasSeenJamendo = false;
        boolean hasSeenSpotify = false;

        for (Track track : tracks) {
            switch (track.getServiceProvider()){
                case ITUNES:
                    hasSeenItune = true;
                    break;
                case SPOTIFY:
                    hasSeenSpotify = true;
                    break;
                case JAMENDO:
                    hasSeenJamendo = true;
                    break;
            }

            assertNotNull(track.getAudioURL(), "all track must have a audio url");
            Metadata meta = track.getMetadata();
            assertNotNull(meta, "All track must have a meta data object");
            assertNotNull(meta.getAlbum(), "all track must have a album name");
            assertNotNull(meta.getArtists(), "all track must have a artist name");
            assertNotNull(meta.getName(), "all track must have a name");
        }

        assertTrue(hasSeenItune, "must have sean itune songs");
        assertTrue(hasSeenJamendo, "must have sean a jamendo song at least");
        assertTrue(hasSeenSpotify, "must have sean spotify song at least");
    }

}