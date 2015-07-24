/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import stundenplan.LectionField;

/**
 *
 * @author Mathias
 */
public interface ValidTimeListener {
    

    public void validTimeSelected(LectionField lectionField, SchuelerDay day);
    
    public void validTimeDeselected();
    
    public void schuelerSelected(SchuelerDay schuelerDay);

}
