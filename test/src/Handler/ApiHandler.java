package Handler;

import Api.BeatportHandler;
import Api.DeezerHandler;
import Api.SpotifyHandler;
import Model.Track;
import org.json.JSONArray;

import java.util.ArrayList;



public class ApiHandler {

    private BeatportHandler beatportHandler;
    private DeezerHandler deezerHandler;
    private SpotifyHandler spotifyHandler;


    public ApiHandler() {
        this.beatportHandler = new BeatportHandler();
        this.deezerHandler = new DeezerHandler();
        this.spotifyHandler = new SpotifyHandler();
        System.out.println("ApiHandler created");
    }

    public ArrayList<Track> searchTrack(String searchEntry){
        ArrayList<Track> results = new ArrayList<>();

        JSONArray beatportResults = beatportHandler.searchTrack(searchEntry);
        JSONArray deezerResults = deezerHandler.searchTrack(searchEntry);
        JSONArray spotifyResults = spotifyHandler.searchTrack(searchEntry);

        /*
        add result from each library to final and unified result
         */

        return results;
    }


    public void readTrack(Track track){
        if(track.getServiceProvider().equals("Spotify")){
            spotifyHandler.readTrack(track.getId());
        }
        else if(track.getServiceProvider().equals("Deezer")){
            deezerHandler.readTrack(track.getId());
        }
        else if(track.getServiceProvider().equals("BeatPort")){
            beatportHandler.readTrack(track.getId());
        }
    }


}
