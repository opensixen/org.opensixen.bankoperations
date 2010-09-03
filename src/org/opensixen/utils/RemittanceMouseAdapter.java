package org.opensixen.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.opensixen.bankoperations.form.RemittanceSearch;

public class RemittanceMouseAdapter extends MouseAdapter {
    /** Descripción de Campos */

    RemittanceSearch adaptee;

    public RemittanceMouseAdapter( RemittanceSearch adaptee ) {
        this.adaptee = adaptee;
    }


    public void mouseClicked( MouseEvent e ) {
        adaptee.mouseClicked( e );
    }
}
