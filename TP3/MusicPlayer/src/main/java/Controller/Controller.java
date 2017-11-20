package Controller;


import Model.Exceptions.PlaylistException;
import Handler.ApiHandler;
import Handler.PlaylistHandler;
import Model.Playlist;
import Model.Track;
import Player.Player;
import View.Browser;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;

public class Controller {

    private Browser browser;
    private Player player;
    private PlaylistHandler playlistHandler;
    private ApiHandler apiHandler;

    private Playlist currentPlaylist;

    /**
     * Constructor
     * @param browser The View (web engine)
     */
    public Controller(Browser browser) {
        this.browser = browser;
        this.apiHandler = new ApiHandler();
        this.player = new Player(this);
        this.playlistHandler = new PlaylistHandler(this.player);
        System.out.println("Controller created");
    }

    public void searchTrack(String searchEntry){
        ArrayList<Track> tracks = apiHandler.searchTrack(searchEntry);
        if (!tracks.isEmpty())
            browser.getMainFrame().displaySearchResults(tracks);
    }

    public void playTrack(Track track){
        if (player.getCurrentTrack() != track) {
            this.player.play(track);
            this.browser.getPlayerFrame().displayPause();
            this.browser.getPlayerFrame().displayTrackInfo(track);
        }
    }

    public void playNext(){
        player.playNext();
        // TODO : Vérifier si ok et get track
        this.browser.getPlayerFrame().displayTrackInfo(player.getCurrentTrack());
    }

    public void playPrevious(){
        player.playPrevious();
        // TODO : Vérifier si ok et get track
        this.browser.getPlayerFrame().displayTrackInfo(player.getCurrentTrack());
    }

    public void play_pauseClicked(){
        this.player.play_pause();
    }

    public void isPlaying(){
        this.browser.getPlayerFrame().displayPause();
    }

    public void isPaused(){
        this.browser.getPlayerFrame().displayPlay();
    }

    public void isStopped(){
        this.browser.getPlayerFrame().displayPlay();
        this.browser.getPlayerFrame().hideTrackInfo();
    }

    public void isPlayingNewSong(){
        this.browser.getPlayerFrame().displayPause();
        this.browser.getPlayerFrame().displayTrackInfo(player.getCurrentTrack());
    }

    /**
     * Create a new playlist and display its name in the menu panel
     * @param title
     * @throws PlaylistException
     */
    public void createPlaylist(String title){
        try {
            playlistHandler.createPlaylist(title);
            browser.getMenuFrame().addPlaylist(title);
        } catch (PlaylistException e) {
            browser.getPlaylistManagerFrame().showCreationWarning();
        }
    }

    /**
     * Fetch the playlist by its name and display the playlist in the main frame
     * @param name
     */
    public void selectPlaylist(String name){
        Playlist playlist = getPlaylistHandler().getPlaylistByName(name);
        if(playlist != null) {
            this.currentPlaylist = playlist;
            this.browser.getMainFrame().displayPlaylist(playlist);
        }
    }

    public void removeCurrentPlaylist() {
        this.getPlaylistHandler().deletePlaylist(currentPlaylist);
        currentPlaylist = null;
        this.browser.getMenuFrame().updatePlaylists();
        this.browser.getMainFrame().displaySearchField();
    }

    public void playCurrentPlaylist(){
        playlistHandler.playPlaylist(currentPlaylist);
    }

    public void removeTrackFromCurrentPlaylist(String trackId){
        playlistHandler.removeTrackFromPlaylist(trackId, currentPlaylist.getName());
        this.browser.getMainFrame().displayPlaylist(currentPlaylist);
    }

    public void addTrack(Track track){
        if (playlistHandler.getPlaylists().size() == 0)
            browser.getMainFrame().showAddTrackWarning();
        else{
            ArrayList<String> playlistNames = new ArrayList<String>();
            for(Playlist p : playlistHandler.getPlaylists())
                playlistNames.add(p.getName());

            Optional<String> playlistName = browser.getMainFrame().choosePlaylist(playlistNames);
            if (playlistName.isPresent())
                playlistHandler.addTrackToPlaylist(track, playlistName.get());
        }
    }

    public void browseClicked(){
        browser.getMainFrame().displaySearchField();
    }

    public PlaylistHandler getPlaylistHandler() {
        return playlistHandler;
    }

}
