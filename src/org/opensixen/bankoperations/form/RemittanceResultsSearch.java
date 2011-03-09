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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import org.compiere.swing.CButton;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.jdesktop.swingx.JXTaskPane;
import org.opensixen.model.RVOpenItem;

/**
 * 
 * RemittanceResultsSearch
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class RemittanceResultsSearch extends JPanel implements VetoableChangeListener,ListSelectionListener,ActionListener,TableModelListener {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Descripcion de campos
	 */

	//Paneles y tablas
	private MiniTable remittance  = new MiniTable();
	private JXTaskPane searchpane = new JXTaskPane();
	private CPanel remittanceselectpanel = RemittanceResultsSelected.getPanel();
	private MiniTable remittanceselect= RemittanceResultsSelected.getTable();
	private CPanel minitablepanel = new CPanel();
	private CPanel SelectAllPanel = new CPanel();
	private CPanel defPanel = new CPanel();
	private CPanel allPanel = new CPanel();
	protected RemittanceFormPanel ParentPane =null;
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
	
	//Seleccionar todos los registros
	private CLabel lSelectAll = new CLabel();
    private CCheckBox SelectAll = new CCheckBox();
    
	//Deseleccionar todos los registros
	private CLabel lDSelectAll = new CLabel();
    private CCheckBox DSelectAll = new CCheckBox();
	
    //Refrescar busqueda
    private CButton vSearch = new CButton(Msg.translate(Env.getCtx(), "Refresh"));
    
	//Sql busqueda
	private String s_sqlWhere=" 1=1";
	private String s_sqlWhereSelected=" 1=1";
	private String m_sql="";
	private String m_sqlSelected="";
	
	//Variables globales
	Hashtable<String,String> env = new Hashtable<String,String>();
	static HashMap<Integer,RVOpenItem> list = new HashMap<Integer,RVOpenItem>();
	private boolean generate=false;
	int m_WindowNo=0;
	protected CLogger log = CLogger.getCLogger(getClass());

	public RemittanceResultsSearch(){
		fillPicks();
		initComponents();
	}
	
	/**
	 * Constructor con panel padre
	 */
	
	public RemittanceResultsSearch(RemittanceFormPanel panel){
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
		remittanceselectpanel = RemittanceResultsSelected.getPanel();
		remittanceselectpanel.add(new JScrollPane());
		minitablepanel.add(remittanceselectpanel,BorderLayout.NORTH);
		minitablepanel.add(SelectAllPanel,BorderLayout.SOUTH);
		minitablepanel.add(new JScrollPane(remittance),BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.add( allPanel,BorderLayout.NORTH);
		this.add(minitablepanel,BorderLayout.CENTER);
	}
	
	/**
	 * Inicializa el panel de busqueda
	 */
	
	private void initsearch(){
		searchpane.setTitle(Msg.translate(Env.getCtx(), "Filter"));
		searchpane.setLayout(new GridBagLayout());
		defPanel.setLayout(new GridBagLayout());
		allPanel.setLayout(new BorderLayout());
		
		defPanel.add( lOrg,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		defPanel.add( vOrg,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));

		//Columna,fila,?,?,withx,withy
		//searchpane.add( lOrg,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		//searchpane.add( vOrg,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lFromInvoiced,new GridBagConstraints( 0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vFromInvoiced,new GridBagConstraints( 1,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lToInvoiced,new GridBagConstraints( 2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vToInvoiced,new GridBagConstraints( 3,0,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( lBPartner,new GridBagConstraints( 0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets( 2,2,2,2 ),0,0 ));
		searchpane.add( vBPartner,new GridBagConstraints( 1,1,1,1,0.3,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets( 2,2,2,20 ),0,0 ));
		searchpane.add( vSearch,new GridBagConstraints( 0,2,1,1,0.3,0.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets( 10,10,2,10 ),0,0 ));

		
		allPanel.add(defPanel,BorderLayout.NORTH);
		allPanel.add(searchpane,BorderLayout.CENTER);
		
		//Inicializamos el panel de seleccionar todos
		SelectAllPanel.add(lSelectAll);
		SelectAllPanel.add(SelectAll);
		SelectAllPanel.add(lDSelectAll);
		SelectAllPanel.add(DSelectAll);
		
		vSearch.addActionListener(this);
	}
	
	/**
	 * Inicializa los componentes de busqueda
	 */
	
	private void fillPicks(){
		//Organizacion
		MLookup orgL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2163, DisplayType.TableDir);
		vOrg = new VLookup ("AD_Org_ID", false, true, true, orgL);
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
		
		//Seleccionar todos
		lSelectAll.setText(Msg.translate(Env.getCtx(), "Select All"));
		lSelectAll.setLabelFor(SelectAll);
		SelectAll.setActionCommand("SelectAll");
		SelectAll.addActionListener(this);
		
		//DeSeleccionar todos
		lDSelectAll.setText(Msg.translate(Env.getCtx(), "DeSelect All"));
		lDSelectAll.setLabelFor(DSelectAll);
		DSelectAll.setActionCommand("DSelectAll");
		DSelectAll.addActionListener(this);
		
		//Valores por defecto
		vOrg.setValue(Env.getAD_Org_ID(Env.getCtx()));
	}

	/**
	 * Prepara la tabla y su definicion
	 */
	
	private void preparetable(){

		String s_sqlFrom="RV_OpenItem p";
		s_sqlFrom+=" INNER JOIN AD_Org g ON(g.AD_Org_ID=p.AD_Org_ID)";
		s_sqlFrom+=" INNER JOIN C_Invoice i ON(i.c_invoice_id=p.c_invoice_id)";
		s_sqlFrom+=" INNER JOIN C_BPartner bp ON(bp.c_bpartner_id=i.c_bpartner_id)";
		ColumnInfo[] s_layoutRemittance = new ColumnInfo[]{
        		new ColumnInfo(RVOpenItem.ColumnInvoice, "p.C_Invoice_ID", IDColumn.class),
        		//new ColumnInfo(RVOpenItem.ColumnOrg, "g.Name", String.class),
        		new ColumnInfo(RVOpenItem.ColumnDocumentNo, "i.Documentno", String.class),
        		new ColumnInfo(RVOpenItem.ColumnPartner, "bp.Name", String.class),
        		new ColumnInfo(RVOpenItem.ColumnOpenAmt, "OpenAmt", BigDecimal.class),
        		new ColumnInfo(RVOpenItem.ColumnDueDate, "DueDate", Date.class),
        		new ColumnInfo(RVOpenItem.ColumnSchedule,"p.C_InvoicePaySchedule_ID",Integer.class)};
		
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
			loadTableSelects(list);

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
	
	public void loadTableSelects(HashMap<Integer,RVOpenItem> listitems)
	{
		//  Clear Table
		remittanceselect.setRowCount(0);

		try
		{
			Iterator<Map.Entry<Integer,RVOpenItem>> it = list.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry<Integer,RVOpenItem> e = (Map.Entry<Integer,RVOpenItem>)it.next();
				RVOpenItem rs =(RVOpenItem)e.getValue();
		
				int row = remittanceselect.getRowCount();
				remittanceselect.setRowCount(row+1);

				for (int col = 0; col < remittanceselect.getLayoutInfo().length; col++)
				{
					Object data = null;
					String c = remittanceselect.getLayoutInfo()[col].getColHeader();
					data= rs.getValue(c);
					//  store
					remittanceselect.setValueAt(data, row, col);
				}
				
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
			
	}	//	loadTable
	
	/**
	 * Actualiza valores de busqueda y tabla de resultados
	 */
	
	public void refreshWhere(){

		s_sqlWhere=" 1=1 AND i.issotrx='Y' AND i.ispaid='N' AND i.ad_org_id="+vOrg.getValue();//Mientras no exista la validación xml
		s_sqlWhereSelected=s_sqlWhere;
		String PartialWhere="";
		String PartialWhereSearch="";
		Collection<String> valores=env.values();
		for(String val : valores) {
			s_sqlWhere+=val;
		}
		//Comprobamos los registros ya seleccionados para guardarlos
		Iterator<Map.Entry<Integer,RVOpenItem>> it = list.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<Integer,RVOpenItem> e = (Map.Entry<Integer,RVOpenItem>)it.next();
			
			//Sentencia Seleccionado
			PartialWhere+=WhereSelected(e,PartialWhere);
			
			//Sentencia No Seleccionado
			PartialWhereSearch+=WhereNotSelected(e,PartialWhere);
		}
		
		if(list.size()<=0)
			PartialWhere=" AND 1=2"+PartialWhere;
			
		s_sqlWhereSelected+=PartialWhere;
		s_sqlWhere+=PartialWhereSearch;
		if(vFromInvoiced.getTimestamp()!=null)
			s_sqlWhere+=" AND DueDate>=TO_TIMESTAMP('"+vFromInvoiced.getTimestamp()+"','YYYY-MM-DD')";
		if(vToInvoiced.getTimestamp()!=null)
			s_sqlWhere+=" AND DueDate<=TO_TIMESTAMP('"+vToInvoiced.getTimestamp()+"','YYYY-MM-DD')";

		preparetable();
		executeQuery();
		
	
	}
	
	
	private String WhereSelected(Map.Entry<Integer,RVOpenItem> sel,String PartialWhere){
		String aux="";

		if(PartialWhere.length()>0)
			aux+=" OR";
		else
			aux+=" AND";
		
		if((Integer)sel.getValue().getC_InvoicePaySchedule_ID()>0)
			aux+="(";
		
		aux+=" p.c_invoice_id IN(";
		aux+=sel.getKey();
		aux+=")";
		
		if((Integer)sel.getValue().getC_InvoicePaySchedule_ID()>0){
			aux+=" AND p.c_invoicepayschedule_id="+((RVOpenItem)sel.getValue()).getC_InvoicePaySchedule_ID();
			aux+=")";
		}
		return aux;
	}
	
	
	private String WhereNotSelected(Map.Entry<Integer,RVOpenItem> sel,String PartialWhere){
		String aux="";
		aux+=" AND";
		
		if((Integer)sel.getValue().getC_InvoicePaySchedule_ID()>0)
			aux+="(";
		else{
			aux+=" p.c_invoice_id NOT IN(";
			aux+=sel.getKey();
			aux+=")";
		}
		if((Integer)sel.getValue().getC_InvoicePaySchedule_ID()>0){
			aux+=" p.c_invoicepayschedule_id<>"+((RVOpenItem)sel.getValue()).getC_InvoicePaySchedule_ID();
			aux+=" OR p.c_invoicepayschedule_id is null)";
		}
		return aux;
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
	
	public static HashMap<Integer,RVOpenItem> getListSelected(){
		return list;
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
	
	/**
	 * Seleccionamos todos los registros
	 * @param select
	 */
	
	private void SelectAll(boolean select){
		generate=true;
		int totalrows=remittance.getRowCount();
		for(int i=0;i<totalrows;i++){
			IDColumn id = (IDColumn)remittance.getModel().getValueAt(i,0);
			id.setSelected(select);
			//Selecciono el registro
			selectregister(i);
		}

		//Una vez seleccionados llamo al refrescar sentencia
		refreshWhere();

		generate=false;
	}
	
	/**
	 * Eliminamos la seleccion en todos los registros 
	 * @param select
	 */
	
	private void DSelectAll(boolean select){
		
		int totalrows=remittanceselect.getRowCount();
		for(int i=0;i<totalrows;i++){
			IDColumn id = (IDColumn)remittanceselect.getModel().getValueAt(i,0);
			id.setSelected(select);
			//Selecciono el registro
			Dselectregister(i);
			
		}
		//Una vez seleccionados llamo al refrescar sentencia
		refreshWhere();

	}
	
	private void ChangeTotals(){
		BigDecimal total=BigDecimal.ZERO;
		
		Iterator<Map.Entry<Integer,RVOpenItem>> it = list.entrySet().iterator();
		//Comprobamos que la lista no sea vacia
		if(!it.hasNext()){
			RemittanceResultsSelected.fAmtTotal.setValue(BigDecimal.ZERO);
			RemittanceResultsSelected.fNumReg.setValue(BigDecimal.ZERO);
		}
			
		while (it.hasNext()) {
			Map.Entry<Integer,RVOpenItem> e = (Map.Entry<Integer,RVOpenItem>)it.next();
			total=total.add(e.getValue().getDueAmt());
		}
		RemittanceResultsSelected.fAmtTotal.setValue(total);
		RemittanceResultsSelected.fNumReg.setValue(list.size());
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

        	selectregister(remittance.getSelectedRow());
    		refreshWhere();
		}
        //Caso desde tabla de seleccionados
        if (e.getSource().equals(remittanceselect.getModel())&&e.getColumn()==0 && !generate)
		{
        	//IDColumn id = (IDColumn)remittanceselect.getValueAt(remittanceselect.getSelectedRow(), 0);
        	Dselectregister(remittanceselect.getSelectedRow());
        	refreshWhere();

		}
       
    }    // tableChanged
	
	/**
	 * Selecciona un registro
	 * @param row
	 */
	
	private void selectregister(int row){

    	IDColumn id = (IDColumn)remittance.getValueAt(row, 0);

    	if(id.isSelected())
    		list.put(id.getRecord_ID(), new RVOpenItem(id.getRecord_ID(),remittance,row));
    	ChangeTotals();
	}
	
	/**
	 * Eliminamos la seleccion de un registro
	 * @param row
	 */
	
	private void Dselectregister(int row){
		
    	IDColumn id = (IDColumn)remittanceselect.getValueAt(row, 0);
       	if(!id.isSelected())
    		list.remove(id.getRecord_ID());
       	ChangeTotals();
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Procede a crear la remesa en base de dato
	 * @return
	 */


	@Override
	public void actionPerformed(ActionEvent arg0) {

		if(arg0.getActionCommand().equals(SelectAll.getActionCommand())){
			//Seleccionamos todos los registros
			SelectAll(true);
		}
		else if(arg0.getActionCommand().equals(DSelectAll.getActionCommand())){
			//Deseleccionamos todos los registros
			DSelectAll(false);
		}
		else if(arg0.getSource().equals(vSearch)){
			refreshWhere();
		}
	}


}
