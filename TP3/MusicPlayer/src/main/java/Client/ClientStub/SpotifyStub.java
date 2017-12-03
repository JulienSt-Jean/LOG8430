package Client.ClientStub;

import Shared.Model.Track;
import Shared.ServiceInterface.ApiServerInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class SpotifyStub {

    private ApiServerInterface spotifyStub;
    public SpotifyStub() {
        this.spotifyStub = loadServerStub();

    }

    private ApiServerInterface loadServerStub(){
        ApiServerInterface stub = null;
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.1.1", 8080);
            stub = (ApiServerInterface) registry.lookup("SpotifyServer");
            System.out.println("Spotify stub is created");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (AccessException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return stub;
    }

    public ArrayList<Track> searchTrack(String trackName){
        ArrayList<Track> result = new ArrayList<>();
        try{
            result = spotifyStub.searchTrack(trackName);
            System.out.println("Client search succeded");
        }catch(RemoteException e){
            System.out.println(e);
        }

        return result;
    }
}
