/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleData;

import java.util.ArrayList;

/**
 *
 * @author Mathias
 */
public class LectionData {

    //   private ScheduleFieldData scheduleFieldData;
    private ArrayList<ScheduleFieldData> lection;

    public LectionData() {
        lection = new ArrayList<>();
    }

    public void add(ScheduleFieldData field) {
 //       System.out.println("field:" + field.getTime());
        lection.add(field);
    }

}
