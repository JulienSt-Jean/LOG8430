package Api;

import Api.Exceptions.WebApiException;
import Model.Metadata;
import Model.Playlist;
import Model.Track;
import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class SpotifyHandler implements ApiWrapper {
    public static void main(String args[]) {
        SpotifyHandler handler = new SpotifyHandler();
        ArrayList<Playlist> list = handler.getPlayLists();
        System.out.println(list.toString());
    }

    private String userId;

    private SpotifyHTTPRequestBuilder httpRequestBuilder = new SpotifyHTTPRequestBuilder();

    private Gson gson;

    private enum ApiError {
        ACCESS_TOKEN_EXPIRED,
        UNKNOWN
    }

    public SpotifyHandler(){
        String response = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildUserProfileRequest());
        setUserId(response);

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Track.class, new TrackDeserializer())
                .registerTypeAdapter(Metadata.class, new MetadataDeserializer());

        this.gson = builder.create();
    }

    public ArrayList<Track> searchTrack(String searchEntry) {
        String response = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildSearchTrackRequest(searchEntry, 50));

        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonElement trackList = jsonObject.get("tracks").getAsJsonObject().get("items");

        return new ArrayList(Arrays.asList(gson.fromJson(trackList, Track[].class)));
    }

    public void readTrack(String trackId) {

    }

    public ArrayList<Playlist> getPlayLists(){
        String response = this.executeRequestWithRetryOnExpiredToken(httpRequestBuilder.buildGetPlaylistRequest());

        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonElement playLists = jsonObject.get("items");

        return new ArrayList(Arrays.asList(gson.fromJson(playLists, Playlist[].class)));
    }

    private void generateAccessToken(){
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

    private void refreshAccessToken(){
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

    private String executeRequestWithRetryOnExpiredToken(HTTPRequest request){
        while(true) {
            try {
                request.makeConnection();
                System.out.println(request.getResponseCode());
                return request.getResponse();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (WebApiException e) {
                ApiError error = testErrorResponse(e.getMessage());
                if (error == ApiError.ACCESS_TOKEN_EXPIRED){
                    refreshAccessToken();
                    request.putRequestProperty("Authorization", "Bearer " + httpRequestBuilder.getAccess_token());
                }
                else {
                    e.printStackTrace();
                    break;
                }
            }
        }

        return "";
    }

    private ApiError testErrorResponse(String response){
        JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();

        switch(jsonResponse.get("error").getAsJsonObject().get("message").getAsString()){
            case "The access token expired":
                return ApiError.ACCESS_TOKEN_EXPIRED;
            default:
                return ApiError.UNKNOWN;
        }
    }

    private void setUserId(String response){
        JsonObject jsonResponse = new JsonParser().parse(response).getAsJsonObject();
        userId = jsonResponse.get("id").getAsString();
    }

    private class TrackDeserializer implements JsonDeserializer<Track>{

        @Override
        public Track deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Gson gson = new Gson();
            Track track = gson.fromJson(jsonElement, Track.class);

            track.setMetadata(jsonDeserializationContext.deserialize(jsonElement, Metadata.class));

            return track;
        }
    }

    private class MetadataDeserializer implements JsonDeserializer<Metadata>{

        @Override
        public Metadata deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObj = jsonElement.getAsJsonObject();

            String album = jsonObj.get("album").getAsJsonObject().get("name").getAsString();
            String track = jsonObj.get("name").getAsString();

            JsonArray jsonArtists = jsonObj.get("artists").getAsJsonArray();
            StringBuilder artists = new StringBuilder();
            for(JsonElement jsonArtist : jsonArtists){
                artists.append(jsonArtist.getAsJsonObject().get("name").getAsString() + ", ");
            }

            return new Metadata(track, artists.toString().substring(0, artists.length() - 2), album);
        }
    }

    private class PlaylistDeserializer implements JsonDeserializer<Playlist>{

        @Override
        public Playlist deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Gson gson = new Gson();
            Playlist playlist = gson.fromJson(jsonElement, Playlist.class);

            playlist.setTrackListUrl(jsonElement.getAsJsonObject().get("tracks").getAsJsonObject().get("href").getAsString());

            return playlist;
        }
    }
}
