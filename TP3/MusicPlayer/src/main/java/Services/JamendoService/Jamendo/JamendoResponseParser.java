package Services.JamendoService.Jamendo;

import Shared.Model.Metadata;
import Shared.Model.ServiceProvider;
import Shared.Model.Track;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class JamendoResponseParser {
    private Gson gson;

    public JamendoResponseParser(){
        GsonBuilder builder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Track.class, new TrackDeserializer());

        this.gson = builder.create();
    }

    public ArrayList<Track> parseTrackList(JsonElement json){
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement trackList = jsonObject.get("results");

        return new ArrayList<>(Arrays.asList(gson.fromJson(trackList, Track[].class)));
    }

    private class TrackDeserializer implements JsonDeserializer<Track> {

        @Override
        public Track deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Gson gson = new Gson();

            Track track = gson.fromJson(jsonElement, Track.class);

            track.setMetadata(jsonDeserializationContext.deserialize(jsonElement, Metadata.class));

            track.setServiceProvider(ServiceProvider.JAMENDO);

            return track;
        }
    }
}
