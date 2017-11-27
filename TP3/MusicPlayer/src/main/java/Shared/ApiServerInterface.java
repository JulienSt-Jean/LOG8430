package Shared;

import Shared.Model.Track;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ApiServerInterface extends Remote{
    ArrayList<Track> searchTrack(String trackName) throws RemoteException;
}
