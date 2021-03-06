package Services.JamendoService.Jamendo;

import Services.ServiceUtilities.HTTPRequest;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Philippe on 11/4/2017.
 */
public class JamendoHTTPRequestBuilder {
    private final String CLIENT_ID = "116c8efc";

    private final String API_URL = "https://api.jamendo.com/";
    private final String API_VERSION = "v3.0/";

    public HTTPRequest buildSearchTrackRequest(String query, int limit) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("tracks"));

        request.putURLParameter("client_id", CLIENT_ID);
        request.putURLParameter("format", "json");
        request.putURLParameter("namesearch", query.replace(" ", "+"));
        request.putURLParameter("limit", String.valueOf(limit));

        return request;
    }

    private URL buildAPIRequestURL(String endpoint) throws MalformedURLException {
        return new URL(API_URL + API_VERSION + endpoint);
    }
}
