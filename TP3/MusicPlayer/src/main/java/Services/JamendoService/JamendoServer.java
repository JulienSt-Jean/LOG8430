package Services.JamendoService;

import Services.JamendoService.Jamendo.JamendoHandler;
import Shared.ServiceInterface.ApiServerInterface;
import Shared.Model.Track;

import java.net.Inet4Address;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class JamendoServer implements ApiServerInterface, Runnable{

    private JamendoHandler jamendoHandler;


    public JamendoServer(){
        super();
        this.jamendoHandler = new JamendoHandler();
        System.out.println("Launching Services.SpotifyService");


    }

    public void run(){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            ApiServerInterface stub = (ApiServerInterface) UnicastRemoteObject.exportObject(this, 8081);
            Registry registry = LocateRegistry.getRegistry(8081);
            registry.rebind("JamendoServer", stub);
            System.out.println("JamendoServer ready.");
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
        System.out.println("Jamendo Service execute the search for :"+trackName);
        ArrayList<Track> results = new ArrayList<Track>();
        results.addAll(jamendoHandler.searchTrack(trackName));
        return results;
    }
}
