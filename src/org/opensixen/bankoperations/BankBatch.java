package org.opensixen.bankoperations;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.compiere.process.SvrProcess;
import org.compiere.util.ExtensionFileFilter;
import org.xml.sax.SAXException;

public class BankBatch extends SvrProcess  {

	public BankBatch() {
		
		//Bug OsGi, pierde los defaults del look and feel instalado
		UIManager.getLookAndFeelDefaults().put("ClassLoader", UIManager.getLookAndFeel().getClass().getClassLoader());

	}

	@Override
	public String doIt()  {
		
	     
	    try {
	    	//Creamos nueva instancia de xml parser
	    	SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
						
			//Seleccionamos el fichero de remesa a generar
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setDialogTitle("Remesa");
			chooser.showOpenDialog(new JFrame());
			File outFile = ExtensionFileFilter.getFile(chooser.getSelectedFile(),chooser.getFileFilter());
			
			//Ejecutamos la lectura del handler de xml de normas
			BankBatchHandler handler = new BankBatchHandler(outFile);
			
			try {
				saxParser.parse("/home/alex/prueba.xml", handler);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return null;
	}

	@Override
	public void prepare() {
		

	}

}
