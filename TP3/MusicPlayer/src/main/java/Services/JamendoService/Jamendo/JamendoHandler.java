package Services.JamendoService.Jamendo;

import Services.ServiceUtilities.ApiWrapper;
import Services.Exceptions.WebApiException;
import Services.ServiceUtilities.HTTPRequest;
import Shared.Model.Track;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class JamendoHandler implements ApiWrapper {
    private JamendoHTTPRequestBuilder httpRequestBuilder = new JamendoHTTPRequestBuilder();
    private JamendoResponseParser parser = new JamendoResponseParser();

    public static void main(String args[]) {
        JamendoHandler handler = new JamendoHandler();
        List<Track> list = handler.searchTrack("black");
        AdvancedPlayer player = null;
        try {
            HTTPRequest request = new HTTPRequest(list.get(0).getAudioURL());
            request.makeConnection();
            player = new AdvancedPlayer(request.getInputStream());
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Track> searchTrack(String searchEntry) {
        try {
            HTTPRequest request = httpRequestBuilder.buildSearchTrackRequest(searchEntry, 50);
            request.makeConnection();

            JsonElement response = new JsonParser().parse(request.getResponse());

            return parser.parseTrackList(response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
