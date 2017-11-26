package Api.ITunes;

import Api.HTTPRequest;

import java.net.MalformedURLException;
import java.net.URL;

public class ITunesHTTPRequestBuilder {

    public static HTTPRequest buildbuildSearchTrackRequest(String searchEntry){
        HTTPRequest request = new HTTPRequest( buildAPIRequestURL());
        request.putURLParameter("term", searchEntry);
        request.putURLParameter("media","music");
        request.putURLParameter("entity", "musicTrack");
        return request;
    }

    private static URL buildAPIRequestURL(){
        try{
            return new URL("https://itunes.apple.com/search");
        }
        catch (Exception e){
            return  null;
        }
    }

}