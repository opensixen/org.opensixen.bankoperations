package org.opensixen.bankoperations.form;


import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.swing.CLabel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.jdesktop.swingx.JXTaskPane;
import org.opensixen.model.I_C_remittanceLine;
import org.opensixen.model.POFactory;
import org.opensixen.model.QParam;
import org.opensixen.model.X_C_remittanceLine;
import org.opensixen.utils.RemittanceMouseAdapter;
import org.opensixen.utils.RemittancePopup;

public class RemittanceSearch extends JPanel implements VetoableChangeListener,ListSelectionListener,ActionListener,TableModelListener {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Descripcion de campos
	 */
	int m_WindowNo=0;
	private MiniTable remittance  = new MiniTable();
	private JXTaskPane searchpane = new JXTaskPane();
	private RemittancePopup popup = new RemittancePopup();
	private MouseListener mouseListener = new RemittanceMouseAdapter( this );
	
	//Cliente de busqueda
	private CLabel lBPartner = new CLabel();
	private VLookup vBPartner;
	
	//Remesa de busqueda
	private CLabel lRemittance = new CLabel();
	private VLookup vRemittance;
	
	//Fecha generacion
	private CLabel lGenerateDate = new CLabel();
	private VDate vGenerateDate;
	
	//Factura de busqueda
	private CLabel lInvoice = new CLabel();
	private VLookup vInvoice;
	
	//Sql busqueda
	private String s_sqlWhere=" 1=1";
	private String m_sql="";
	
	//Variables globales
	private Hashtable<String,String> env = new Hashtable<String,String>();
	private boolean generate=false;
	protected CLogger log = CLogger.getCLogger(getClass());
	protected RemittanceFormPanel ParentPane =null;
	
	public RemittanceSearch(){
		initRemittanceSearch();
	}
	
	public RemittanceSearch(RemittanceFormPanel panel){
		ParentPane=panel;
		initRemittanceSearch();
	}
	
	private void initRemittanceSearch(){
		fillPicks();
		initComponents();
	}
	
	/**
	 * Inicializacion componentes
	 */
	private void initComponents(){
		initsearch();
		this.setLayout(new BorderLayout());
		this.add( searchpane,BorderLayout.NORTH);
		this.add(new JScrollPane(remittance),BorderLayout.CENTER);
		remittance.getModel().addTableModelListener(this);
		remittance.addMouseListener( mouseListener );
	}
	
	/**
	 * Inicializa el panel de busqueda
	 */
	private void initsearch(){
		searchpane.setTitle(Msg.translate(Env.getCtx(), "C_Remittance"));
		searchpane.setLayout(new GridBagLayout());
		//Columna,fila,?,?,withx,withy
		searchpane.add( lRemittance,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vRemittance,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lBPartner,new GridBagConstraints( 0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vBPartner,new GridBagConstraints( 1,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lGenerateDate,new GridBagConstraints( 2,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vGenerateDate,new GridBagConstraints( 3,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lInvoice,new GridBagConstraints( 0,2,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vInvoice,new GridBagConstraints( 1,2,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));

	}
	
	/**
	 * Inicializa los componentes de busqueda
	 */
	
	private void fillPicks(){
		
		//Cliente
		MLookup partnerL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2893, DisplayType.Search);
		vBPartner = new VLookup ("C_BPartner_ID", false, false, true, partnerL);
		lBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		vBPartner.addVetoableChangeListener(this);
		lBPartner.setLabelFor(vBPartner);
		
		//Factura
		MLookup invoiceL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 3484, DisplayType.Search);
		vInvoice = new VLookup ("C_Invoice_ID", false, false, true, invoiceL);
		lInvoice.setText(Msg.translate(Env.getCtx(), "C_Invoice_ID"));
		vInvoice.addVetoableChangeListener(this);
		lInvoice.setLabelFor(vInvoice);
		
		//Remesa
		MLookup remittanceL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0,150098, DisplayType.TableDir);
		vRemittance = new VLookup ("C_Remittance_ID", false, false, true, remittanceL);
		lRemittance.setText(Msg.translate(Env.getCtx(), "C_Remittance_ID"));
		vRemittance.addVetoableChangeListener(this);
		lRemittance.setLabelFor(vRemittance);
		
		//Fecha Generacion
		vGenerateDate = new VDate();
		lGenerateDate.setText(Msg.translate(Env.getCtx(), "GenerateDate"));
		lGenerateDate.setLabelFor(vGenerateDate);
		
	}

	/**
	 * Prepara la tabla y su definicion
	 */
	
	private void preparetable(){

		String s_sqlFrom="C_Remittance p";
		s_sqlFrom+=" INNER JOIN AD_Org g ON(g.AD_Org_ID=p.AD_Org_ID)";
		s_sqlFrom+=" INNER JOIN C_Bank b ON(p.c_bank_id=b.c_bank_id)";
		s_sqlFrom+=" INNER JOIN C_BankAccount bp ON(bp.c_bankaccount_id=p.c_bankaccount_id)";
		
		ColumnInfo[] s_layoutRemittance = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "C_Remittance_ID"), "C_Remittance_ID", IDColumn.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "AD_Org_ID"), "g.Name", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Documentno"), "Documentno", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "GenerateDate"), "GenerateDate", Date.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "ExecuteDate"), "ExecuteDate", Date.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "C_Bank_ID"), "b.Name", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "C_BankAccount_ID"), "bp.AccountNo", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "TotalAmt"), "TotalAmt", Double.class)};
		
		//Reseteamos el modelo de tabla
		remittance.setModel(new DefaultTableModel());
		
		String m_sqlRemittance = remittance.prepareTable(s_layoutRemittance, s_sqlFrom, s_sqlWhere, true, null);
		m_sql=m_sqlRemittance;
		remittance.setRowSelectionAllowed(true);
		remittance.getModel().addTableModelListener(this);
		remittance.getSelectionModel().addListSelectionListener(this);
        remittance.autoSize();
	}

	
	/**
	 * Ejecuta sentencia
	 */

