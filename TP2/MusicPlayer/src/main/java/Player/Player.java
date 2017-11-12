package Player;

import Handler.ApiHandler;
import Model.Track;

import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private ApiHandler apiHandler;
    private Stack<Track> previous;
    private Stack<Track> next;

    public Player(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
        this.previous = new Stack<Track>();
        this.next = new Stack<Track>();
    }

    //Plays a track
    public void play(Track track){

    }

    //Pause a track
    public void pause(Track track){

    }

    public void addToQueue(ArrayList<Track> tracks){
        for(Track t : tracks){
            next.push(t);
        }
    }

    public void clearQueue(){
        previous.clear();
        next.clear();
    }

    public void playNext(){
        if(next.isEmpty()){
            System.out.println("No next song to play");
        }else{
            Track track = next.pop();
            previous.push(track);
            play(track);
            System.out.println("Play next song");
        }

    }

    public void playPrevious(){
        if(previous.isEmpty()){
            System.out.println("No previous song to play");
        }
        else{
            Track track = previous.pop();
            next.push(track);
            play(track);
            System.out.println("Play previous song");
        }

    }


}
