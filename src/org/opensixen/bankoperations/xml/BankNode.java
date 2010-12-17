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

package org.opensixen.bankoperations.xml;


import java.util.ArrayList;

import org.opensixen.process.RemittanceDataSource;
import org.w3c.dom.Node;

/**
 * 
 * Activator 
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class BankNode {
	
	/**
	 * Descripción de tipos de elementos y datos
	 */
	
	public static final String OperationLine="OPERATIONLINE"; 
	public static final String OperationData="DATA"; 
	public static final String OperationWhile="WHILE";
	public static final String OperationENDWhile="ENDWHILE";
	
	public static final String Data_OrgName="ORGNAME";
	public static final String Data_Duns="DUNS";
	public static final String Data_GenerateDate="GENERATEDATE";
	public static final String Data_ExecuteDate="EXECUTEDATE";
	public static final String Data_DateInvoiced="DATEINVOICED";
	public static final String Data_AccountNo="ACCOUNTNO";
	public static final String Data_BPName="BPNAME";
	public static final String Data_DocumentNo="DOCUMENTNO";
	public static final String Data_LineTotal="LINETOTAL";
	public static final String Data_BPAccountNo="BPACCOUNTNO";
	public static final String Data_TotalAmt="TOTALAMT";
	
	private static boolean whilenode=false;
	private static ArrayList<Node> nodes= new ArrayList<Node>();
	
	
	public static void doNode(Node node,RemittanceDataSource remi){
		
		if(node.getNodeName().equals(OperationLine)) {
			if(whilenode)
				addNode(node);
			BankNodeElement.ElementFile(node,remi);
		}
		else if(node.getNodeName().equals(OperationData)) {
			if(whilenode)
				addNode(node);
			BankNodeData.ElementData(node,remi);
		}
		else if(node.getNodeName().equals(OperationWhile)){
			whilenode=true;
		}
		else if(node.getNodeName().equals(OperationENDWhile)){
			whilenode=false;
			//Repetimos los nodos hasta fin de datasource
			doWhileNodes(remi);
		}

	}
	
	/**
	 * Añade nodo a la lista del bloque a repetir
	 * @param node
	 */

	public static void addNode(Node node){
		nodes.add(node);
	}
	
	/**
	 * Devuelve la lista de nodos a repetir
	 * @return ArrayList<Node>
	 */
	
	public ArrayList<Node> getWhileNodes(){
		return nodes;
	}
	
	/**
	 * Repite el bloque entre while tantas veces como registros tenga el datasource
	 * (A partir del siguiente registro ya que el primero ya se ejecuto)
	 * @param remi
	 */
	
	private static void doWhileNodes(RemittanceDataSource remi){
		while (remi.next()){
			for(Node nod : nodes){
				doNode(nod,remi);
			}
		}
		//Una vez acabado devolvemos el datasource a un registro capaz de sacar datos
		remi.previous();
		nodes.clear();
	}
	
}
