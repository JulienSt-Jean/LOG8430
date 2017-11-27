package Services;

import Shared.Model.Track;

import java.util.List;

public interface ApiWrapper {
    List<Track> searchTrack(String searchEntry);
    /*List<Playlist> getPlaylists();
    Playlist addTrackToPlaylist(URI trackURI, String playlistId);
    Playlist removeTrackFromPlaylist(URI trackURI, String playlistId);
    Playlist createPlaylist(String name);
    Playlist deletePlaylist(String playlistId);*/
}
