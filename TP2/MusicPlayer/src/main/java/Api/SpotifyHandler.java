package Api;

import Api.Exceptions.WebApiException;
import Model.Playlist;
import Model.Track;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SpotifyHandler implements ApiWrapper {
    private String userId;
    private SpotifyHTTPRequestBuilder httpRequestBuilder = new SpotifyHTTPRequestBuilder();
    private SpotifyResponseParser parser = new SpotifyResponseParser();

    public SpotifyHandler() {
        JsonElement response = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildUserProfileRequest());
        setUserId(response);


    }

    public static void main(String args[]) {
        SpotifyHandler handler = new SpotifyHandler();
        ArrayList<Playlist> list = handler.getPlayLists();
        System.out.println(list.get(0).getTrackListUrl());
    }

    public ArrayList<Track> searchTrack(String searchEntry) {
        JsonElement response = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildSearchTrackRequest(searchEntry, 50));

        return parser.parseTrackList(response.getAsJsonObject().get("tracks"));
    }

    public void readTrack(String trackId) {

    }

    public ArrayList<Playlist> getPlayLists() {
        JsonElement playlistResponse = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildGetPlaylistRequest());

        ArrayList<Playlist> playlists = parser.parsePlaylists(playlistResponse);

        for (Playlist playlist : playlists) {
            JsonElement tracskResponse = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildRequest(playlist.getTrackListUrl()));
            playlist.setListTrack(parser.parseTrackList(tracskResponse));
        }

        return playlists;
    }

    private void generateAccessToken() {
        HTTPRequest request = httpRequestBuilder.buildAccessTokenRequest();

        try {
            request.makeConnection();
            Files.write(Paths.get("./tokens"), request.getResponse().getBytes());
            httpRequestBuilder.loadToken();
        } catch (WebApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshAccessToken() {
        HTTPRequest request = httpRequestBuilder.buildRefreshAccessTokenRequest();

        JsonObject jsonResponse = null;
        try {
            request.makeConnection();
            jsonResponse = new JsonParser().parse(request.getResponse()).getAsJsonObject();
        } catch (WebApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonResponse.addProperty("refresh_token", httpRequestBuilder.getRefreshToken());

        try {
            Files.write(Paths.get("./tokens"), jsonResponse.toString().getBytes());
            httpRequestBuilder.loadToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonElement executeRequestWithRetryOnExpiredToken(HTTPRequest request) {
        while (true) {
            try {
                request.makeConnection();
                System.out.println(request.getResponseCode());
                return new JsonParser().parse(request.getResponse());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (WebApiException e) {
                ApiError error = testErrorResponse(e.getMessage());
                if (error == ApiError.ACCESS_TOKEN_EXPIRED) {
                    refreshAccessToken();
                    request.putRequestProperty("Authorization", "Bearer " + httpRequestBuilder.getAccess_token());
                } else {
                    e.printStackTrace();
                    break;
                }
            }
        }

        return new JsonParser().parse("");
    }

    private ApiError testErrorResponse(String response) {
        JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();

        switch (jsonResponse.get("error").getAsJsonObject().get("message").getAsString()) {
            case "The access token expired":
                return ApiError.ACCESS_TOKEN_EXPIRED;
            default:
                return ApiError.UNKNOWN;
        }
    }

    private void setUserId(JsonElement response) {
        JsonObject jsonResponse = response.getAsJsonObject();
        userId = jsonResponse.get("id").getAsString();
    }

    private enum ApiError {
        ACCESS_TOKEN_EXPIRED,
        UNKNOWN
    }
}
