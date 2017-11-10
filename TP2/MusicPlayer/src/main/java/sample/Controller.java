package sample;


import Api.Exceptions.PlaylistException;
import Handler.ApiHandler;
import Handler.PlaylistHandler;
import Model.Playlist;
import Model.Track;
import Player.Player;

import java.util.ArrayList;

public class Controller {

    private Player player;
    private PlaylistHandler playlistHandler;
    private ApiHandler apiHandler;

    public Controller() {
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

    public void createPlaylist(String title) throws PlaylistException{

            playlistHandler.createPlaylist(title);
            System.out.println("new playlist : "+title);

    }

    public void deletePlaylist(Playlist playlist){
        playlistHandler.deletePlaylist(playlist);
    }

    public void addSongtoPlaylist(Track track, Playlist playlist){
        playlist.addTrack(track);
    }

    public void removeSongFromPlaylist(Track track, Playlist playlist){
        playlist.removeTrack(track);
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

    public void setPlayer(Player player) {
        this.player = player;
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
