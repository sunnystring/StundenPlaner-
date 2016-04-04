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
public class OutOfBoundException extends RuntimeException{

    public OutOfBoundException() {
    }

    public OutOfBoundException(String message) {
        super(message);
    }

    public OutOfBoundException(Throwable cause) {
        super(cause);
    }

    public OutOfBoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
