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

package org.opensixen.bankoperations.xml;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opensixen.process.RemittanceCreateFile;
import org.opensixen.source.RemittanceDataSource;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 * BankNodeData
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class BankNodeData {

	//Posibles atributos
	public static final String Atrib_Type="type"; //Indica donde se obtiene el valor
	public static final String Atrib_FillValue="fillvalue"; //Indica el valor de relleno 
	public static final String Atrib_Length="length";//Indica la longitud del campo
	public static final String Atrib_Initialpos="initialpos";//Indica el valor desde donde se sacara un substring hasta initialpos+length
	public static final String Atrib_formatdate="formatdate";//Formato de fecha
	public static final String Atrib_Trim="trim";//Valores a eliminar del string
	public static final String Atrib_Align="align";//Alineacion del campo, posibles valores LEFT , RIGHT

	public static final String Atrib_AlignLeft="LEFT";
	public static final String Atrib_AlignRight="RIGHT";
	
	//Valores de atributo type
	public static final String Atrib_Type_Text="TEXT";
	public static final String Atrib_Type_DB="DB";
	public static final String Atrib_Type_Free="FREE";
	public static final String Atrib_Type_Count1="COUNTDOM";//Numero total de domiciliaciones
	public static final String Atrib_Type_Count2="COUNTREG";//Numero total de registros excepto el actual
	public static final String Atrib_Type_Count3="COUNTTOTAL";//Numero total de registros incluido el actual

	public static void ElementData(Node m_node,RemittanceDataSource remi) {
		//Definimos las variables necesarias para el formateo del dato
		String finalvalue=null;
		String fillvalue=null;
		String trim=null;
		String align=null;
		
		int length=-1;
		int initialpos=-1;
		String formatdate=null;
		//Buscamos todos los atributos que tenga el campo
		NamedNodeMap attrs = m_node.getAttributes();

		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);

  		  	//El atributo tipo es el primero en comprobar ya que indica si el dato es de base de datos o texto
  		  	if(attr.getNodeName().equals(Atrib_Type)){
  			  
  		  		//Atributo tipo de campo, debe ser el primero es leer
  		  		if(attr.getNodeValue().equals(Atrib_Type_Text)){
  		  			finalvalue=m_node.getTextContent();
  		  			//Si el valor es diferente de nulo lo guardamo sin mas
  		  		}else if(attr.getNodeValue().equals(Atrib_Type_DB)){
  		  			//Tipo Base de datos
  		  			if(remi.getFieldValue(m_node.getTextContent())!=null){
  		  				//Valor sin formatear
  		  				finalvalue=remi.getFieldValue(m_node.getTextContent()).toString();
  		  			}//Fin if
  		  		}else if(attr.getNodeValue().equals(Atrib_Type_Free)){
  		  			//Tipo libre, espacios en blanco
  		  			finalvalue="";
  		  		}else if(attr.getNodeValue().equals(Atrib_Type_Count1)){
  		  			finalvalue=String.valueOf(remi.getCurrentRecord()+1);
  		  		}else if(attr.getNodeValue().equals(Atrib_Type_Count2)){
  		  			finalvalue=String.valueOf(RemittanceCreateFile.getNumberReg());
  		  		}else if(attr.getNodeValue().equals(Atrib_Type_Count3)){
  		  			finalvalue=String.valueOf(RemittanceCreateFile.getNumberReg()+1);
  		  		}//Fin else
  		  		
  		  	}//Fin Atributo type
  		  	//Tipo relleno
  		  	else if(attr.getNodeName().equals(Atrib_FillValue)){
  		  		fillvalue=attr.getNodeValue();
  		  	}
  		  	//Tipo longitud
  		  	else if(attr.getNodeName().equals(Atrib_Length)){
  		  		length=Integer.valueOf(attr.getNodeValue());
  		  	}
  		  	//Tipo posicion inicial
  		  	else if(attr.getNodeName().equals(Atrib_Initialpos)){
  		  		initialpos=Integer.valueOf(attr.getNodeValue());
  		  	}
  		  	//Tipo formato de fecha
  		  	else if(attr.getNodeName().equals(Atrib_formatdate)){
  		  		formatdate=attr.getNodeValue();
  		  	}
  		  	else if(attr.getNodeName().equals(Atrib_Trim)){
  		  		trim=attr.getNodeValue();
  		  	}
  		  	else if(attr.getNodeName().equals(Atrib_Align)){
  		  		align=attr.getNodeValue();
  		  	}
  	  	}
		
		//Llamamos a la funcion de formateo
		finalvalue= formatvalue(finalvalue,fillvalue,length,initialpos,formatdate,trim,align);
		//Guardamos el valor ya formateado
		RemittanceCreateFile.saveLine(finalvalue);
		
	}
	
	/**
	 * Formateamos el valor para que coincida con las indicaciones de la norma
	 * @param finalvalue
	 * @param fillvalue
	 * @param length
	 * @param initialpos
	 * @param formatdate
	 * @return
	 */
	
	private static String formatvalue(String finalvalue, String fillvalue,
			int length, int initialpos, String formatdate,String trim,String align) {
		//Nos aseguramos que el valor no sea nulo
		if(finalvalue==null)
			finalvalue="";
		//Limpiamos el string de caracteres
		if(trim!=null)
		  finalvalue=trimString(finalvalue,trim);
		
		//En el caso de fechas, formateo
		if(formatdate!=null){
			finalvalue=formatdate(finalvalue,formatdate);
		}
		
		//Limpiamos el string de espacios blancos
		finalvalue=finalvalue.trim();

		
		//En el caso en el que sea un substring de la cadena
		if(initialpos>=0){
			int finallength=(initialpos+length)>finalvalue.length()?finalvalue.length():(initialpos+length);
			if (finallength<0)
				finallength=0;
			
			String subs=finalvalue.substring(initialpos, finallength);
			finalvalue=subs;
		}
		//Longitud
		finalvalue=adjustlength(finalvalue,fillvalue,length,align);
		
		return finalvalue;
	}
	
	
	public static String trimString(String finalvalue, String trim){
		
		//Eliminamos los caracteres especificados

		for( char a : trim.toCharArray()){
			finalvalue=finalvalue.replace(String.valueOf(a),"");
		}
		//Devolvemos el string limpio
		return finalvalue;
	}
	
	/**
	 * 
	 * @param finalvalue
	 * @param formatdate
	 * @return
	 */
	
	private static String formatdate(String finalvalue, String formatdate) {
		String cadenaFecha=null;
		SimpleDateFormat formato = new SimpleDateFormat(formatdate);
		//Formato estándar de fecha guardada en bd
		SimpleDateFormat toDate= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			
			Date date = toDate.parse(finalvalue);
			cadenaFecha = formato.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return cadenaFecha;
	}

	/**
	 * Ajusta la longitud del valor al informado
	 * @param finalvalue
	 * @param fillvalue
	 * @param length
	 * @return
	 */
	
	private static String adjustlength(String finalvalue,String fillvalue,int length,String align){
		//Ajustar el valor a la longitud pedida
		int cont=finalvalue.length();
		String aux=finalvalue;
		
		//Por defecto alineamos a la izquierda
		if(align==null)
			align=Atrib_AlignLeft;
		
		if(cont<length){
			while(aux.length()<length){
				if(align.equals(Atrib_AlignRight))
					aux=fillvalue+aux;
				else if(align.equals(Atrib_AlignLeft))
					aux=aux+fillvalue;
			}
		}else if(cont>length){
			
			aux=finalvalue.substring(0, length);
		}
		return aux;
	}
	
	


	
}
