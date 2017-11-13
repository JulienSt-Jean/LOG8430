package Model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class Playlist implements Iterable<Track>{
    private ArrayList<Track> listTrack;

    private ServiceProvider serviceProvider;

    private String id;
    private String name;

    private URL trackListUrl;

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

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Track> getListTrack() {
        return listTrack;
    }

    public void setListTrack(ArrayList<Track> listTrack) {
        this.listTrack = listTrack;
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

    public URL getTrackListUrl() {
        return trackListUrl;
    }

    public void setTrackListUrl(URL trackListUrl) {
        this.trackListUrl = trackListUrl;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public String getId() {
        return id;
    }

    @Override
    public Iterator<Track> iterator() {
        return listTrack.iterator();
    }
}
