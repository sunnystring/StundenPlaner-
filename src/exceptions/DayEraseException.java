/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Mathias
 */
public class DayEraseException extends RuntimeException {

    public DayEraseException() {
    }

    public DayEraseException(String string) {
        super(string);
    }

    public DayEraseException(Throwable thrwbl) {
        super(thrwbl);
    }

    public DayEraseException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
