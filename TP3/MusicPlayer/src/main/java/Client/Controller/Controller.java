package Client.Controller;


import Client.ClientStub.ITunesProxy;
import Client.ClientStub.JamendoProxy;
import Client.ClientStub.PlaylistHandlerProxy;
import Shared.Model.Exceptions.PlaylistException;
import Shared.Model.Playlist;
import Shared.Model.Track;
import Client.Player.Player;
import Client.View.Browser;
import Client.ClientStub.SpotifyProxy;

import java.util.ArrayList;
import java.util.Optional;

public class Controller {

    private Browser browser;
    private Player player;
    private Playlist currentPlaylist;

    private SpotifyProxy spotifyStub;
    private JamendoProxy jamendoStub;
    private ITunesProxy iTunesStub;

    private PlaylistHandlerProxy playlistHandlerStub;

    /**
     * Constructor
     *
     * @param browser The Client.View (web engine)
     */
    public Controller(Browser browser) {
        this.browser = browser;
        this.player = new Player(this);
        this.spotifyStub = new SpotifyProxy();
        this.jamendoStub = new JamendoProxy();
        this.iTunesStub = new ITunesProxy();

        this.playlistHandlerStub = new PlaylistHandlerProxy();

        System.out.println("Client.Controller created");
    }

    public void searchTrack(String searchEntry) {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.addAll(spotifyStub.searchTrack(searchEntry));
        tracks.addAll(jamendoStub.searchTrack(searchEntry));
        tracks.addAll(iTunesStub.searchTrack(searchEntry));
        if (!tracks.isEmpty())
            browser.getMainFrame().displaySearchResults(tracks);

    }

    public void playTrack(Track track) {
        if (player.getCurrentTrack() != track) {
            this.player.play(track);
            this.browser.getPlayerFrame().displayPause();
            this.browser.getPlayerFrame().displayTrackInfo(track);
        }
    }

    public void playNext() {
        player.playNext();

    }

    public void playPrevious() {
        player.playPrevious();

    }

    public void play_pauseClicked() {
        this.player.play_pause();
    }

    public void isPlaying() {
        this.browser.getPlayerFrame().displayPause();
    }

    public void isPaused() {
        this.browser.getPlayerFrame().displayPlay();
    }

    public void isStopped() {
        this.player.stop();
        this.browser.getPlayerFrame().displayPlay();
        this.browser.getPlayerFrame().hideTrackInfo();
    }

    public void isPlayingNewSong() {
        this.browser.getPlayerFrame().displayPause();
        this.browser.getPlayerFrame().displayTrackInfo(player.getCurrentTrack());
    }

    /**
     * Create a new playlist and display its name in the menu panel
     *
     * @param title
     * @throws PlaylistException
     */
    public void createPlaylist(String title) {
        try {
            playlistHandlerStub.createPlaylist(title);
            browser.getMenuFrame().addPlaylist(title);
        } catch (PlaylistException e) {
            browser.getPlaylistManagerFrame().showCreationWarning();
        }
    }

    /**
     * Fetch the playlist by its name and display the playlist in the main frame
     *
     * @param name
     */
    public void selectPlaylist(String name) {
        Playlist playlist = playlistHandlerStub.getPlaylistByName(name);
        if (playlist != null) {
            this.currentPlaylist = playlist;
            this.browser.getMainFrame().displayPlaylist(playlist);
        }
    }

    public void removeCurrentPlaylist() {
        System.out.println(currentPlaylist.getName());
        this.playlistHandlerStub.deletePlaylist(currentPlaylist);
        currentPlaylist = null;
        this.browser.getMenuFrame().updatePlaylists(getPlaylists());
        this.browser.getMainFrame().displaySearchField();
    }

    public void playCurrentPlaylist() {
        this.player.playPlaylist(currentPlaylist);
    }

    public void removeTrackFromCurrentPlaylist(String trackId) {
        playlistHandlerStub.removeTrackFromPlaylist(trackId, currentPlaylist.getName());
        currentPlaylist = playlistHandlerStub.getPlaylistByName(currentPlaylist.getName());
        this.browser.getMainFrame().displayPlaylist(currentPlaylist);
    }

    public void addTrack(Track track) {
        if (playlistHandlerStub.getPlaylists().size() == 0)
            browser.getMainFrame().showAddTrackWarning();
        else {
            ArrayList<String> playlistNames = new ArrayList<String>();
            for (Playlist p : playlistHandlerStub.getPlaylists())
                playlistNames.add(p.getName());

            Optional<String> playlistName = browser.getMainFrame().choosePlaylist(playlistNames);
            if (playlistName.isPresent())
                playlistHandlerStub.addTrackToPlaylist(track, playlistName.get());
        }
    }

    public void browseClicked() {
        browser.getMainFrame().displaySearchField();
    }

    public ArrayList<Playlist> getPlaylists(){
        return playlistHandlerStub.getPlaylists();
    }


    public PlaylistHandlerProxy getPlaylistHandlerStub() {
        return playlistHandlerStub;
    }
}
