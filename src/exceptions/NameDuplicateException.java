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
public class NameDuplicateException extends RuntimeException {

    public NameDuplicateException() {
    }

    public NameDuplicateException(String message) {
        super(message);
    }

    public NameDuplicateException(Throwable cause) {
        super(cause);
    }

    public NameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
