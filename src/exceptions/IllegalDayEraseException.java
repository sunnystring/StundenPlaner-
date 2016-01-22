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
public class IllegalDayEraseException extends RuntimeException {

    public IllegalDayEraseException() {
    }

    public IllegalDayEraseException(String string) {
        super(string);
    }

    public IllegalDayEraseException(Throwable thrwbl) {
        super(thrwbl);
    }

    public IllegalDayEraseException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
