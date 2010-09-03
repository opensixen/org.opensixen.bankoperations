package org.opensixen.bankoperations.form;

import java.awt.BorderLayout;
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
import org.opensixen.osgi.interfaces.IFormPanel;

public class RemittanceFormPanel extends JPanel implements FormPanel,ActionListener,VetoableChangeListener,ChangeListener,TableModelListener,ASyncProcess,IFormPanel {


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
    private JSplitPane splitleft = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    
    private RemittanceParams params;
    private RemittanceResults results;
    
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
	 * Retorna el panel de parametros
	 * @return 
	 */
	
	protected RemittanceParams getPanelParams(){
		return params;
	}
	
	
	protected RemittanceResults getPanelResults(){
		return results;
	}

    void jbInit() throws Exception {
    	//Panel principal
    	splitp.setDividerLocation(.75);
    	splitp.setOneTouchExpandable(true);
    	//splitp.setDividerSize(arg0)
    	//Panel norma
    	params= new RemittanceParams(); 	
    	
    	//Panel registros
    	results= new RemittanceResults(this);
    	splitp.setRightComponent(results);
    	
    	//Panel remesas
    	RemittanceSearch search = new RemittanceSearch(this);
    	
    	//Panel izquierdo
    	splitleft.setLeftComponent(params);
    	splitleft.setRightComponent(search);
    	
    	splitp.setLeftComponent(splitleft);

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

    private void executeQuery() {

    }    // executeQuery

    
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
     * @param e
     */

    
    public void remesagenerate_process(){
/*
       int        AD_Process_ID = proceso;    // HARDCODED    C_RemesaGenerate
       MPInstance instance      = new MPInstance( Env.getCtx(),AD_Process_ID,0,null );

       if( !instance.save()) {
           info.setText( Msg.getMsg( Env.getCtx(),"ProcessNoInstance" ));

           return;
       }

       ProcessInfo pi = new ProcessInfo( "",AD_Process_ID );

       pi.setAD_PInstance_ID( instance.getAD_PInstance_ID());

       // Add Parameters

       MPInstancePara para = new MPInstancePara( instance,10 );

       para.setParameter( "Selection","Y" );

       if( !para.save()) {
           String msg = "No Selection Parameter added";    // not translated

           info.setText( msg );
           log.log( Level.SEVERE,msg );

           return;
       }

       para = new MPInstancePara( instance,20 );
       para.setParameter( "DocAction","CO" );

       if( !para.save()) {
           String msg = "No DocAction Parameter added";    // not translated

           info.setText( msg );
           log.log( Level.SEVERE,msg );

           return;
       }

       // Execute Process

       ProcessCtl worker = new ProcessCtl( this,pi,null );

       worker.start();  
       */
   }
     
    
 
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
