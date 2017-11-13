package Model;

import java.util.ArrayList;

public class Playlist {

    private String id;
    private String name;

    private String trackListUrl;
    private ArrayList<Track> listTrack;

    private ServiceProvider serviceProvider;

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

    public String getTrackListUrl() {
        return trackListUrl;
    }

    public void setTrackListUrl(String trackListUrl) {
        this.trackListUrl = trackListUrl;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
