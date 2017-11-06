package Api;

import Model.Track;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public interface ApiWrapper {
    ArrayList<Track> searchTrack(String searchEntry);
    void readTrack(String trackId);

    class TrackDeserializer{

    }
}
