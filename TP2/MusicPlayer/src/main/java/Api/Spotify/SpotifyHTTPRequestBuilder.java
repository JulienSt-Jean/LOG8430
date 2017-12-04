package Api.Spotify;

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
 * Construit des requêtes HTTP pour l'API Spotify
 */
public class SpotifyHTTPRequestBuilder{
    private final String CLIENT_ID = "09485d17989d4058a4c112f80196c12c";
    private final String CLIENT_SECRET = "f0ac2909f1314bc685e0a1259764d501";
    private final String REDIRECT_URI = "https://github.com/PhilippeMarcotte";

    private String auth_code = "AQCCUDzcMrB5_0XTk8eJ3TydPgHCdoy_o3LLnYL24dxWHeFhYDF9jJCK9f86ksImirc4uY2xt5jbyAug44oyOPQ1y6_ytcXUydgKq-x-u--A3XRoub8WsYj8ehAjFWUKCCUL1-us4t9JyCEt6nVa4ILBXqdNsqfN0qhi2GWGKHpbBe7K7NWUN2yiB10JanuBCHgWJ2YyoF9bzHrwyKjvXfen8FwPgVnBuU9Myo8pnXXrut2HCiNS_djAJS4dKhGOzcTzC9HF2gSSTA";
    private String access_token;
    private String refreshToken;

    private final String ACCOUNT_URL = "https://accounts.spotify.com/";
    private final String ACCOUNT_AUTH_ENDPOINT = "authorize";
    private final String ACCOUNT_TOKEN_ENDPOINT = "api/token";

    private final String API_URL = "https://api.spotify.com/";
    private final String API_VERSION = "v1/";

    private String userId;

    /**
     * Constructeur
     */
    public SpotifyHTTPRequestBuilder(){
        loadToken();
    }

    /**
     * Construit une requête pour l'obtention d'un token de connexion
     * @return requête HTTP
     */
    public HTTPRequest buildRefreshAccessTokenRequest(){
        HTTPRequest request = null;
        try {
            request = new HTTPRequest(new URL(ACCOUNT_URL + ACCOUNT_TOKEN_ENDPOINT));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        request.putURLParameter("grant_type", "refresh_token");
        request.putURLParameter("refresh_token", refreshToken);
        request.putURLParameter("redirect_uri", REDIRECT_URI);

        request.setRequestMethod("POST");

        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        request.putRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes())));
        request.putContentType("application/x-www-form-urlencoded");

        return request;
    }

    /**
     * Récupère un token de connexion à l'API
     */
    public void loadToken(){
        String token = null;
        try {
            token = new String(Files.readAllBytes(Paths.get("./tokens")));
            JsonObject jsonResponse = new JsonParser().parse(token).getAsJsonObject();
            access_token = jsonResponse.get("access_token").getAsString();
            refreshToken = jsonResponse.get("refresh_token").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public HTTPRequest buildUserProfileRequest() throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("me"));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        return request;
    }

    /**
     * Construit une requête de recherche de musique
     * @param query mot clé de la recherche
     * @param limit nombre maximal de résultat à retourner
     * @return requête HTTP
     * @throws MalformedURLException
     */
    public HTTPRequest buildSearchTrackRequest(String query, int limit) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("search"));

        request.putURLParameter("q", query.replace(" ", "+"));
        request.putURLParameter("type", "track");
        request.putURLParameter("limit", String.valueOf(limit));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        return request;
    }

    private URL buildAPIRequestURL(String endpoint) throws MalformedURLException {
        return new URL(API_URL + API_VERSION + endpoint);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setUserId(JsonElement json) {
        this.userId = json.getAsJsonObject().get("id").getAsString();
    }
}
