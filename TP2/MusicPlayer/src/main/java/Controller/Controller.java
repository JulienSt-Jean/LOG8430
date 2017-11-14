package Controller;


import Model.Exceptions.PlaylistException;
import Handler.ApiHandler;
import Handler.PlaylistHandler;
import Model.Playlist;
import Model.Track;
import Player.Player;
import View.Browser;

import java.util.ArrayList;

public class Controller {

    private Browser browser;
    private Player player;
    private PlaylistHandler playlistHandler;
    private ApiHandler apiHandler;

    public Controller(Browser browser) {
        this.browser = browser;
        this.apiHandler = new ApiHandler();
        this.player = new Player(this.apiHandler);
        this.playlistHandler = new PlaylistHandler(this.player);
        System.out.println("Controller created");
    }


    public ArrayList<Track> searchTrack(String searchEntry){
        return apiHandler.searchTrack(searchEntry);
    }

    public void playTrack(Track track){
        player.play(track);
    }

    /**
     * Create a new playlist and display its name in the menu panel
     * @param title
     * @throws PlaylistException
     */
    public void createPlaylist(String title) throws PlaylistException{
        playlistHandler.createPlaylist(title);
        System.out.println("new playlist : "+title);
        browser.getMenuFrame().addPlaylist(title);

    }

    /**
     * Fetch the playlist by its name and display the playlist in the main frame
     * @param name
     */
    public void displayPlaylistInMain(String name){
        Playlist playlist = getPlaylistHandler().getPlaylistByName(name);
        if(!playlist.equals(null)){
            browser.getMainFrame().displayPlaylist(playlist);
        }

    }

    public void removePlaylist(Playlist playlist) {
        this.getPlaylistHandler().deletePlaylist(playlist);
        this.browser.getMenuFrame().updatePlaylists();
    }

    public void playPlaylist(Playlist playlist){
        playlistHandler.playPlaylist(playlist);
        this.browser.getPlayerFrame().play();
    }

    public void removeTrackFromPlaylist(String trackId, String playlistId){
        getPlaylistHandler().removeTrackFromPlaylist(trackId, playlistId);
        this.browser.getMainFrame().displayPlaylist(getPlaylistHandler().getPlaylistByName(playlistId));
    }

    public void displaySearchDiv(){
        browser.getMainFrame().displaySearchDiv();
    }

    public void playMusic(){
        System.out.println("music is playing");
    }

    public void pauseMusic(){
        System.out.println("Music is paused");
    }

    public void deletePlaylist(Playlist playlist){
        playlistHandler.deletePlaylist(playlist);
    }

    public void addSongtoPlaylist(Track track, Playlist playlist){
        playlist.addTrack(track);
    }



    public void listenTrack(Track track){
        player.play(track);
    }

    public void listenPlaylist(Playlist playlist){
        playlistHandler.playPlaylist(playlist);
    }


    public Player getPlayer() {
        return player;
    }


    public PlaylistHandler getPlaylistHandler() {
        return playlistHandler;
    }

    public void setPlaylistHandler(PlaylistHandler playlistHandler) {
        this.playlistHandler = playlistHandler;
    }

    public ApiHandler getApiHandler() {
        return apiHandler;
    }

    public void setApiHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }
}
