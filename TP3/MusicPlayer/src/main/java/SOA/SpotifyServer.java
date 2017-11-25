package SOA;

import Api.Spotify.SpotifyHandler;
import Model.Track;

import java.net.Inet4Address;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SpotifyServer implements SpotifyServerInterface, Runnable{

//    public static void main(String[] args) {
//            SpotifyServer server = new SpotifyServer();
//            server.run();
//    }
    private SpotifyHandler spotifyHandler;
    public SpotifyServer(){
        super();
        this.spotifyHandler = new SpotifyHandler();
        System.out.println("Launching SpotifyService");


    }

    public void run(){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            SpotifyServerInterface stub = (SpotifyServerInterface) UnicastRemoteObject.exportObject(this, 8080);
            Registry registry = LocateRegistry.getRegistry(8081);
            registry.rebind("server", stub);
            System.out.println("Server ready.");
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
