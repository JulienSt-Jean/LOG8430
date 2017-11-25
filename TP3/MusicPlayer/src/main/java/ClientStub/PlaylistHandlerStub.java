package ClientStub;


import SOA.PlaylistHandlerServerInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PlaylistHandlerStub {
    private PlaylistHandlerServerInterface playlistHandlerStub;

    public PlaylistHandlerStub() {
        this.playlistHandlerStub = loadServerStub();
    }

    public PlaylistHandlerServerInterface loadServerStub(){
        PlaylistHandlerServerInterface stub = null;
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.1.1", 8083);
            stub = (PlaylistHandlerServerInterface) registry.lookup("PlaylistHandlerServer");
            System.out.println("Jamendo stub is created");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (AccessException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return stub;
    }
}
