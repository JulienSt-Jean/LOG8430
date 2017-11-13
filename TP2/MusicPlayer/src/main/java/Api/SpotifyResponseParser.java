package Api;

import Model.Metadata;
import Model.Playlist;
import Model.ServiceProvider;
import Model.Track;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class SpotifyResponseParser {
    private Gson gson;

    public SpotifyResponseParser(){
        GsonBuilder builder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Track.class, new TrackDeserializer())
                .registerTypeAdapter(Metadata.class, new MetadataDeserializer())
                .registerTypeAdapter(Playlist.class, new PlaylistDeserializer());

        this.gson = builder.create();
    }

    public ArrayList<Track> parseTrackList(JsonElement json){
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement trackList = jsonObject.get("items");

        return new ArrayList<>(Arrays.asList(gson.fromJson(trackList, Track[].class)));
    }

    public ArrayList<Playlist> parsePlaylists(JsonElement json){
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement playLists = jsonObject.get("items");

        return new ArrayList<>(Arrays.asList(gson.fromJson(playLists, Playlist[].class)));
    }

    public Playlist parsePlaylist(JsonElement json) {
        return gson.fromJson(json, Playlist.class);
    }

    private class TrackDeserializer implements JsonDeserializer<Track> {

        @Override
        public Track deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Gson gson = new Gson();

            if (jsonElement.getAsJsonObject().has("track")) {
                jsonElement = jsonElement.getAsJsonObject().get("track");
            }

            Track track = gson.fromJson(jsonElement, Track.class);

            track.setMetadata(jsonDeserializationContext.deserialize(jsonElement, Metadata.class));

            track.setServiceProvider(ServiceProvider.SPOTIFY);

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
                artists.append(jsonArtist.getAsJsonObject().get("name").getAsString()).append(", ");
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

            playlist.setServiceProvider(ServiceProvider.SPOTIFY);

            return playlist;
        }
    }
}
