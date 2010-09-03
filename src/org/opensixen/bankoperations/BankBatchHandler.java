package org.opensixen.bankoperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.swing.JFileChooser;
import org.compiere.util.Env;
import org.compiere.util.ExtensionFileFilter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BankBatchHandler extends DefaultHandler {
	
	public BankBatchHandler(File file){
		outFile=file;
	}
	/**
	 * Valores para los diferentes elementos
	 */
	private String norma="norma";
	private String line="line";
	private String data="data";
	private String size="size";
	private String database="database";
	private String value="value";
	private String type="type";
	private String inidefault="default";
	private String alignment="alignment";
	private String format="format";
		
	private Writer writer;
	private File   outFile;
	StringBuffer sb = new StringBuffer("");
	
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException {
		
		
		//Empezamos con el mapeo de caracteristicas
		if(qName.equals(norma)){
			//Creamos el fichero
			try {
				outFile.createNewFile();
				FileWriter fwout = new FileWriter (outFile, false);
				writer = new BufferedWriter(fwout);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(qName.equals(line)){
			//Creamos linea nueva en la que vamos generando los diferentes datos
			sb = new StringBuffer("");
		}else if(qName.equals(data)){
			//Generamos los datos de la linea
			writeline(attributes);
		}
		
	}
	/**
	 * Escribe en el buffer de linea los datos necesarios
	 * @param attr
	 */
	private void writeline(Attributes attr){
		sb.append(attr.getValue(value));
	}
	
	
	public void endElement(String uri, String localName, String qName)
		throws SAXException {
		
		if(qName.equals(line)){
			//Guardamos la linea
			saveLine(sb.toString());
		}
		else if(qName.equals(norma)){
			//Cerramos el fichero
			closeFile();
		}
	}
	/**
	 * Cierra el fichero creado
	 */
	private void closeFile(){
		try
		{
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Guarda la linea generada
	 * @param linea
	 */
	private void saveLine(String linea)
	{
		try
		{
			if (linea != null)
				if (linea.length() > 0)
				{
					writer.write(linea);
					writer.write(Env.NL);
				}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	
}
