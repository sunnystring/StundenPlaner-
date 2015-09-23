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
    
    
    public void studentAdded(Student s);
    
    public void studentRemoved(Student s);
    
    public void studentEdited(Student s);
    
    public void scheduleAdded(TeacherTimes t);
    
    public void scheduleRemoved(TeacherTimes t);
    
    public void scheduleEdited(TeacherTimes t);
    
    
    
}
