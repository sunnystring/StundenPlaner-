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
public class ScheduleOutOfBoundException extends RuntimeException{

    public ScheduleOutOfBoundException() {
    }

    public ScheduleOutOfBoundException(String message) {
        super(message);
    }

    public ScheduleOutOfBoundException(Throwable cause) {
        super(cause);
    }

    public ScheduleOutOfBoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
