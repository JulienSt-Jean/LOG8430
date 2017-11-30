package Api.ITunes;


import Model.Metadata;
import Model.ServiceProvider;
import Model.Track;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parse les réponses fournies (en JSON) par le service de streaming ITunes
 */
public class ITunesResponseParser {

    private Gson gson;

    /**
     * Constructeur
     */
    public ITunesResponseParser(){
        GsonBuilder builder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Track.class, new ITunesResponseParser.TrackDeserializer());

        this.gson = builder.create();
    }

    /**
     * Retourne sous forme d'une ArrayList Java une liste de pistes JSON
     * @param json objet JSON contenant la liste de pistes
     * @return
     */
    public ArrayList<Track> parseTrackList(JsonElement json){
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement trackList = jsonObject.get("results");

        return new ArrayList<>(Arrays.asList(gson.fromJson(trackList, Track[].class)));
    }

    /**
     * Gère la désérialisation d'une piste sous forme d'objet JSON en objet Track
     */
    private class TrackDeserializer implements JsonDeserializer<Track> {

        @Override
        public Track deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Gson gson = new Gson();

            Track track = gson.fromJson(jsonElement, Track.class);

            track.setMetadata(jsonDeserializationContext.deserialize(jsonElement, Metadata.class));

            track.setServiceProvider(ServiceProvider.ITUNES);

            return track;
        }
    }
}
