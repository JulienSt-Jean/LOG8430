package Api.Spotify;

import Api.ApiWrapper;
import Api.Exceptions.WebApiException;
import Api.HTTPRequest;
import Model.Playlist;
import Model.Track;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpotifyHandler implements ApiWrapper {
    private SpotifyHTTPRequestBuilder httpRequestBuilder = new SpotifyHTTPRequestBuilder();
    private SpotifyResponseParser parser = new SpotifyResponseParser();

    public SpotifyHandler() {
        JsonElement response = null;
        try {
            response = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildUserProfileRequest());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        httpRequestBuilder.setUserId(response);
    }

    @Override
    public List<Track> searchTrack(String searchEntry) {
        JsonElement response = null;
        try {
            response = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildSearchTrackRequest(searchEntry, 50));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Track> notCleanList = parser.parseTrackList(response.getAsJsonObject().get("tracks"));
        List<Track> cleanList = new LinkedList<Track>();

        for (Track track: notCleanList) {
            if(track.getAudioURL() != null){
                cleanList.add(track);
            }
        }

        return cleanList;
    }

    public void refreshAccessToken() {
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

    private enum ApiError {
        ACCESS_TOKEN_EXPIRED,
        UNKNOWN
    }
}
