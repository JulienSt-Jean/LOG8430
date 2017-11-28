package Services.ServiceUtilities;

import Shared.Model.Track;

import java.util.List;

public interface ApiWrapper {
    List<Track> searchTrack(String searchEntry);
}
