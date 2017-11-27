package Model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class Playlist implements Iterable<Track>{
    private ArrayList<Track> listTrack;
    private String name;

    //Constructor
    public Playlist(String name, ArrayList<Track> listTrack) {
        this.name = name;
        this.listTrack = listTrack;
    }

    public Playlist(String name) {
        this.name = name;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public ArrayList<Track> getListTrack() {
        return listTrack;
    }

    //Add a track to the playlist
    public void addTrack(Track track) {
        if (!listTrack.contains(track)) {
            listTrack.add(track);
        }
    }

    //Remove a track from the playlist
    public void removeTrack(Track track) {
        if (listTrack.contains(track)) {
            listTrack.remove(track);
        }
    }



    @Override
    public Iterator<Track> iterator() {
        return listTrack.iterator();
    }
}
