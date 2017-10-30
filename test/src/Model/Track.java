package Model;



public class Track {

    private Metadata metadata;
    private String id;
    private String serviceProvider;

    public Track(Metadata metadata, String id, String serviceProvider) {
        this.metadata = metadata;
        this.id = id;
        this.serviceProvider = serviceProvider;
    }

    public String getId() {
        return id;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }


}
