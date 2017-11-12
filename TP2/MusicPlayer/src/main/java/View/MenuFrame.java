package View;

import Controller.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MenuFrame extends Frame {

    public MenuFrame(Document doc, Controller c){
        super(doc, c);
    }

    public void addPlaylist(String title){
        Element playlists = DOM.getElementById("playlists");
        Element p = DOM.createElement("li");
        Element button = DOM.createElement("button");
        button.setTextContent(title);
        button.setAttribute("id", title);
        p.appendChild(button);

        playlists.appendChild(p);
    }
}
