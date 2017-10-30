package Model;

public class Metadata {
    private String title;
    private String artiste;
    private String album;

    public Metadata(String title, String artiste, String album) {
        this.title = title;
        this.artiste = artiste;
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
