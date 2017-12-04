package Handler;

import Model.Exceptions.PlaylistException;
import Model.Metadata;
import Model.Playlist;
import Model.ServiceProvider;
import Model.Track;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistHandlerTest {

    PlaylistHandler pHandler;

    @BeforeEach
    void setUp(){
        pHandler = new PlaylistHandler();
    }

    @Test
    void createPlaylist(){
        try{
            pHandler.createPlaylist("Test");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }

        try{
            pHandler.createPlaylist("Test");
            assertTrue(false,"Ex[ected to recive a error");
        } catch (PlaylistException e) {
            //it's expected to create a error
        }

        try{
            pHandler.createPlaylist("2");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }
        assertNotNull(pHandler.getPlaylistByName("Test"));
        assertNotNull(pHandler.getPlaylistByName("2"));
    }

    @Test
    void deletePlaylist() {
        try{
            pHandler.createPlaylist("Test");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }
        pHandler.deletePlaylist(pHandler.getPlaylistByName("Test"));
        assertNull(pHandler.getPlaylistByName("Test"));

    }

    @Test
    void removeTrackFromPlaylist() {
        try{
            pHandler.createPlaylist("Test");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }

        Track t = new Track(new Metadata("t","t","t"),"test", ServiceProvider.ITUNES);

        pHandler.addTrackToPlaylist(t,"Test");

        pHandler.removeTrackFromPlaylist("testing false id","Test");

        pHandler.removeTrackFromPlaylist(t.getId(),"Test");

        assertSame(pHandler.getPlaylistByName("Test").getListTrack().size(), 0);


        pHandler.removeTrackFromPlaylist(t.getId(),"Test");

    }

    @Test
    void addTrackToPlaylist() {
        try{
            pHandler.createPlaylist("Test");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }

        Track t = new Track(new Metadata("t","t","t"),"test", ServiceProvider.ITUNES);

        pHandler.addTrackToPlaylist(t,"Test");

        assertSame(pHandler.getPlaylistByName("Test").getListTrack().get(0),t);
    }

    @Test
    void getPlaylists() {
        try{
            pHandler.createPlaylist("Test");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }
        try{
            pHandler.createPlaylist("Test2");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }
        try{
            pHandler.createPlaylist("Test3");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }

        ArrayList<Playlist> p = pHandler.getPlaylists();

        assertEquals(p.get(0).getName(), "Test");
        assertEquals(p.get(1).getName(), "Test2");
        assertEquals(p.get(2).getName(), "Test3");

    }

    @Test
    void getPlaylistByName() {
        try{
            pHandler.createPlaylist("Test");
        } catch (PlaylistException e) {
            assertTrue(false,"There is no playlist with the same name(should not throw a error)");
        }

        assertEquals(pHandler.getPlaylistByName("Test").getName(), "Test");

        assertNull(pHandler.getPlaylistByName("test2"));

        assertNull(pHandler.getPlaylistByName(null));
    }

}