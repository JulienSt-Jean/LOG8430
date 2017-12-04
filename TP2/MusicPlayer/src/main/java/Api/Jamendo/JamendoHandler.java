package Api.Jamendo;

import Api.ApiWrapper;
import Api.Exceptions.WebApiException;
import Api.HTTPRequest;
import Api.Spotify.SpotifyHTTPRequestBuilder;
import Api.Spotify.SpotifyHandler;
import Api.Spotify.SpotifyResponseParser;
import Model.Playlist;
import Model.Track;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.json.HTTP;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface avec l'API Jamendo
 */
public class JamendoHandler implements ApiWrapper {
    private JamendoHTTPRequestBuilder httpRequestBuilder = new JamendoHTTPRequestBuilder();
    private JamendoResponseParser parser = new JamendoResponseParser();

    public List<Track> searchTrack(String searchEntry) {
        try {
            HTTPRequest request = httpRequestBuilder.buildSearchTrackRequest(searchEntry, 50);
            request.makeConnection();

            JsonElement response = new JsonParser().parse(request.getResponse());

            return parser.parseTrackList(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
