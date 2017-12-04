package Api.Jamendo;

import Api.HTTPRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Created by Philippe on 11/4/2017.
 * Construit des requêtes HTTP pour l'API Jamendo
 */
public class JamendoHTTPRequestBuilder {
    private final String CLIENT_ID = "116c8efc";

    private final String API_URL = "https://api.jamendo.com/";
    private final String API_VERSION = "v3.0/";

    /**
     * Construit une requête de recherche de musique
     * @param query mot clé de la recherche
     * @param limit nombre maximal de résultat à retourner
     * @return requête HTTP
     * @throws MalformedURLException
     */
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
