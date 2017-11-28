package Services.SpotifyService.Spotify;

import Services.ServiceUtilities.HTTPRequest;
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

    public SpotifyHTTPRequestBuilder(){
        loadToken();
    }

    public HTTPRequest buildAccessTokenRequest(String [] scopes){
        HTTPRequest request = null;
        try {
            request = new HTTPRequest(new URL(ACCOUNT_URL + ACCOUNT_TOKEN_ENDPOINT));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        request.putURLParameter("grant_type", "authorization_code");
        request.putURLParameter("code", auth_code);
        request.putURLParameter("redirect_uri", REDIRECT_URI);

        request.setRequestMethod("POST");

        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        request.putRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes())));
        request.putContentType("application/x-www-form-urlencoded");

        return request;
    }

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

    public String generateAuthUrl(String [] scopes) {
        HTTPRequest request = null;
        try {
            request = new HTTPRequest(new URL(ACCOUNT_URL + ACCOUNT_AUTH_ENDPOINT));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        request.putURLParameter("client_id", CLIENT_ID);
        request.putURLParameter("response_type", "code");
        request.putURLParameter("redirect_uri", REDIRECT_URI);
        request.putURLParameter("show_dialog", "true");
        request.putURLParameter("scope", String.join(" ", scopes));

        try {
            return request.buildUrl();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

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

    public HTTPRequest buildSearchTrackRequest(String query, int limit) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("search"));

        request.putURLParameter("q", query.replace(" ", "+"));
        request.putURLParameter("type", "track");
        request.putURLParameter("limit", String.valueOf(limit));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        return request;
    }

    public HTTPRequest buildGetPlaylistsRequest() throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("me/playlists"));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        return request;
    }

    public HTTPRequest buildGetPlaylistRequest(String playlistId) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("users/" + userId + "/playlists/" + playlistId));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        return request;
    }

    public HTTPRequest buildAddTrackToPlaylistRequest(URI trackURI, String playlistId) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("users/" + userId + "/playlists/" + playlistId + "/tracks"));

        request.setRequestMethod("POST");

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        request.putURLParameter("uris", trackURI.toString());

        return request;
    }

    public HTTPRequest buildRemoveTrackFromPlaylistRequest(URI trackURI, String playlistId) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("users/" + userId + "/playlists/" + playlistId + "/tracks"));

        request.setRequestMethod("DELETE");

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        JsonObject trackObject = new JsonObject();
        trackObject.addProperty("uri", trackURI.toString());

        JsonArray tracksArray = new JsonArray();
        tracksArray.add(trackObject);

        JsonObject tracksObject = new JsonObject();
        tracksObject.add("tracks", tracksArray);

        request.setBody(tracksObject.toString());

        return request;
    }

    public HTTPRequest buildCreatePlaylistRequest(String name) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("users/" + userId  + "/playlists"));

        request.setRequestMethod("POST");

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        JsonObject object = new JsonObject();
        object.addProperty("name", name);

        request.setBody(object.toString());

        return request;
    }

    public HTTPRequest buildUnfollowPlaylistRequest(String playlistId) throws MalformedURLException {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("users/" + userId  + "/playlists/" + playlistId + "/followers"));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        request.setRequestMethod("DELETE");

        return request;
    }

    public HTTPRequest buildRequest(URL url){
        HTTPRequest request = new HTTPRequest(url);

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
