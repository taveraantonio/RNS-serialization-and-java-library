package it.polito.dp2.RNS.sol1;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.RNS.RnsReader;
import it.polito.dp2.RNS.RnsReaderException;
import it.polito.dp2.RNS.sol1.jaxb.Rns;

public class RnsReaderFactory extends it.polito.dp2.RNS.RnsReaderFactory{

	
	@Override
	public RnsReader newRnsReader() throws RnsReaderException {
		
		// Check if the system property is set otherwise throw an exception 
		if(System.getProperty("it.polito.dp2.RNS.sol1.RnsInfo.file")==null)
			throw new RnsReaderException("Property Rnsinfo.file not set"); 
		
		// Instantiate the file, check if it exist 
		File inputFile = new File(System.getProperty("it.polito.dp2.RNS.sol1.RnsInfo.file"));
		if(!inputFile.exists())
			throw new RnsReaderException("Input file does not exists"); 
			
		try{
			// Create the unmarshaller and the schema factory 
			JAXBContext jaxbc = JAXBContext.newInstance(Rns.class);
			Unmarshaller u = jaxbc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			try{
				// Set the schema and create a personal event handler for validation
				Schema schema = sf.newSchema(new File("xsd/rnsInfo.xsd"));
				u.setSchema(schema);
				u.setEventHandler(
						new ValidationEventHandler(){
							public boolean handleEvent(ValidationEvent ve){
								if(ve.getSeverity()!=ValidationEvent.WARNING){
									ValidationEventLocator vel = ve.getLocator();
									System.out.println("Line:Col[" + vel.getLineNumber()+":"+vel.getColumnNumber()+":"+ve.getMessage());
								}
								return true; 
							}
						}
						);
				
			}catch(SAXException se){
				// Catch the SAX exception, unable to validate 
				System.out.println("Unable to validate due to the following error");
				se.printStackTrace(); 
			}
			
			// Unmarshal the input file and cast it to rns 
			Rns rns = (Rns)u.unmarshal(inputFile);
			// Instantiate a new SolRnsReader
			SolRnsReader solRnsReader = new SolRnsReader(rns); 
			return solRnsReader; 
		
			// Check exceptions
		} catch(UnmarshalException ue){
			System.out.println("Caught UnmarshalException");
			ue.printStackTrace();
		} catch(JAXBException je) {
			System.out.println("Caught JAXBException");
			je.printStackTrace();
		} catch(ClassCastException ce){
			System.out.println("Caught ClassCastException");
			ce.printStackTrace();
		} catch(NullPointerException npe){
			System.out.println("Caught NullPointerException");
			npe.printStackTrace();
		} catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		
		return null; 
		
	}
}
