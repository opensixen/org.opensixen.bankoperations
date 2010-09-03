package org.opensixen.utils;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.compiere.util.Env;
import org.compiere.util.Msg;

public class RemittancePopup extends JPopupMenu {


	private static final long serialVersionUID = 1L;
	
	/**
	 * Elementos del menu
	 */
	private JMenuItem mFile = new JMenuItem(Msg.translate(Env.getCtx(), "RemittanceFile"));
    private JMenuItem mEdit = new JMenuItem(Msg.translate(Env.getCtx(), "RemittanceEdit"));
    private JMenuItem mCancel = new JMenuItem(Msg.translate(Env.getCtx(), "RemittanceCancel"));
    private JMenuItem mDelete = new JMenuItem(Msg.translate(Env.getCtx(), "RemittanceDelete"));
	
    /**
     * Constructor estandar
     */
    
	public RemittancePopup(){
		super();
		this.setLightWeightPopupEnabled( false );
		this.add(mFile);
		this.add(mEdit);
		this.add(mCancel);
		this.add(mDelete);
	}
	
	/**
	 * Constructor diferenciador del estado de la remesa para diferentes acciones
	 * @param status
	 */
	public RemittancePopup(String status){
		super();
		this.setLightWeightPopupEnabled( false );
		
		this.add(mFile);
		this.add(mEdit);
		this.add(mCancel);
		this.add(mDelete);
	}
	
	
	/**
	 * 
	 * @return Menu crear fichero
	 */
	public JMenuItem getmFile(){
		return mFile;
	}
	
	/**
	 * 
	 * @return Menu Editar Remesa
	 */
	
	public JMenuItem getmEdit(){
		return mEdit;
	}
	
	/**
	 * 
	 * @return Menu Cancelar Remesa
	 */
	
	public JMenuItem getmCancel(){
		return mCancel;
	}
	
	/**
	 * 
	 * @return Menu Borrar Remesa
	 */
	
	public JMenuItem getmDelete(){
		return mDelete;
	}
	

	
	
}
