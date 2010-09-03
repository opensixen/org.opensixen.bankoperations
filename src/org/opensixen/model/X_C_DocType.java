package org.opensixen.model;

import java.sql.ResultSet;
import java.util.Properties;

public class X_C_DocType extends org.compiere.model.X_C_DocType {


	private static final long serialVersionUID = 1L;
	
	public X_C_DocType(Properties ctx, int C_DocType_ID, String trxName) {
		super(ctx, C_DocType_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	
    /** Load Constructor */
    public X_C_DocType (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }
    
	/** GL Journal = GLJ */
	public static final String DOCBASETYPE_Remittance = "REM";
}
