package org.opensixen.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MRemittanceLine extends X_C_remittanceLine {


	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RemittanceLine_ID id
	 *	@param trxName trx
	 */
	
	public MRemittanceLine(Properties ctx, int C_remittanceLine_ID,
			String trxName) {
		super(ctx, C_remittanceLine_ID, trxName);

	}
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	
	public MRemittanceLine (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInvoiceBatch

}
