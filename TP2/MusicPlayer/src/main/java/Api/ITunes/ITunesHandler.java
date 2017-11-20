package Api.ITunes;

import Api.ApiWrapper;
import Api.Exceptions.WebApiException;
import Api.HTTPRequest;
import Model.Track;
import com.google.gson.JsonElement;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class ITunesHandler implements ApiWrapper {

    public static void main(String args[]) {
        ITunesHandler handler = new ITunesHandler();
        handler.searchTrack("skrillex");

    }


    public ArrayList<Track> searchTrack(String searchEntry) {
        try {
            HTTPRequest request = ITunesHTTPRequestBuilder.buildbuildSearchTrackRequest(searchEntry);
            request.makeConnection();
            System.out.print(request.getResponse());

            return null;
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