	public void executeQuery(){
		
		generate=true;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(m_sql, null);
			rs = pstmt.executeQuery();
			remittance.loadTable(rs);
			
		}
		catch (Exception e)
		{
			//log.log(Level.WARNING, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		generate=false;
	}
	
	/**
	 * Actualiza valores de busqueda y tabla de resultados
	 */
	
	public void refreshWhere(){
		s_sqlWhere=" 1=1";
		Collection<String> valores=env.values();
		for(String val : valores) {
			s_sqlWhere+=val;
		}
		preparetable();
		executeQuery();
	}
	
	@Override
	public void vetoableChange(PropertyChangeEvent arg0)
			throws PropertyVetoException {
		
		if(arg0.getSource().equals(vBPartner)){
			env.remove("C_BPartner_ID");
			
			if(arg0.getNewValue()!=null)
				env.put("C_BPartner_ID", " AND p.C_BPartner_ID="+arg0.getNewValue());

		}
		else if(arg0.getSource().equals(vRemittance)){
			env.remove("C_Remittance_ID");
			
			if(arg0.getNewValue()!=null)
				env.put("C_Remittance_ID", " AND p.C_Remittance_ID="+arg0.getNewValue());

		}
		
		refreshWhere();
		resetPanelResults();
		refreshPanelResults();
	}



	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {

		 if (arg0.getSource().equals(remittance.getModel())&&arg0.getColumn()==0 && !generate)
			{
			 	//Reseteamos valores
			 	resetPanelResults();
			 	//Seteamos los valores de las lineas de la remesa
			 	setRemittanceLines(remittance.getValueAt(remittance.getSelectedRow(), arg0.getColumn()));
			 	//Refrescamos la consulta y el panel
			 	refreshPanelResults();
			}
		
	}
	
    public void mouseClicked( MouseEvent e ) {

        if( e.getSource() instanceof MiniTable ) {
            if( SwingUtilities.isRightMouseButton( e )) {
            	popup.show( remittance,e.getX(),e.getY());
                
            }
        }    // JButton
    }        // mouseClicked
	
	/**
	 * Recoge los registros asociados a la remesa escogida
	 * @param id
	 */
	
	public void setRemittanceLines(Object id){
		
		IDColumn remittance_id=(IDColumn)id;
		//Lista de objetos de lineas de remesa
		ArrayList<X_C_remittanceLine> rems = (ArrayList<X_C_remittanceLine>) POFactory.getList(Env.getCtx(),X_C_remittanceLine.class, new QParam(I_C_remittanceLine.COLUMNNAME_C_Remittance_ID,remittance_id.getRecord_ID()));
		for(X_C_remittanceLine rl : rems){
			ParentPane.getPanelResults().list.add(rl.getC_InvoicePaySchedule_ID());
		}
		
	}
	
	/**
	 * Refresca el panel de resultados
	 */
	
	private void refreshPanelResults(){
		ParentPane.getPanelResults().refreshWhere();
	}
	
	/**
	 * Resetea el panel de resultados
	 */
	
	private void resetPanelResults(){
		ParentPane.getPanelResults().resetSearch();
	}
	
}
