package Handler;

import Api.Exceptions.PlaylistException;
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

    public void createPlaylist(String title) throws PlaylistException{
        for(Playlist playlist : playlists){
            if(playlist.getName().equals(title)){
                throw new PlaylistException("La playlist existe deja");

            }
        }
        System.out.println("ajout de la playlist");
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
