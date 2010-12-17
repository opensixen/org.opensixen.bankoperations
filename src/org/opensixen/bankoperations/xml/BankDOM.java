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


import java.io.IOException;
import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

import org.opensixen.process.RemittanceDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * BankDOM
 *
 * @author Alejandro González
 * Nexis Servicios Informáticos http://www.nexis.es
 */

public class BankDOM {
	
  
  /** Lee los datos del xml y pasa al siguiente. */
	
  public static void getElementDOM(Node node,RemittanceDataSource remi) {

	  int type = node.getNodeType();
	  
	  switch (type) {

      	case Node.DOCUMENT_NODE: {
      		//Nodo principal de documento, tipo de xml,etc
      		getElementDOM(((Document)node).getDocumentElement(),remi);
      		break;
      	}

      	case Node.ELEMENT_NODE: {
      		//Creamos objeto de tipo BankNode con este nodo
      		BankNode.doNode(node,remi);
      		//Leemos los nodos hijos
      		NodeList children = node.getChildNodes();
      		if (children != null) {
      			int len = children.getLength();
      			for (int i = 0; i < len; i++)
    			  	getElementDOM(children.item(i),remi);
      		}

      		break;
      	}
      
    }

  }
  
  /**
   * Parsea el fichero XML y crea el documento
   * @param fileName
   * @return Document
   */
  public static Document parse(String fileName) {
    Document document = null;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setValidating(false);
    factory.setNamespaceAware(true);

    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.parse( new File(fileName));
      
      return document;

    } catch (SAXParseException spe) {

      Exception x = spe;
      if (spe.getException() != null)
        x = spe.getException();
      x.printStackTrace();
    } catch (SAXException sxe) {
      // Error generated during parsing
      Exception x = sxe;
      if (sxe.getException() != null)
        x = sxe.getException();
      x.printStackTrace();
    } catch (ParserConfigurationException pce) {
      // Parser with specified options can't be built
      pce.printStackTrace();
    } catch (IOException ioe) {
      // I/O error
      ioe.printStackTrace();
    }

    return null;
  }

}

