package Client.ClientStub;


import Shared.Model.Exceptions.PlaylistException;
import Shared.Model.Playlist;
import Shared.Model.Track;
import Shared.ServiceInterface.PlaylistHandlerServerInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class PlaylistHandlerStub {
    private PlaylistHandlerServerInterface playlistHandlerStub;

    public PlaylistHandlerStub() {
        this.playlistHandlerStub = loadServerStub();
    }

    private PlaylistHandlerServerInterface loadServerStub(){
        PlaylistHandlerServerInterface stub = null;
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.1.1", 8083);
            stub = (PlaylistHandlerServerInterface) registry.lookup("PlaylistHandlerServer");
            System.out.println("PlaylistHandler stub is created");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (AccessException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return stub;
    }


    /**
     * Add a new Playlist to the existing playlists
     * @param name
     * @throws PlaylistException
     */
    public void createPlaylist(String name) throws PlaylistException{
        try{
            playlistHandlerStub.createPlaylist(name);

        }catch(RemoteException re){
            System.out.println(re.getMessage());
        }catch (PlaylistException pe){
            throw new PlaylistException("La playlist existe deja");
        }

    }

    public void deletePlaylist(Playlist playlist){

        try {
            playlistHandlerStub.deletePlaylist(playlist);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    public void removeTrackFromPlaylist(String trackId, String playlistId){
        try {
            playlistHandlerStub.removeTrackFromPlaylist(trackId, playlistId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addTrackToPlaylist(Track track, String playlistName){
        try {
            playlistHandlerStub.addTrackToPlaylist(track, playlistName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Playlist> getPlaylists(){

        ArrayList<Playlist> result = new ArrayList<>();

        try {
            result.addAll(playlistHandlerStub.getPlaylists());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Playlist getPlaylistByName(String name){
        Playlist playlist = null;
        try {
            playlist = playlistHandlerStub.getPlaylistByName(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return playlist;
    }
}
