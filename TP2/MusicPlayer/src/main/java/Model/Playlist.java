package Model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Représente une Playlist (nom et liste de pistes)
 */
public class Playlist implements Iterable<Track>{
    private ArrayList<Track> listTrack;
    private String name;

    /**
     * Constructeur
     * @param name nom de la playlist
     * @param listTrack liste de pistes
     */
    public Playlist(String name, ArrayList<Track> listTrack) {
        this.name = name;
        this.listTrack = listTrack;
    }

    /**
     * Constructeur (playliste vide)
     * @param name nom de la playlist
     */
    public Playlist(String name) {
        this.name = name;
        this.listTrack = new ArrayList<Track>();
    }


    //Getters
    public String getName() {
        return name;
    }
    public ArrayList<Track> getListTrack() {
        return listTrack;
    }

    /**
     * Ajoute une piste à la playlist
     * @param track piste à ajouter
     */
    public void addTrack(Track track) {
        if (!listTrack.contains(track)) {
            listTrack.add(track);
        }
    }

    /**
     * Supprime une piste de la playlist
     * @param track piste à supprimer
     */
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
