/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentlist;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class NameField extends StudentField {

    private String name, firstName;

    public NameField(String firstName, String name) {

        this.firstName = firstName;
        this.name = name;
        super.setFirstName(firstName);
        super.setName(name);

        setText(firstName + " " + name);

        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 3));
        setBackground(Colors.NAME_FIELD);

    }

}
