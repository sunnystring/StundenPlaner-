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
public class IllegalDayEntryException extends RuntimeException{

    public IllegalDayEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDayEntryException(Throwable cause) {
        super(cause);
    }

    public IllegalDayEntryException(String message) {
        super(message);
    }

    public IllegalDayEntryException() {
    }
    
}
