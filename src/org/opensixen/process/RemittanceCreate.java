package org.opensixen.process;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.compiere.apps.ADialog;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.opensixen.bankoperations.form.RemittanceFormPanel;
import org.opensixen.bankoperations.form.RemittanceParams;
import org.opensixen.model.MRemittance;
import org.opensixen.model.X_C_Remittance;

public class RemittanceCreate {
	
	/**
	 * Descripcion de campos
	 */
	private ArrayList<Integer> invoicelist=null;
	private RemittanceParams params=null;
	private String TrxName =null;
    private static CLogger log = CLogger.getCLogger( RemittanceCreate.class );
	/**
	 * Creamos Remesa en base de datos a partir de Lineas de factura
	 */
	
	
	public RemittanceCreate(ArrayList<Integer> list){
		invoicelist=list;
	}
	
	/**
	 * Constructor por defecto
	 */
	
	public RemittanceCreate(){}
	
	private boolean save(final MRemittance rem) {
		try
        {
                Trx.run(new TrxRunnable() 
                {
                        public void run(String trxName)
                        {
                        	rem.save(trxName);
                        }
                });
        }
        catch (Exception e)
        {
               log.severe("Error saving remittance,"+e);
               return false;
        }
        return true;
		
		
	} // saveChanges
	
	/**
	 * Creamos la remesa
	 * @return
	 */
	public boolean Create(){
		if(!checkValues()){
			return false;
		}

		MRemittance remit = new MRemittance(Env.getCtx(),0,TrxName);
		//Norma
		remit.setC_BankRegulation_ID((Integer)params.getBankRegulation());
		//Cuenta bancaria
		remit.setC_Bank_ID((Integer)params.getBank());
		remit.setC_BankAccount_ID((Integer)params.getBankAccount());
		if(!save(remit))
			return false;
		
		return CreateLines();
	}

	public boolean CreateLines() {

		if(invoicelist.size()==0){
			return false;
		}
		return true;
	}
	
	/**
	 * Setea el panel de parametros necesario
	 * @param panelParams
	 */
	
	public void setPanelValues(RemittanceParams panelParams) {
		params=panelParams;
		
	}
	
	/**
	 * Comprueba que existan los valores necesarios para la creacion de la remesa
	 */
	
	private boolean checkValues(){
		
		if(params==null){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Null"), Msg.translate(Env.getCtx(), "Null"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(params.getBank()==null){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Null Bank"), Msg.translate(Env.getCtx(), "C_Bank"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(params.getBankAccount()==null){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Null BankAccount"), Msg.translate(Env.getCtx(), "C_BankAccount"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(params.getSendDate()==null){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Null SendDate"), Msg.translate(Env.getCtx(), "SendDate"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(params.getExecuteDate()==null){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Null ExecuteDate"), Msg.translate(Env.getCtx(), "ExecuteDate"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(params.getBankRegulation()==null){
			JOptionPane.showMessageDialog(null, Msg.translate(Env.getCtx(), "Null BankRegulation"), Msg.translate(Env.getCtx(), "C_BankRegulation"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}



}
