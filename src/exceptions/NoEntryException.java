/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author mathiaskielholz
 */
public class NoEntryException extends RuntimeException{

    public NoEntryException() {
    }

    public NoEntryException(String message) {
        super(message);
    }

    public NoEntryException(Throwable cause) {
        super(cause);
    }

    public NoEntryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
