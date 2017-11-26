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

public class ITunesHandler implements ApiWrapper {

    private ITunesResponseParser parser = new ITunesResponseParser();

    public static void main(String args[]) throws IOException {
        ITunesHandler handler = new ITunesHandler();
        List<Track> list = handler.searchTrack("skrillex");

        Track song1 = list.get(0);
        Metadata meta = song1.getMetadata();
        System.out.println(meta.getName());
        System.out.println(meta.getArtists());
        System.out.println(meta.getAlbum());
        System.out.println(song1.getAudioURL());


    }



    public ArrayList<Track> searchTrack(String searchEntry) {
        try {
            HTTPRequest request = ITunesHTTPRequestBuilder.buildbuildSearchTrackRequest(searchEntry);
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