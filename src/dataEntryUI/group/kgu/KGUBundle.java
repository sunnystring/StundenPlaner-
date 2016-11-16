/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataEntryUI.group.kgu;

import java.util.ArrayList;

/**
 *
 * @author mathiaskielholz
 */
public class KGUBundle {

    private ArrayList<KGUDay> members;
    private ArrayList<Integer> memberIDs;
    private int maximalNumberOfMembers;

    public KGUBundle(int maximalNumberOfMembers) {
        this.maximalNumberOfMembers = maximalNumberOfMembers;
        members = new ArrayList<>();
        memberIDs = new ArrayList<>();
    }

    public boolean excludes(KGUDay mergedDays) {
        ArrayList<Integer> mergedTimesIDs = mergedDays.getMemberIDs();
        for (int i = 0; i < mergedTimesIDs.size(); i++) {
            for (int j = 0; j < memberIDs.size(); j++) {
                if (mergedTimesIDs.get(i).equals(memberIDs.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addGroup(KGUDay kguDay) {
        members.add(kguDay);
    }

    public boolean isComplete() {
        return memberIDs.size() == maximalNumberOfMembers;
    }

    public ArrayList<KGUDay> getMembers() {
        return members;
    }

    public void addMemberIDs(ArrayList<Integer> IDs) {
        memberIDs.addAll(IDs);
    }

    public ArrayList<Integer> getMemberIDs() {
        return memberIDs;
    }
}
