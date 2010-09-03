package org.opensixen.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

import org.compiere.model.I_C_DocType;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

public class MRemittance extends X_C_Remittance implements DocAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Variables globales
	 */
	
	private MRemittanceLine[]	m_lines	= null;
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Remittance_ID id
	 *	@param trxName trx
	 */
	
	public MRemittance(Properties ctx, int C_Remittance_ID, String trxName) {
		super(ctx, C_Remittance_ID, trxName);
		//Recogemos la fecha actual
		Timestamp today=new Timestamp(TimeUtil.getToday().getTimeInMillis());
		//Escogemos el tipo de documento remesa
		
		QParam param = new
		QParam(I_C_DocType.COLUMNNAME_DocBaseType+"='"+org.opensixen.model.X_C_DocType.DOCBASETYPE_Remittance+"' AND "+I_C_DocType.COLUMNNAME_AD_Client_ID+"="+Env.getAD_Client_ID(Env.getCtx()));
		
		org.opensixen.model.X_C_DocType doctype =
		POFactory.get(org.opensixen.model.X_C_DocType.class, param);

		
		this.setTotalAmt(BigDecimal.ZERO);
		this.setGenerateDate(today);
		this.setExecuteDate(today);
		this.setC_DocTypeTarget_ID(doctype.getC_DocType_ID());
		this.setC_DocType_ID(0);
	}
	
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	
	public MRemittance (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInvoiceBatch

	
	/**
	 * 	Get Lines
	 *	@param reload data
	 *	@return array of lines
	 */
	public MRemittanceLine[] getLines (boolean reload)
	{
		if (m_lines != null && !reload) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		
		//Lista de objetos de lineas de remesa
		ArrayList<MRemittanceLine> rems = (ArrayList<MRemittanceLine>) 
			POFactory.getList(Env.getCtx(),MRemittanceLine.class, new QParam(I_C_remittanceLine.COLUMNNAME_C_Remittance_ID,getC_Remittance_ID()));

		m_lines = new MRemittanceLine[rems.size ()];
		rems.toArray (m_lines);
		return m_lines;
	}	//	getLines

	
	/**
	 * 	Set Processed
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		
	}	//	setProcessed


	@Override
	public void setDocStatus(String newStatus) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getDocStatus() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean processIt(String action) throws Exception {
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (action, getDocAction());
	}


	@Override
	public boolean unlockIt() {
		setProcessing(false);
		return true;
	}


	@Override
	public boolean invalidateIt() {
		//setDocAction(DocAction.ACTION_Prepare);
		return true;
	}


	@Override
	public String prepareIt() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean approveIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String completeIt() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean voidIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean closeIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean reActivateIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getProcessMsg() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getDoc_User_ID() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getDocAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
