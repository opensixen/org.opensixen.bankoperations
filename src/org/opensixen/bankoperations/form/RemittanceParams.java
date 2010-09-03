package org.opensixen.bankoperations.form;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.Timestamp;

import javax.swing.JPanel;

import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.swing.CLabel;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class RemittanceParams extends JPanel implements VetoableChangeListener {

	private static final long serialVersionUID = 1L;


	/**
	 * Descripción de variables globales
	 */
	
	int m_WindowNo=0;
	
	
	/**
	 * Descripción de swings
	 */
	
	//Norma
	private CLabel lBankRegulation = new CLabel();
	private VLookup vBankRegulation;
	
	//Banco propio
	private CLabel lBank = new CLabel();
	private VLookup vBank;
	
	//Cuenta Bancaria
	private CLabel lBankAccount = new CLabel();
	private VLookup vBankAccount;
	
	//Fecha Envio
	private CLabel lSendDate = new CLabel();
	private VDate vSendDate = new VDate();
	
	//Fecha Cargo
	private CLabel lExecDate = new CLabel();
	private VDate vExecDate = new VDate();
	
	//Tipo de Operacion
	private CLabel lOpType = new CLabel();
	private VLookup vOpType;
	
	
	public RemittanceParams(){
		initComponents();
	}
	
	private void initComponents(){
		fillPicks();
		this.setLayout(new GridBagLayout());
        //Columna,fila,?,?,withx,withy
		this.add( lBankRegulation,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vBankRegulation,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));

		this.add( lBank,new GridBagConstraints( 0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vBank,new GridBagConstraints( 1,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		this.add( lBankAccount,new GridBagConstraints( 0,2,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vBankAccount,new GridBagConstraints( 1,2,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
        this.add( lSendDate,new GridBagConstraints( 0,3,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vSendDate,new GridBagConstraints( 1,3,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
        this.add( lExecDate,new GridBagConstraints( 2,3,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
        this.add( vExecDate,new GridBagConstraints( 3,3,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));


	}
	
	private void fillPicks(){
		
		//Normas
		MLookup bankRegulationL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 150086, DisplayType.TableDir);
		vBankRegulation = new VLookup ("C_BankRegulation_ID", true, false, true, bankRegulationL);
		lBankRegulation.setText(Msg.translate(Env.getCtx(), "C_BankRegulation_ID"));
		vBankRegulation.addVetoableChangeListener(this);
		lBankRegulation.setLabelFor(vBankRegulation);
		
		//Banco propio
		MLookup bankL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 3031, DisplayType.TableDir);
		vBank = new VLookup ("C_Bank_ID", true, false, true, bankL);
		lBank.setText(Msg.translate(Env.getCtx(), "C_Bank_ID"));
		vBank.addVetoableChangeListener(this);
		lBank.setLabelFor(vBank);
		
		//Cuenta Bancaria
		MLookup bankAccountL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 3077, DisplayType.TableDir);
		vBankAccount = new VLookup ("C_BankAccount_ID", true, false, true, bankAccountL);
		lBankAccount.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
		vBankAccount.addVetoableChangeListener(this);
		lBankAccount.setLabelFor(vBankAccount);
		
		//Fecha de Envio
		lSendDate.setText(Msg.translate(Env.getCtx(), "GenerateDate"));
		lSendDate.setLabelFor(vSendDate);
		
		//Fecha de Cargo
		lExecDate.setText(Msg.translate(Env.getCtx(), "ExecuteDate"));
		lExecDate.setLabelFor(vExecDate);
		
	}

	@Override
	public void vetoableChange(PropertyChangeEvent arg0)
			throws PropertyVetoException {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 
	 * @return Fecha de Envio
	 */
	
	public Timestamp getSendDate(){
		return vSendDate.getTimestamp();
	}
	
	/**
	 * 
	 * @return Fecha de Ejecucion
	 */
	
	public Timestamp getExecuteDate(){
		return vExecDate.getTimestamp();
	}
	
	/**
	 * 
	 * @return Cuenta Bancaria
	 */
	
	public Object getBankAccount(){
		return vBankAccount.getValue();
	}
	
	/**
	 * 
	 * @return Banco propio
	 */
	
	public Object getBank(){
		return vBank.getValue();
	}
	
	/**
	 * 
	 * @return Norma Bancaria
	 */
	
	public Object getBankRegulation(){
		return vBankRegulation.getValue();
	}
}
