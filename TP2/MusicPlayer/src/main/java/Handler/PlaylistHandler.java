package Handler;

import Model.Exceptions.PlaylistException;
import Model.Playlist;
import Model.Track;
import Player.Player;

import javax.xml.bind.Element;
import java.util.ArrayList;

/**
 * Manipule les playlists crées par l'utilisateur
 */
public class PlaylistHandler {
    private ArrayList<Playlist> playlists;

    /**
     * Constructeur
     */
    public PlaylistHandler() {
        this.playlists = new ArrayList<Playlist>();
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

    /**
     * Suppression d'une playlist
     * @param playlist
     */
    public void deletePlaylist(Playlist playlist){
        playlists.remove(playlist);
    }

    /**
     * Supprime une piste d'une playlist
     * @param trackId identifiant de la piste
     * @param playlistId nom de la playlist
     */
    public void removeTrackFromPlaylist(String trackId, String playlistId){
        Playlist playlist = getPlaylistByName(playlistId);
        this.playlists.remove(playlist);
        for(Track track: playlist.getListTrack()){
            if(track.getId().equals(trackId)){
                playlist.removeTrack(track);
                System.out.println("Track : "+trackId + " removed from playlist : "+playlist.getName());
                this.playlists.add(playlist);
                break;
            }
        }
        this.playlists.add(playlist);
    }

    /**
     * Ajoute une piste à une playlist
     * @param track piste à ajouter
     * @param playlistName nom de la playlist
     */
    public void addTrackToPlaylist(Track track, String playlistName){
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist != null){
            playlist.addTrack(track);
        }
    }

    // Getter
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Retrouve une playlist par son nom
     * @param name
     * @return la playlist trouvée
     */
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

}
