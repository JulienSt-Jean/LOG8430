package Services.SpotifyService.Spotify;

import Services.ServiceUtilities.ApiWrapper;
import Services.Exceptions.WebApiException;
import Services.ServiceUtilities.HTTPRequest;
import Shared.Model.Playlist;
import Shared.Model.Track;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SpotifyHandler implements ApiWrapper {
    private SpotifyHTTPRequestBuilder httpRequestBuilder = new SpotifyHTTPRequestBuilder();
    private SpotifyResponseParser parser = new SpotifyResponseParser();

    public static void main(String args[]) {
        SpotifyHandler handler = new SpotifyHandler();
        List<Playlist> list = handler.getPlaylists();
        handler.deletePlaylist(list.get(0).getId());
        list = handler.getPlaylists();
        System.out.println(list);
    }

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

        return parser.parseTrackList(response.getAsJsonObject().get("tracks"));
    }

    public List<Playlist> getPlaylists() {
        JsonElement playlistResponse = null;
        try {
            playlistResponse = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildGetPlaylistsRequest());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ArrayList<Playlist> playlists = parser.parsePlaylists(playlistResponse);

        for (Playlist playlist : playlists) {
            JsonElement tracskResponse = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildRequest(playlist.getTrackListUrl()));
            playlist.setListTrack(parser.parseTrackList(tracskResponse));
        }

        return playlists;
    }

    public Playlist getPlaylist(String playlistId) {
        JsonElement playlistResponse = null;
        try {
            playlistResponse = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildGetPlaylistRequest(playlistId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Playlist playlist = parser.parsePlaylist(playlistResponse);

        JsonElement tracskResponse = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildRequest(playlist.getTrackListUrl()));
        playlist.setListTrack(parser.parseTrackList(tracskResponse));

        return playlist;
    }

    public Playlist addTrackToPlaylist(URI trackURI, String playlistId) {
        try {
            this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildAddTrackToPlaylistRequest(trackURI, playlistId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return getPlaylist(playlistId);
    }

    public Playlist removeTrackFromPlaylist(URI trackURI, String playlistId) {
        try {
            this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildRemoveTrackFromPlaylistRequest(trackURI, playlistId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return getPlaylist(playlistId);
    }

    public Playlist createPlaylist(String name) {
        JsonElement playlistResponse = null;
        try {
            playlistResponse = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildCreatePlaylistRequest(name));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return parser.parsePlaylist(playlistResponse);
    }

    public Playlist deletePlaylist(String playlistId) {
        try {
            this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildUnfollowPlaylistRequest(playlistId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return getPlaylist(playlistId);
    }

    private void generateAccessToken() {
        HTTPRequest request = httpRequestBuilder.buildAccessTokenRequest(new String[]{"playlist-modify-public", "playlist-modify-private"});

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
