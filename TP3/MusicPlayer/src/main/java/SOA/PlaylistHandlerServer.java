package SOA;

import Model.Exceptions.PlaylistException;
import Model.Playlist;
import Model.Track;
import Player.Player;

import java.net.Inet4Address;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PlaylistHandlerServer implements PlaylistHandlerServerInterface, Runnable{
    private ArrayList<Playlist> playlists;

    public PlaylistHandlerServer() {
        this.playlists = new ArrayList<Playlist>();
        System.out.println("PlaylistHandler created");
    }

    public void run(){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            PlaylistHandlerServerInterface stub = (PlaylistHandlerServerInterface) UnicastRemoteObject.exportObject(this, 8083);
            Registry registry = LocateRegistry.getRegistry(8083);
            registry.rebind("PlaylistHandlerServer", stub);
            System.out.println("PlaylistHandlerServer ready.");
            System.out.println("IP :"+ Inet4Address.getLocalHost().getHostAddress());
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }


    /**
     * Add a new Playlist to the existing playlists
     * @param name
     * @throws PlaylistException
     */
    @Override
    public void createPlaylist(String name) throws PlaylistException, RemoteException{
        for(Playlist playlist : playlists){
            if(playlist.getName().equals(name)){
                throw new PlaylistException("La playlist existe deja");

            }
        }
        System.out.println("ajout de la playlist");
        playlists.add(new Playlist(name, new ArrayList<Track>()));


    }
    @Override
    public void deletePlaylist(Playlist playlist) throws RemoteException{
        playlists.remove(playlist);
    }


    @Override
    public void removeTrackFromPlaylist(String trackId, String playlistId) throws RemoteException{
        Playlist playlist = getPlaylistByName(playlistId);
        this.playlists.remove(playlist);
        for(Track track: playlist.getListTrack()){
            if(track.getId().equals(trackId)){
                playlist.removeTrack(track);
                System.out.println("Track : "+trackId + " removed from playlist : "+playlist.getName());
                this.playlists.add(playlist);
                break;
            }
        }
    }
    @Override
    public void addTrackToPlaylist(Track track, String playlistName) throws RemoteException{
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist != null){
            playlist.addTrack(track);
        }
    }
    @Override
    public ArrayList<Playlist> getPlaylists() throws RemoteException {
        return playlists;
    }

    @Override
    public Playlist getPlaylistByName(String name) throws RemoteException {
        if(name == null || name.equals("")){
            return null;
        }
        else{
            for(Playlist playlist: playlists){
                if(name.equals(playlist.getName())){
                    return playlist;
                }
            }
            return null;
        }

    }
}
