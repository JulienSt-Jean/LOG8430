package Handler;


import Api.Jamendo.JamendoHandler;
import Api.ITunes.ITunesHandler;
import Api.Spotify.SpotifyHandler;
import Model.Track;
import java.util.ArrayList;


/**
 * Interface avec les différentes APIs de streaming
 */
public class ApiHandler {

    private JamendoHandler jamendoHandler;
    private ITunesHandler iTunesHandler;
    private SpotifyHandler spotifyHandler;

    /**
     * Constructeur
     */
    public ApiHandler() {
        this.jamendoHandler = new JamendoHandler();
        this.iTunesHandler = new ITunesHandler();
        this.spotifyHandler = new SpotifyHandler();
        System.out.println("ApiHandler created");
    }

    /**
     * Lance une requête sur les APIs pour effectuer une recherche
     * @param searchEntry mot clé de la recherche
     * @return liste de pistes trouvées
     */
    public ArrayList<Track> searchTrack(String searchEntry){
        ArrayList<Track> results = new ArrayList<Track>();
        results.addAll(this.spotifyHandler.searchTrack(searchEntry));
        results.addAll(this.jamendoHandler.searchTrack(searchEntry));
        results.addAll(this.iTunesHandler.searchTrack(searchEntry));

        return results;
    }
}
