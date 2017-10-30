package Api;

import Model.Track;
import org.json.JSONArray;

import java.util.ArrayList;

public interface ApiWrapper {
    JSONArray searchTrack(String searchEntry);
    void readTrack(String trackId);
}
