package Api.Spotify;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpotifyHandlerTest {
    @Test
    void refreshAccessToken() {
        SpotifyHandler h = new SpotifyHandler();
        h.refreshAccessToken();

    }

}