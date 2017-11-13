package Handler;

import Model.Exceptions.PlaylistException;
import Model.Playlist;
import Model.Track;
import Player.Player;

import javax.xml.bind.Element;
import java.util.ArrayList;

public class PlaylistHandler {
    private ArrayList<Playlist> playlists;
    private Player player;

    public PlaylistHandler(Player player) {
        this.playlists = new ArrayList<Playlist>();
        this.player = player;
        System.out.println("PlaylistHandler created");
    }


    /**
     * Add a new Playlist to the existing playlists
     * @param name
     * @throws PlaylistException
     */
    public void createPlaylist(String name) throws PlaylistException{
        for(Playlist playlist : playlists){
            if(playlist.getName().equals(name)){
                throw new PlaylistException("La playlist existe deja");

            }
        }
        System.out.println("ajout de la playlist");
        playlists.add(new Playlist(name, new ArrayList<Track>()));


    }

    public void deletePlaylist(Playlist playlist){
        playlists.remove(playlist);
    }

    public void playPlaylist(Playlist playlist){
        System.out.println("Play playlist :"+playlist.getName());
        player.clearQueue();
        player.addToQueue(playlist.getListTrack());
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public Playlist getPlaylistByName(String name){
        if(name == null || name.equals("")){
            return null;
        }
        else{
            for(Playlist playlist: playlists){
                if(name.equals(playlist.getName())){
                    return playlist;
                }
            }
            return null;
        }

    }
    public Player getPlayer() {
        return player;
    }
}
