package Player;

import Handler.ApiHandler;
import Model.Track;

import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private ApiHandler apiHandler;
    private Stack<Track> queue;

    public Player(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
        this.queue = new Stack<Track>();
    }

    //Plays a track
    public void play(Track track){

    }

    //Pause a track
    public void pause(Track track){

    }

    public void addToQueue(ArrayList<Track> tracks){
        for(Track t : tracks){
            queue.push(t);
        }
    }

    public void clearQueue(){
        queue.clear();
    }

    public void playNext(){
        play(queue.pop());
    }


}
