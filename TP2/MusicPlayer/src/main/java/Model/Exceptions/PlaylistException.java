package Model.Exceptions;

import java.security.spec.ECFieldF2m;

/**
 * Exception pouvant être déclenchée par une playlist
 */
public class PlaylistException extends Exception{
    public PlaylistException(String message) {
        super(message);
    }
}
