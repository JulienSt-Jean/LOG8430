package View;

import org.w3c.dom.Document;
import Controller.Controller;

/**
 * Classe abstraite représentant un Frame HTML. Un tel frame constitue une partie de la Vue
 */
public class Frame {

    protected Document DOM;
    protected Controller controller;

    /**
     * Constructeur
     * @param doc DOM du frame HTML
     * @param c référence sur le Contrôleur de l'application
     */
    public Frame(Document doc, Controller c){
        DOM = doc;
        controller = c;
    }
}
