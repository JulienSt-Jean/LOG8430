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
    List<Playlist> getPlaylists();
    void addTrackToPlaylist(URI trackURI, String playlistId);
    void removeTrackFromPlaylist(URI trackURI, String playlistId);
    Playlist createPlaylist(String name);
    void deletePlaylist(String playlistId);
}
