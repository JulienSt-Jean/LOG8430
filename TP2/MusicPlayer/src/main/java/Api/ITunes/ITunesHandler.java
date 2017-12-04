package Api.ITunes;

import Api.ApiWrapper;
import Api.Exceptions.WebApiException;
import Api.HTTPRequest;
import Controller.Controller;
import Model.Metadata;
import Model.Track;
import View.Browser;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import Player.Player;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Interface avec l'API ITunes
 */
public class ITunesHandler implements ApiWrapper {

    private ITunesResponseParser parser = new ITunesResponseParser();

    public ArrayList<Track> searchTrack(String searchEntry) {
        try {
            HTTPRequest request = ITunesHTTPRequestBuilder.buildbuildSearchTrackRequest(searchEntry);
            request.makeConnection();
            JsonElement response = new JsonParser().parse(request.getResponse());

            return parser.parseTrackList(response);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }




}
