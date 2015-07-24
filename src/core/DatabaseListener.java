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
public interface DatabaseListener {
    
    
    public void schuelerAdded(Schueler s);
    
    public void schuelerRemoved(Schueler s);
    
    public void schuelerEdited(Schueler s);
    
    public void dayAdded(StundenplanDay d);
    
    public void dayRemoved(StundenplanDay d);
    
    public void dayEdited(StundenplanDay d);
    
    
    
}
