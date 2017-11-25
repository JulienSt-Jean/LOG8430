package SOA;

import Model.Exceptions.PlaylistException;
import Model.Playlist;
import Model.Track;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PlaylistHandlerServerInterface extends Remote {
    void createPlaylist(String name) throws PlaylistException, RemoteException;
    void deletePlaylist(Playlist playlist) throws RemoteException;
    void removeTrackFromPlaylist(String trackId, String playlistId) throws RemoteException;
    void addTrackToPlaylist(Track track, String playlistName) throws RemoteException;
    ArrayList<Playlist> getPlaylists() throws RemoteException;
    Playlist getPlaylistByName(String name) throws RemoteException;
}
