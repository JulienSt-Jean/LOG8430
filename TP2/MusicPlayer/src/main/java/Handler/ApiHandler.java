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
        System.out.println("ApiHandler created");
    }

    public ArrayList<Track> searchTrack(String searchEntry){
        ArrayList<Track> results = new ArrayList<Track>();

        /*JSONArray beatportResults = jamendoHandler.searchTrack(searchEntry);
        JSONArray deezerResults = deezerHandler.searchTrack(searchEntry);
        JSONArray spotifyResults = spotifyHandler.searchTrack(searchEntry);*/

        /*
        add result from each library to final and unified result
         */

        return results;
    }


    public void readTrack(Track track){
    }


}
