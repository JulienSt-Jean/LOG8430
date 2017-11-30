package Api.ITunes;

import Api.HTTPRequest;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Construit des requêtes HTTP pour l'API ITunes
 */
public interface ITunesHTTPRequestBuilder {

    /**
     * Construit une requête de recherche de musique
     * @param searchEntry mot clé de la recherche
     * @return requête HTTP
     * @throws MalformedURLException
     */
    public static HTTPRequest buildbuildSearchTrackRequest(String searchEntry) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest( buildAPIRequestURL());
        request.putURLParameter("term", searchEntry);
        request.putURLParameter("media","music");
        request.putURLParameter("entity", "musicTrack");
        return request;
    }

    public static URL buildAPIRequestURL() throws MalformedURLException {
        return new URL("https://itunes.apple.com/search");
    }

}
