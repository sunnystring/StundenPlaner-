/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Mathias
 */
public interface ValidTimeListener {
    

    public void validTimeSelected(StudentDay studentDay);
    
    public void validTimeDeselected();
    
    public void studentSelected(StudentDay studentDay);

}
