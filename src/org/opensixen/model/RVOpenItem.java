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

package org.opensixen.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrg;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * 
 * RVOpenItem
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class RVOpenItem {

	private Hashtable<String,Object> RowValues = new Hashtable<String,Object>();
	
	/**
	 * Cabeceras columnas tabla
	 */
	public static final String ColumnInvoice=Msg.translate(Env.getCtx(), "C_Invoice_ID");
	public static final String ColumnOrg=Msg.translate(Env.getCtx(), "AD_Org_ID");
	public static final String ColumnDocumentNo=Msg.translate(Env.getCtx(), "Documentno");
	public static final String ColumnPartner=Msg.translate(Env.getCtx(), "C_BPartner_ID");
	public static final String ColumnDueDate=Msg.translate(Env.getCtx(), "DueDate");
	public static final String ColumnOpenAmt=Msg.translate(Env.getCtx(), "OpenAmt");
	public static final String ColumnSchedule=Msg.translate(Env.getCtx(), "");
	
	/**
	 * 
	 * Constructor estandar
	 */
	
	public RVOpenItem(){
		
	}

	public RVOpenItem(X_C_remittanceLine rl){
		
		MInvoice inv = new MInvoice(Env.getCtx(),rl.getC_Invoice_ID(),null);
		MOrg org = new MOrg(Env.getCtx(),inv.getAD_Org_ID(),null);
		MBPartner partner = new MBPartner(Env.getCtx(),inv.getC_BPartner_ID(),null);
		//ID
		setValue(ColumnInvoice,new IDColumn(rl.getC_Invoice_ID()));
		//Posible payschedule
		setValue(ColumnSchedule,rl.getC_InvoicePaySchedule_ID());
		//Org
		setValue(ColumnOrg,org.getName());
		//DocumentNo
		setValue(ColumnDocumentNo,inv.getDocumentNo());
		//Partner
		setValue(ColumnPartner,partner.getName());
		//DueDate
		setValue(ColumnDueDate,inv.getDateInvoiced());
		//Total
		setValue(ColumnOpenAmt,rl.getGrandTotal());
	}
	
	public RVOpenItem(Integer record_ID, MiniTable remittance, int row) {

		setValues(remittance,row);
	}
	
	private void setValues(MiniTable tab, int rowselected){
		
		for (int col = 0; col < tab.getLayoutInfo().length; col++)
		{
			//Cogemos el valor de la cabecera
			String c = tab.getLayoutInfo()[col].getColHeader();
			//Lo añadimos al hash
			setValue(c,tab.getValueAt(rowselected, col));
		}
	}
	
	private void setValue(String header,Object value  ){
		RowValues.put(header,value);
	}

	public Object getValue(String header){
		Object data = null;
		//Comprobamos los registros ya seleccionados para guardarlos
		Iterator<Map.Entry<String,Object>> it = RowValues.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String,Object> e = (Map.Entry<String,Object>)it.next();
			if(e.getKey().equals(header)){
				data=e.getValue();
				break;
			}
		}
		return data;
	}
	
	/**
	 * Visualizadores de la clase
	 * @return
	 */
	
	public int getC_Invoice_ID(){
		IDColumn id = (IDColumn)getValue(ColumnInvoice );
    	return id.getRecord_ID();
	}
	
	public int getC_InvoicePaySchedule_ID(){
		return (Integer)getValue("");
	}
	
	
	public BigDecimal getDueAmt(){
		return (BigDecimal)getValue(ColumnOpenAmt );
	}

	
}
