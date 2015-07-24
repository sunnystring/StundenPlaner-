/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schuelerliste;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import util.Colors;

/**
 *
 * @author Mathias
 */
public class NameField extends SchuelerField {

    private String name, vorname;

    public NameField(String vorname, String name) {

        this.vorname = vorname;
        this.name = name;
        super.setVorname(vorname);
        super.setName(name);

        setText(vorname + " " + name);

        setHorizontalAlignment(SwingConstants.LEADING);
        setFont(this.getFont().deriveFont(Font.PLAIN, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 3));
        setBackground(Colors.NAME_FIELD);

    }

}
