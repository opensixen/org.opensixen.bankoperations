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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;

import org.compiere.util.Env;
import org.compiere.util.ExtensionFileFilter;
import org.compiere.util.Msg;
import org.opensixen.bankoperations.xml.BankDOM;
import org.opensixen.model.MBankRegulation;
import org.opensixen.model.MRemittance;
import org.w3c.dom.Document;

/**
 * 
 * RemittanceCreateFile
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class RemittanceCreateFile {

	
	private static BufferedWriter writer=null;
	private static int numberregs=0;
	private MRemittance remit=null;
	
	//Codificacion
	private static String codification="ISO-8859-15";//Codificacion del fichero en windows
	private static String lineend="\r\n";//Fin de linea formato windows
	/**
	 * Constructor
	 * @param remit
	 */
	
	public RemittanceCreateFile(MRemittance rem) {
		remit=rem;
		CreateFile();
	}

	
	/**
	 * Devuelve el writer actual
	 * @return writer
	 */
	
	public static BufferedWriter getwriter(){
		  return writer;
	}
	
	
	/**
	 * Setea el writer
	 * @param writer
	 */
	
	public static void setwriter(BufferedWriter writ){
		  writer=writ;
	}
	
	
	/**
	 * Guardar el string al writter
	 * @param line
	 */
	
	public static void saveLine(String line){
			
		try {
			writer.write(line);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Añade una nuevalinea cerrando la anterior
	 * @param line
	 */
	
	public static void closeLine(){
			
		try {

			writer.write(lineend);
			numberregs++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	
	/**
	 * Proceso principal para crear el fichero e iniciar el proceso de lectura del xml
	 */
	
	private void CreateFile(){
  
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle(Msg.getMsg(Env.getCtx(), "Remittance"));

		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) 
			return;
	
		File outFile = ExtensionFileFilter.getFile(chooser.getSelectedFile(),chooser.getFileFilter());
		try { 
			outFile.createNewFile();
		} catch (IOException e) {
				
		}		
		
		try {	
			//FileWriter fwout = new FileWriter (outFile, false);
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),codification));
			//Cogemos el datasource
			RemittanceDataSource source = new RemittanceDataSource(remit);

			//Cargamos el xml
			MBankRegulation regulation = new MBankRegulation(Env.getCtx(),remit.getC_BankRegulation_ID(),null);
			Document document = null;
			document = regulation.getXML();
			BankDOM.getElementDOM(document,source);
 
			//Cerramos el writer
			writer.flush();
			writer.close();
		
		}catch (FileNotFoundException fnfe){		
		
		}
		 catch (IOException e){
			
		 }
			
	}
	
	/**
	 * Devuelve el total de registros escritos hasta el momento 
	 * @return
	 */
	
	public static int getNumberReg(){
		return numberregs;
	}
	
}
