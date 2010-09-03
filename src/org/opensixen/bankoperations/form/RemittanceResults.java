package org.opensixen.bankoperations.form;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.compiere.apps.ConfirmPanel;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.jdesktop.swingx.JXTaskPane;
import org.opensixen.process.RemittanceCreate;

public class RemittanceResults extends JPanel implements VetoableChangeListener,ListSelectionListener,ActionListener,TableModelListener {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Descripcion de campos
	 */

	//Paneles y tablas
	private MiniTable remittance  = new MiniTable();
	private MiniTable remittanceselect  = new MiniTable();
	private JXTaskPane searchpane = new JXTaskPane();
	private JXTaskPane remittanceselectpanel = new JXTaskPane();
	private ConfirmPanel confirm =  new ConfirmPanel(true);
	private CPanel minitablepanel = new CPanel();
	
	//Organizacion de busqueda
	private CLabel lOrg = new CLabel();
	private VLookup vOrg;
	
	//Cliente de busqueda
	private CLabel lBPartner = new CLabel();
	private VLookup vBPartner;
	
	//Fecha facturacion/cobro desde
	private CLabel lFromInvoiced = new CLabel();
	private VDate vFromInvoiced;
	
	//Fecha facturacion/cobro hasta
	private CLabel lToInvoiced = new CLabel();
	private VDate vToInvoiced;
	
	//Sql busqueda
	private String s_sqlWhere=" 1=1";
	private String s_sqlWhereSelected=" 1=1";
	private String m_sql="";
	private String m_sqlSelected="";
	
	//Variables globales
	Hashtable<String,String> env = new Hashtable<String,String>();
	ArrayList<Integer> list = new ArrayList<Integer>();
	private boolean generate=false;
	int m_WindowNo=0;
	protected RemittanceFormPanel ParentPane =null;
	protected CLogger log = CLogger.getCLogger(getClass());

	
	public RemittanceResults(){
		fillPicks();
		initComponents();
	}
	
	/**
	 * Constructor con panel padre
	 */
	
	public RemittanceResults(RemittanceFormPanel panel){
		ParentPane=panel;
		fillPicks();
		initComponents();
	}
	
	/**
	 * Inicializacion componentes
	 */
	
	private void initComponents(){
		initsearch();
		minitablepanel.setLayout(new BorderLayout());
		remittanceselectpanel.add(new JScrollPane(remittanceselect));
		remittanceselectpanel.setTitle(Msg.translate(Env.getCtx(), "RemittanceSelect"));
		minitablepanel.add(remittanceselectpanel,BorderLayout.NORTH);
		
		minitablepanel.add(new JScrollPane(remittance),BorderLayout.CENTER);

		confirm.addActionListener(this);
		//Dimensionado hardcoded
		remittanceselect.setPreferredScrollableViewportSize(new Dimension(1,200));


		this.setLayout(new BorderLayout());
		this.add( searchpane,BorderLayout.NORTH);
		this.add(minitablepanel,BorderLayout.CENTER);
		this.add(confirm,BorderLayout.SOUTH);
	}
	
	/**
	 * Inicializa el panel de busqueda
	 */
	
