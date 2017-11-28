package Services.SpotifyService;

import Services.SpotifyService.Spotify.SpotifyHandler;
import Shared.ServiceInterface.ApiServerInterface;
import Shared.Model.Track;

import java.net.Inet4Address;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SpotifyServer implements ApiServerInterface, Runnable{

    private SpotifyHandler spotifyHandler;

    public SpotifyServer(){
        super();
        this.spotifyHandler = new SpotifyHandler();
        System.out.println("Launching Services.SpotifyService");
    }

    public void run(){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            ApiServerInterface stub = (ApiServerInterface) UnicastRemoteObject.exportObject(this, 8080);
            Registry registry = LocateRegistry.getRegistry(8080);
            registry.rebind("SpotifyServer", stub);
            System.out.println("SpotifyServer ready.");
            System.out.println("IP :"+ Inet4Address.getLocalHost().getHostAddress());
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Track> searchTrack(String trackName) throws RemoteException{
        System.out.println("Spotify Service execute the search for :"+trackName);
        ArrayList<Track> results = new ArrayList<Track>();
        results.addAll(spotifyHandler.searchTrack(trackName));
        return results;
    }


}
