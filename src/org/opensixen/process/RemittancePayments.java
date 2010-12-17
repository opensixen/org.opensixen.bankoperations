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
 *  Indeos Consultoria S.L. - http://www.indeos.es
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

package org.opensixen.process;

import javax.swing.JOptionPane;

import org.compiere.model.MPayment;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.opensixen.model.MRemittance;
import org.opensixen.model.MRemittanceLine;

/**
 * 
 * RemittancePayments 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class RemittancePayments {
	
	private String trxName;
	
	public RemittancePayments(MRemittance remittance){
		
		doIt(remittance,true);
	}

	
	public RemittancePayments() {
	}

	/**
	 * 
	 * @param remittance
	 * @param completeIt
	 */

	public void doIt(MRemittance remittance,boolean completeIt) {
		
		for(MRemittanceLine line : remittance.getLines(true)){
			if(!createpayment(line,completeIt)){
				JOptionPane.showMessageDialog(null,Msg.translate(Env.getCtx(), "Payments not save"), "Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
			if(completeIt){
				remittance.setDocAction( MRemittance.ACTION_Complete );
				
				try {
					remittance.processIt( MRemittance.ACTION_Complete );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				if (!remittance.save())
					break;
			}
		}
		
	}
	
	/**
	 * Crea los pagos y los asocia a la linea de Remesa
	 * @param line
	 * @param completeIt
	 * @return
	 */
	
	private boolean createpayment(MRemittanceLine line,boolean completeIt){
		MPayment payment = new MPayment(Env.getCtx(),0,trxName);
		MRemittance remit = new MRemittance(Env.getCtx(),line.getC_Remittance_ID(),trxName);
		payment.setC_BankAccount_ID(remit.getC_BankAccount_ID());
		payment.setAmount(Env.getContextAsInt(Env.getCtx(), "C_Currency_ID"), line.getGrandTotal());
		payment.setC_Invoice_ID(line.getC_Invoice_ID());
		payment.setC_BPartner_ID(line.getC_BPartner_ID());
		
		if(!payment.save())
			return false;
		
		if(completeIt){
			payment.setDocAction( MPayment.ACTION_Complete );
			
			try {
				payment.processIt( MPayment.ACTION_Complete );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if (!payment.save())
				return false;
		}
		
		line.setC_Payment_ID(payment.getC_Payment_ID());
		return line.save(trxName);
	}

}
