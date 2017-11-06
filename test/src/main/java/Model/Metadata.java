package Model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Metadata {
    private String name;
    private String artists;
    private String album;

    public Metadata(String name, String artists, String album) {
        this.name = name;
        this.artists = artists;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
