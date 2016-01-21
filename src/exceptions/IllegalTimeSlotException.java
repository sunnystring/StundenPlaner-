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
public class IllegalTimeSlotException extends RuntimeException{

    public IllegalTimeSlotException() {
    }

    public IllegalTimeSlotException(String message) {
        super(message);
    }

    public IllegalTimeSlotException(Throwable cause) {
        super(cause);
    }

    public IllegalTimeSlotException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
