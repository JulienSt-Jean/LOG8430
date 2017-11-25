package Handler;

import Api.*;

import Api.Jamendo.JamendoHandler;
import Api.Spotify.SpotifyHandler;
import Model.Track;
import java.util.ArrayList;



public class ApiHandler {

    private JamendoHandler jamendoHandler;
    private DeezerHandler deezerHandler;
    private SpotifyHandler spotifyHandler;

    public ApiHandler() {
        this.jamendoHandler = new JamendoHandler();
        this.deezerHandler = new DeezerHandler();
        this.spotifyHandler = new SpotifyHandler();
    }

    public ArrayList<Track> searchTrack(String searchEntry){
        ArrayList<Track> results = new ArrayList<Track>();
        results.addAll(this.spotifyHandler.searchTrack(searchEntry));
        results.addAll(this.jamendoHandler.searchTrack(searchEntry));

        return results;
    }
}
