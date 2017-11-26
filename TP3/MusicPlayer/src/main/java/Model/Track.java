package Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URL;

public class Track implements Serializable{

    private Metadata metadata;
    @SerializedName(value = "id", alternate = {"trackId"})
    private String id;
    private ServiceProvider serviceProvider;
    @SerializedName(value = "preview_url", alternate = {"audio","previewUrl"})
    private URL audioURL;

    public Track(Metadata metadata, String id, ServiceProvider serviceProvider) {
        this.metadata = metadata;
        this.id = id;
        this.serviceProvider = serviceProvider;
    }

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