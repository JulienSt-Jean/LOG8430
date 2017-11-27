package Client.View;

import org.w3c.dom.Document;
import Client.Controller.Controller;

public class Frame {

    protected Document DOM;
    protected Controller controller;

    public Frame(Document doc, Controller c){
        DOM = doc;
        controller = c;
    }
}
