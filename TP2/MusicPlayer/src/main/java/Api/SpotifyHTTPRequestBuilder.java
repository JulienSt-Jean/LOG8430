package Api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    private String auth_code = "AQCGmf5SlfKJzxHo3Vttly2LbGcQtIGQzkEGdS8_C7P_KBA4mf3pF2zttw2fcVMFYYnexPPnF5dAO2j_ifMp86H4A78Zd4W7xkQd1cEZ0t9a0YCbo4VSilMX4QBzhs3BUdFYMY8Q33X-Kyr5ZFCX5N6tg4a__o4LpnJ9ignl2_2JZTNc8OhKLWKRpiJNKb4pQ-kEGjOQm3zI";
    private String access_token;
    private String refreshToken;

    private final String ACCOUNT_URL = "https://accounts.spotify.com/";
    private final String ACCOUNT_AUTH_ENDPOINT = "authorize";
    private final String ACCOUNT_TOKEN_ENDPOINT = "api/token";

    private final String API_URL = "https://api.spotify.com/";
    private final String API_VERSION = "v1/";

    public SpotifyHTTPRequestBuilder(){
        loadToken();
    }

    public HTTPRequest buildAccessTokenRequest(){
        HTTPRequest request = new HTTPRequest(ACCOUNT_URL + ACCOUNT_TOKEN_ENDPOINT);
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
        HTTPRequest request = new HTTPRequest(ACCOUNT_URL + ACCOUNT_TOKEN_ENDPOINT);
        request.putURLParameter("grant_type", "refresh_token");
        request.putURLParameter("refresh_token", refreshToken);
        request.putURLParameter("redirect_uri", REDIRECT_URI);

        request.setRequestMethod("POST");

        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        request.putRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes())));
        request.putContentType("application/x-www-form-urlencoded");

        return request;
    }

    public String generateAuthUrl() {
        HTTPRequest request = new HTTPRequest(ACCOUNT_URL + ACCOUNT_AUTH_ENDPOINT);
        request.putURLParameter("client_id", CLIENT_ID);
        request.putURLParameter("response_type", "code");
        request.putURLParameter("redirect_uri", REDIRECT_URI);
        request.putURLParameter("show_dialog", "true");

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject jsonResponse = new JsonParser().parse(token).getAsJsonObject();
        access_token = jsonResponse.get("access_token").getAsString();
        refreshToken = jsonResponse.get("refresh_token").getAsString();
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public HTTPRequest buildUserProfileRequest() {
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("me"));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        return request;
    }

    public HTTPRequest buildSearchTrackRequest(String query, int limit){
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("search"));

        request.putURLParameter("q", query.replace(" ", "+"));
        request.putURLParameter("type", "track");
        request.putURLParameter("limit", String.valueOf(limit));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        return request;
    }

    public HTTPRequest buildGetPlaylistRequest(){
        HTTPRequest request = new HTTPRequest(buildAPIRequestURL("me/playlists"));

        request.putRequestProperty("Authorization", "Bearer " + access_token);

        return request;
    }

    private String buildAPIRequestURL(String endpoint){
        return API_URL + API_VERSION + endpoint;
    }

    public String getAccess_token() {
        return access_token;
    }
}