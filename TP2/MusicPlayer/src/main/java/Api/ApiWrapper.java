package Api;

import Model.Playlist;
import Model.Track;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public interface ApiWrapper {
    List<Track> searchTrack(String searchEntry);

}
