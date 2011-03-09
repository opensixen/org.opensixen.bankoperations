 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Nexis Servicios Informáticos S.L. - http://www.nexis.es
 *
 * Contribuyente(s):
 *  Alejandro González <alejandro@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */
package org.opensixen.bankoperations.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.process.ProcessInfo;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;

/**
 * 
 * RemittanceFormPanel 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class RemittanceFormPanel extends JPanel implements FormPanel,ActionListener,VetoableChangeListener,ChangeListener,TableModelListener,ASyncProcess {


	private static final long serialVersionUID = 1L;

	public RemittanceFormPanel() {
		// TODO Auto-generated constructor stub
		//Bug OsGi, pierde los defaults del look and feel instalado
		UIManager.getLookAndFeelDefaults().put("ClassLoader", UIManager.getLookAndFeel().getClass().getClassLoader());

	}
	
    /** Descripción de Campos */
	
    private int m_WindowNo = 0;
    private FormFrame m_frame;
    private static CLogger log = CLogger.getCLogger( RemittanceFormPanel.class );
    
    /**
     * Descripción de paneles
     */
    private JSplitPane splitp = new JSplitPane();
    private JSplitPane splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    
    private RemittanceResultsSearch results;
    private RemittanceResultsSelected resultsSelected;
    
    /**
     * Descripción de Método
     *
     *
     * @throws Exception
     */
    
	public void init(int WindowNo, FormFrame frame) {
		// TODO Auto-generated method stub
		m_WindowNo = WindowNo;
	    m_frame    = frame;

	    try {
	        	
	    	fillPicks();
	    	jbInit();
	    	dynInit();
	    	frame.getContentPane().add( splitp,BorderLayout.CENTER );
	    	frame.pack();
	    } catch( Exception ex ) {
	    	log.log( Level.SEVERE,"init",ex );
	    }
	}
	

	/**
	 * Retorna el panel de resultados de posibles facturas
	 * @return 
	 */
	
	protected RemittanceResultsSearch getPanelResults(){
		return results;
	}

    void jbInit() throws Exception {
    	//Panel principal
 
    	splitp.setOneTouchExpandable(true);
    	splitRight.setOneTouchExpandable(true);
    	
    	//Panel registros
    	results= new RemittanceResultsSearch(this);
    	resultsSelected= new RemittanceResultsSelected(this);

    	//Panel remesas
    	RemittanceSearch search = new RemittanceSearch(this);
    	
    	//Panel derecho
    	splitRight.setRightComponent(search);
    	splitRight.setLeftComponent(resultsSelected);

    	splitp.setRightComponent(splitRight);
    	splitp.setLeftComponent(results);
    	
    	//Dimensiones de los split
    	Dimension minimumSize = new Dimension(0, 0);
    	splitRight.getRightComponent().setMinimumSize(minimumSize);
    	splitRight.getLeftComponent().setMinimumSize(minimumSize);

    	splitp.getLeftComponent().setMinimumSize(new Dimension(450, 0));
    	splitRight.setDividerLocation(0.99);
    	splitRight.setResizeWeight(1.0);


    }    // jbInit

    /**
     * Descripción de Método
     *
     *
     * @throws Exception
     */
   
    private void fillPicks() throws Exception {
       
    }    // fillPicks

    /**
     * Descripción de Método
     *
     */

    private void dynInit() {

    }    // dynInit

    
    /**
     * Descripción de Método
     *
     */

    public void dispose() {
        if( m_frame != null ) {
            m_frame.dispose();
        }

        m_frame = null;
    }    // dispose

 
    /**
     * Descripción de Método
     *
     *
     * @return
     */

    public boolean isUILocked() {
        return this.isEnabled();
    }    // isUILocked

    /**
     * Descripción de Método
     *
     *
     * @param pi
     */


	@Override
	public void vetoableChange(PropertyChangeEvent arg0)
			throws PropertyVetoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeASync(ProcessInfo pi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lockUI(ProcessInfo pi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlockUI(ProcessInfo pi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
