package Handler;

import Model.Playlist;
import Player.Player;

import java.util.ArrayList;

public class PlaylistHandler {
    private ArrayList<Playlist> playlists;
    private Player player;

    public PlaylistHandler(Player player) {
        this.playlists = new ArrayList<Playlist>();
        this.player = player;
        System.out.println("PlaylistHandler created");
    }

    public void createPlaylist(String title){
        playlists.add(new Playlist(title));
    }

    public void deletePlaylist(Playlist playlist){
        playlists.remove(playlist);
    }

    public void playPlaylist(Playlist playlist){
        player.clearQueue();
        player.addToQueue(playlist.getListTrack());
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public Player getPlayer() {
        return player;
    }
}
