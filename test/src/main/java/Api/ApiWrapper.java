package Api;

import org.json.JSONArray;

public interface ApiWrapper {
    JSONArray searchTrack(String searchEntry);
    void readTrack(String trackId);
}
