package Model;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

/**
 * Représente une piste et ses métadonnées (titre, artiste, album...)
 */
public class Track {

    private Metadata metadata;
    @SerializedName(value = "id", alternate = {"trackId"})
    private String id;
    private ServiceProvider serviceProvider;
    @SerializedName(value = "preview_url", alternate = {"audio","previewUrl"})
    private URL audioURL;

    /**
     * Constructeur
     * @param metadata métadonnées de la piste
     * @param id identifie la piste
     * @param serviceProvider (Spotify, Jamendo, ITunes)
     */
    public Track(Metadata metadata, String id, ServiceProvider serviceProvider) {
        this.metadata = metadata;
        this.id = id;
        this.serviceProvider = serviceProvider;
    }

    // Getters / Setters
    public String getId() {
        return id;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public URL getAudioURL() {
        return audioURL;
    }
}