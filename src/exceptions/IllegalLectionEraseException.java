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
public class IllegalLectionEraseException extends RuntimeException {

    public IllegalLectionEraseException() {
    }

    public IllegalLectionEraseException(String string) {
        super(string);
    }

    public IllegalLectionEraseException(Throwable thrwbl) {
        super(thrwbl);
    }

    public IllegalLectionEraseException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
