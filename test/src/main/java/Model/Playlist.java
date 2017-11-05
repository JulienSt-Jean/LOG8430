package Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Playlist {

    private String id;
    private String title;

    @SerializedName("/tracks/href")
    private String trackListUrl;
    private ArrayList<Track> listTrack;

    //Constructor
    public Playlist(String title, ArrayList<Track> listTrack) {
        this.title = title;
        this.listTrack = listTrack;
    }
    public Playlist(String title) {
        this.title = title;
       
    }

    //Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Track> getListTrack() {
        return listTrack;
    }

    public void setListTrack(ArrayList<Track> listTrack) {
        this.listTrack = listTrack;
    }


    //Add a track to the playlist
    public void addTrack(Track track){
        if(!listTrack.contains(track)){
            listTrack.add(track);
        }
    }

    //Remove a track from the playlist
    public void removeTrack(Track track){
        if(listTrack.contains(track)) {
            listTrack.remove(track);
        }
    }

}
