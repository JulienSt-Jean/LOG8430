package Model;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

/**
 * Métadonnées d'une piste
 * - nom
 * - artiste
 * - album
 */
public class Metadata {
    @SerializedName(value = "name", alternate = {"trackName"})
    private String name;
    @SerializedName(value = "artists", alternate = {"artist_name","artistName"})
    private String artists;
    @SerializedName(value = "album", alternate = {"album_name","collectionName"})
    private String album;

    /**
     * Constructeur
     * @param name
     * @param artists
     * @param album
     */
    public Metadata(String name, String artists, String album) {
        this.name = name;
        this.artists = artists;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public String getArtists() {
        return artists;
    }

    public String getAlbum() {
        return album;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "name='" + name + '\'' +
                ", artists='" + artists + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