	private void initsearch(){
		searchpane.setTitle(Msg.translate(Env.getCtx(), "Search"));
		searchpane.setLayout(new GridBagLayout());
		//Columna,fila,?,?,withx,withy
		searchpane.add( lOrg,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vOrg,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lBPartner,new GridBagConstraints( 2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vBPartner,new GridBagConstraints( 3,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lFromInvoiced,new GridBagConstraints( 0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vFromInvoiced,new GridBagConstraints( 1,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lToInvoiced,new GridBagConstraints( 2,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vToInvoiced,new GridBagConstraints( 3,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));

	}
	
	/**
	 * Inicializa los componentes de busqueda
	 */
	
	private void fillPicks(){
		//Organizacion
		MLookup orgL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2163, DisplayType.TableDir);
		vOrg = new VLookup ("AD_Org_ID", false, false, true, orgL);
		lOrg.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		vOrg.addVetoableChangeListener(this);
		lOrg.setLabelFor(vOrg);
		
		//Cliente
		MLookup partnerL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2893, DisplayType.Search);
		vBPartner = new VLookup ("C_BPartner_ID", false, false, true, partnerL);
		lBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		vBPartner.addVetoableChangeListener(this);
		lBPartner.setLabelFor(vBPartner);
		
		//Fecha facturacion desde
		vFromInvoiced = new VDate();
		lFromInvoiced.setText(Msg.translate(Env.getCtx(), "FromDateInvoiced"));
		lFromInvoiced.setLabelFor(vFromInvoiced);
		
		//Fecha facturacion hasta
		vToInvoiced = new VDate();
		lToInvoiced.setText(Msg.translate(Env.getCtx(), "ToDateInvoiced"));
		lToInvoiced.setLabelFor(vToInvoiced);
	}

	/**
	 * Prepara la tabla y su definicion
	 */
	
	private void preparetable(){

		String s_sqlFrom="C_InvoicePaySchedule p";
		s_sqlFrom+=" INNER JOIN AD_Org g ON(g.AD_Org_ID=p.AD_Org_ID)";
		s_sqlFrom+=" INNER JOIN C_Invoice i ON(i.c_invoice_id=p.c_invoice_id)";
		s_sqlFrom+=" INNER JOIN C_BPartner bp ON(bp.c_bpartner_id=i.c_bpartner_id)";
		
		ColumnInfo[] s_layoutRemittance = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "C_InvoicePaySchedule_ID"), "C_InvoicePayschedule_ID", IDColumn.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "AD_Org_ID"), "g.Name", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Documentno"), "Documentno", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), "bp.Name", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "DueDate"), "DueDate", Date.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "DueAmt"), "DueAmt", Double.class)};
		
		//Reseteamos el modelo de tabla
		remittance.setModel(new DefaultTableModel());
		remittanceselect.setModel(new DefaultTableModel());
		
		
		String m_sqlRemittance = remittance.prepareTable(s_layoutRemittance, s_sqlFrom, s_sqlWhere, true, null);
		String m_sqlRemittanceselect = remittanceselect.prepareTable(s_layoutRemittance, s_sqlFrom, s_sqlWhereSelected, true, null);
		
		m_sql=m_sqlRemittance;
		m_sqlSelected=m_sqlRemittanceselect;
		

		remittanceselect.setRowSelectionAllowed(true);
		remittanceselect.getSelectionModel().addListSelectionListener(this);
        remittanceselect.autoSize();
        
        remittanceselect.getModel().addTableModelListener(this);
		
		remittance.setRowSelectionAllowed(true);
		remittance.getSelectionModel().addListSelectionListener(this);
        remittance.autoSize();
        remittance.getModel().addTableModelListener(this);
        

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
			
			//Cargamos la tabla seleccionadas
			pstmt = DB.prepareStatement(m_sqlSelected, null);
			rs = pstmt.executeQuery();
			remittanceselect.loadTable(rs);
			
			pstmt = DB.prepareStatement(m_sql, null);
			rs = pstmt.executeQuery();
			remittance.loadTable(rs);

			//Seleccionamos las necesarias
			SelectRows();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Exception=", e);
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
		s_sqlWhereSelected=s_sqlWhere;
		String PartialWhere="";
		String PartialWhereSearch="";
		Collection<String> valores=env.values();
		for(String val : valores) {
			s_sqlWhere+=val;
		}
		//Comprobamos los registros ya seleccionados para guardarlos

		for(Integer sel : list){
			PartialWhere+=sel+",";
			PartialWhereSearch+=sel+",";
		}
		
		if(list.size()>0){
			PartialWhere=PartialWhere.substring(0, PartialWhere.length()-1);
			PartialWhere+=")";
			PartialWhere=" AND p.C_InvoicePaySchedule_ID IN("+PartialWhere;
			
			PartialWhereSearch=PartialWhereSearch.substring(0, PartialWhereSearch.length()-1);
			PartialWhereSearch+=")";
			PartialWhereSearch=" AND p.C_InvoicePaySchedule_ID NOT IN("+PartialWhereSearch;
			
		}else{
			PartialWhere=" AND 1=2"+PartialWhere;
		}
		s_sqlWhereSelected+=PartialWhere;
		s_sqlWhere+=PartialWhereSearch;
		preparetable();
		executeQuery();
		
	
	}
	
	/**
	 * Resetea los valores de seleccion y de consulta
	 */
	
	protected void resetSearch(){
		env.clear();
		list.clear();
	}
	
	
	/**
	 * Setea los valores seleccionados
	 */
	protected void setSelectedValues(Hashtable<String,String> values){
		env=values;
	}
	
	
	/**
	 * Selecciona las filas ya seleccionadas con anterioridad
	 */
	
	private void SelectRows(){

		int totalrows=remittanceselect.getRowCount();
		for(int i=0;i<totalrows;i++){
			IDColumn id = (IDColumn)remittanceselect.getModel().getValueAt(i,0);
			if (id != null){
				id.setSelected(true);
			}
		}
		
	}
	
	@Override
	public void vetoableChange(PropertyChangeEvent arg0)
			throws PropertyVetoException {
		if(arg0.getSource().equals(vOrg)){
			env.remove("AD_Org_ID");
			
			if(arg0.getNewValue()!=null)
				env.put("AD_Org_ID", " AND i.AD_Org_ID="+arg0.getNewValue());

		}
		else if(arg0.getSource().equals(vBPartner)){
			env.remove("C_BPartner_ID");
			
			if(arg0.getNewValue()!=null)
				env.put("C_BPartner_ID", " AND i.C_BPartner_ID="+arg0.getNewValue());

		}
		refreshWhere();
	}

	public void tableChanged( TableModelEvent e ) {
		//Caso desde tabla de busqueda
        if (e.getSource().equals(remittance.getModel())&&e.getColumn()==0 && !generate)
		{
        	IDColumn id = (IDColumn)remittance.getValueAt(remittance.getSelectedRow(), 0);
        	if(id.isSelected()){
        		list.add(id.getRecord_ID());
        		refreshWhere();
        	}
		}
        //Caso desde tabla de seleccionados
        if (e.getSource().equals(remittanceselect.getModel())&&e.getColumn()==0 && !generate)
		{
        	IDColumn id = (IDColumn)remittanceselect.getValueAt(remittanceselect.getSelectedRow(), 0);
        	if(!id.isSelected()){
        		list.remove(id.getRecord_ID());
        		refreshWhere();
        	}
		}
       
    }    // tableChanged

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {

		if(arg0.getActionCommand().equals(ConfirmPanel.A_OK)){
			//Creamos Remesa
			
			RemittanceCreate remittance = new RemittanceCreate(list);
			remittance.setPanelValues(ParentPane.getPanelParams());
			remittance.Create();
			
		}
		
	}

}
