package SOA;

import Api.ITunes.ITunesHandler;
import Model.Track;

import java.net.Inet4Address;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ITunesServer implements  ApiServerInterface, Runnable {
    private ITunesHandler iTunesHandler;


    public ITunesServer(){
        super();
        this.iTunesHandler = new ITunesHandler();
        System.out.println("Launching ITunesService");
    }

    public void run(){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            ApiServerInterface stub = (ApiServerInterface) UnicastRemoteObject.exportObject(this, 8082);
            Registry registry = LocateRegistry.getRegistry(8082);
            registry.rebind("ITunesServer", stub);
            System.out.println("ITunesServer ready.");
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
    public ArrayList<Track> searchTrack(String trackName) throws RemoteException {
        System.out.println("Itunes Service execute the search for :"+trackName);
        ArrayList<Track> results = new ArrayList<Track>();
        results.addAll(iTunesHandler.searchTrack(trackName));
        return results;
    }
}
