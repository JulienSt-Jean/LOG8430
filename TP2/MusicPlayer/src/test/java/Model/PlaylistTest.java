package Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    @Test
    void iterator() {
        Playlist p = new Playlist("Hello World");
        Track t = new Track(new Metadata("t","t","t"),"test", ServiceProvider.ITUNES);
        p.addTrack(t);
        assertNotNull(p.iterator());
    }

}