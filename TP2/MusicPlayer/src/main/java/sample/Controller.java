package sample;


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

    public void createPlaylist(String title){
        playlistHandler.createPlaylist(title);
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

}
