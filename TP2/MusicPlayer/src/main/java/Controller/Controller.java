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

/**
 * Contrôleur de l'application. Assure le binding Vue - Modèle.
 * Fait appel à :
 * - un Player spécialisé pour la lecture des pistes et playlists
 * - un PlaylistManager pour la gestion des playlists
 * - un ApiHandler pour l'interface avec les API de streaming de musique
 */
public class Controller {

    private Browser browser;
    private Player player;
    private PlaylistHandler playlistHandler;
    private ApiHandler apiHandler;

    private Playlist currentPlaylist;

    /**
     * Constructeur
     * @param browser The View (web engine)
     */
    public Controller(Browser browser) {
        this.browser = browser;
        this.apiHandler = new ApiHandler();
        this.player = new Player(this);
        this.playlistHandler = new PlaylistHandler();
        System.out.println("Controller created");
    }

    /**
     * Recherche des pistes
     * @param searchEntry mot clé pour la recherche de pistes
     */
    public void searchTrack(String searchEntry){
        ArrayList<Track> tracks = apiHandler.searchTrack(searchEntry);
        if (!tracks.isEmpty())
            browser.getMainFrame().displaySearchResults(tracks);
    }

    /**
     * Déclenche la lecture d'une piste (clic sur bouton "play" d'une piste)
     * @param track piste à jouer
     */
    public void playTrack(Track track){
        if (player.getCurrentTrack() != track) {
            this.player.play(track);
            this.browser.getPlayerFrame().displayPause();
            this.browser.getPlayerFrame().displayTrackInfo(track);
        }
    }

    /**
     * Déclenche la lecture de la piste suivante (clic sur bouton "next")
     */
    public void playNext(){
        player.playNext();
    }

    /**
     * Déclenche la lecture de la piste précédente (clic sur bouton "previous")
     */
    public void playPrevious(){
        player.playPrevious();
    }

    /**
     * Action lors d'un clic sur le bouton "play/pause"
     */
    public void play_pauseClicked(){
        this.player.play_pause();
    }

    /**
     * Met à jour la Vue lorsqu'une piste est jouée
     */
    public void isPlaying(){
        this.browser.getPlayerFrame().displayPause();
    }

    /**
     * Met à jour la Vue lorsque la lecture est arrêtée
     */
    public void isStopped(){
        this.browser.getPlayerFrame().displayPlay();
        this.browser.getPlayerFrame().hideTrackInfo();
    }

    /**
     * Met à jour la Vue lorsqu'une nouvelle piste est jouée
     */
    public void isPlayingNewSong(){
        this.browser.getPlayerFrame().displayPause();
        this.browser.getPlayerFrame().displayTrackInfo(player.getCurrentTrack());
    }

    /**
     * Create a new playlist and display its name in the menu panel
     * @param title Playlist title chosen
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
     * Arrête les musiques en train de jouer
     */
    public void stopAllMusicPlaying(){
        player.stop();
    }

    /**
     * Fetch the playlist by its name and display the playlist in the main frame
     * @param name nom de la playlist
     */
    public void selectPlaylist(String name){
        Playlist playlist = getPlaylistHandler().getPlaylistByName(name);
        if(playlist != null) {
            this.currentPlaylist = playlist;
            this.browser.getMainFrame().displayPlaylist(playlist);
        }
    }

    /**
     * Supprime la playlist courante et met à jour la Vue
     */
    public void removeCurrentPlaylist() {
        this.getPlaylistHandler().deletePlaylist(currentPlaylist);
        currentPlaylist = null;
        this.browser.getMenuFrame().updatePlaylists();
        this.browser.getMainFrame().displaySearchField();
    }

    /**
     * Lance la lecture de la playlist courante
     */
    public void playCurrentPlaylist(){
        this.player.playPlaylist(currentPlaylist);
    }

    /**
     * Supprime une piste de la playlist courante et met à jour la Vue
     * @param trackId Id de la piste à supprimer
     */
    public void removeTrackFromCurrentPlaylist(String trackId){
        playlistHandler.removeTrackFromPlaylist(trackId, currentPlaylist.getName());
        this.browser.getMainFrame().displayPlaylist(currentPlaylist);
    }

    /**
     * Ajoute une piste à un playlist choisie par l'utilisateur
     * @param track piste à ajouter
     */
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

    /**
     * Déclenche l'affichage de l'onglet de recherche
     */
    public void browseClicked(){
        browser.getMainFrame().displaySearchField();
    }

    /**
     * Getter sur le PlaylistHandler
     * @return référence sur le PlaylistHandler
     */
    public PlaylistHandler getPlaylistHandler() {
        return playlistHandler;
    }

}
