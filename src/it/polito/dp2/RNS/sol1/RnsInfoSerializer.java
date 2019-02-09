package it.polito.dp2.RNS.sol1;

import java.io.File;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import it.polito.dp2.RNS.*;
import it.polito.dp2.RNS.RnsReaderFactory; 

import it.polito.dp2.RNS.sol1.jaxb.*;
import it.polito.dp2.RNS.sol1.jaxb.Rns.Entity;
import it.polito.dp2.RNS.sol1.jaxb.PlaceObject.Place;
import it.polito.dp2.RNS.sol1.jaxb.ParkingArea.Services;
import it.polito.dp2.RNS.sol1.jaxb.ParkingArea.Services.Service;
import it.polito.dp2.RNS.sol1.jaxb.RoadSegment.Road; 
import it.polito.dp2.RNS.sol1.jaxb.VehicleObject.Vehicle;
import it.polito.dp2.RNS.sol1.jaxb.VehicleState;
import it.polito.dp2.RNS.sol1.jaxb.VehicleType; 


public class RnsInfoSerializer {
	
	private RnsReader monitor;		//rns random generator monitor
	private ObjectFactory ob;		//object factory for my JAXB classes  
	
	/**
	 * Default constructor
	 * @throws RnsReaderException 
	 */
	public RnsInfoSerializer() throws RnsReaderException {
		try{
			RnsReaderFactory factory = RnsReaderFactory.newInstance();
			this.monitor = factory.newRnsReader();
			this.ob = new ObjectFactory(); 
		}catch(Exception e){
			System.out.println("Error initializing the constructor");
			throw new RnsReaderException();
		}
	}
	
	public RnsInfoSerializer(RnsReader monitor) {
		super();
		this.monitor = monitor;
	}

	
	/**
	 * Call the read data from random generator and marshal it to file
	 * @param args
	 */
	public static void main(String[] args) {
		RnsInfoSerializer wf;
		try {
			wf = new RnsInfoSerializer();
			wf.saveToFile(args[0]);
		} catch (RnsReaderException e) {
			System.err.println("Could not instantiate data generator.");
			e.printStackTrace();
			return;  
		} 
	}


	/**
	 * Load what generated by the random generator to the Rns structure,
	 * validate and marshal it
	 * @param outputFile is the file where to marshal the Rns data
	 */
	public void saveToFile(String outputFile){
		
		// Define a new rns 
		Rns rns = this.ob.createRns();
		
		// Call that functions to set into rns all the information received  
		this.setGates(rns); 
		this.setParkingArea(rns); 
		this.setRoadSegment(rns); 
		this.setVehicle(rns);
		
		
		// Create the Marshaller 
		JAXBContext jaxbc = null; 
		Marshaller marshaller = null; 
		
		try{
			jaxbc = JAXBContext.newInstance("it.polito.dp2.RNS.sol1.jaxb"); 
			marshaller = jaxbc.createMarshaller();
		}catch(JAXBException je){
			System.out.println("Error while creating marshaller");
            je.printStackTrace();
		}catch(Exception e){
			System.out.println("Catched exception while creating marshaller");
			e.printStackTrace();
		}
		
		try{
			// Instantiate the schema where validate the output file after marshaling 
			SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File("xsd/rnsInfo.xsd")); 
			
			// Marshal and validate the rns system to the output file
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setSchema(schema);
			marshaller.setEventHandler(new MyValidationEventHandler());
			marshaller.marshal(rns, new File(outputFile));
			
		} catch(JAXBException je){
			System.out.println("Error invalid schema or data. Error following:");
			je.printStackTrace();
		} catch(SAXException se) {
			System.out.println("Unable to validate due to the following error:");
			se.printStackTrace();
		} catch(Exception e){
			System.out.println("Catched Exception");
		}
		
	}
	
	
	/**
	 * Load the gates from the random generator monitor
	 * @param rns structure
	 */
	private void setGates(Rns rns){
		
		// SET GATES
		Set<GateReader> gateSet = this.monitor.getGates(null); 
		for (GateReader gate : gateSet){
			Entity e = this.ob.createRnsEntity(); 
			PlaceObject po = this.ob.createPlaceObject();
			Place p = this.ob.createPlaceObjectPlace();
					
			e.setEntityID(gate.getId());
			p.setPlaceID(gate.getId());
			p.setCapacity(BigInteger.valueOf(gate.getCapacity()));
			p.setGate(it.polito.dp2.RNS.sol1.jaxb.GateType.fromValue(gate.getType().name()));
					
			// Get and set list of connections for that place
			Set<PlaceReader> connections = gate.getNextPlaces(); 
			for(PlaceReader place: connections){
				p.getConnectedTo().add(place.getId());
			}
			
			po.setPlace(p);
			e.setPlaces(po);
			rns.getEntity().add(e); 			
		}
		return; 		
	}


	/**
	 * Load the parking areas from the random generator monitor 
	 * @param rns structure
	 */
	private void setParkingArea(Rns rns){
		
		// SET PARKING AREAS
		Set<ParkingAreaReader> paset = this.monitor.getParkingAreas(null);
		for(ParkingAreaReader parking: paset){
			Entity e = this.ob.createRnsEntity();
			PlaceObject po = this.ob.createPlaceObject(); 				
			Place p = this.ob.createPlaceObjectPlace();
					
			e.setEntityID(parking.getId());
			p.setPlaceID(parking.getId());
			p.setCapacity(BigInteger.valueOf(parking.getCapacity()));
					
			// Add services 
			Services services = this.ob.createParkingAreaServices();
			for(String str : parking.getServices()){
				Service s = this.ob.createParkingAreaServicesService();
				s.setServiceName(str);
				services.getService().add(s); 
			}
			ParkingArea pa = this.ob.createParkingArea();
			pa.setServices(services);
			p.setParkingArea(pa);
					
			// Get and set list of connections for that place
			Set<PlaceReader> connections = parking.getNextPlaces(); 
			for(PlaceReader place: connections){
				p.getConnectedTo().add(place.getId());
			}
					
			po.setPlace(p);
			e.setPlaces(po);
			rns.getEntity().add(e); 			
		}
		return; 
	}
	

	/**
	 * Load the roadSegments from the random generator monitor
	 * @param rns structure
	 */
	private void setRoadSegment(Rns rns){
		
		//SET ROAD SEGMENT; 
		Set<RoadSegmentReader> roadset = this.monitor.getRoadSegments(null);
		for(RoadSegmentReader road: roadset){
			Entity e = this.ob.createRnsEntity();
			PlaceObject po = this.ob.createPlaceObject();
			Place p = this.ob.createPlaceObjectPlace(); 

			e.setEntityID(road.getId());
			p.setPlaceID(road.getId());
			p.setCapacity(BigInteger.valueOf(road.getCapacity()));

			RoadSegment rs = this.ob.createRoadSegment();
			rs.setRoadSegmentName(road.getName());
			Road r = this.ob.createRoadSegmentRoad();
			r.setRoadName(road.getRoadName());
			rs.setRoad(r);
			p.setRoadSegment(rs);

			//get and set list of connections for that place
			Set<PlaceReader> connections = road.getNextPlaces(); 
			for(PlaceReader place: connections){
				p.getConnectedTo().add(place.getId());
			}

			po.setPlace(p);
			e.setPlaces(po);
			rns.getEntity().add(e); 
		}
		return; 		
	}
	

	/**
	 * Load the Vehicles from the random generator monitor
	 * @param rns structure
	 */
	private void setVehicle(Rns rns){
		//SET VEHICLE
		Set<VehicleReader> vehicleset = this.monitor.getVehicles(null,null,null);
		for(VehicleReader vehicle: vehicleset){
			Entity e = this.ob.createRnsEntity(); 
			VehicleObject vo = this.ob.createVehicleObject();
			Vehicle v = this.ob.createVehicleObjectVehicle();

			e.setEntityID(vehicle.getId());
			v.setVehicleID(vehicle.getId());
			v.setVehicleState(VehicleState.fromValue(vehicle.getState().name()));
			v.setVehicleType(VehicleType.fromValue(vehicle.getType().name()));

			Date date = vehicle.getEntryTime().getTime(); 
			GregorianCalendar gc = new GregorianCalendar(); 
			gc.setTime(date);
			XMLGregorianCalendar xgc = null;
			try {
				xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				
			} catch (DatatypeConfigurationException e1) {
				e1.printStackTrace();
			} 
			v.setEntryTime(xgc);

			Localization l = this.ob.createLocalization(); 
			l.setComesFrom(vehicle.getOrigin().getId());
			l.setDirectedTo(vehicle.getDestination().getId());
			l.setPosition(vehicle.getPosition().getId());
			v.setLocalization(l);

			vo.setVehicle(v);
			e.setVehicles(vo);
			rns.getEntity().add(e); 
		}
		return; 
	}
	

	/**
	 * Personal validation handler for validate data against schema 
	 */
	public class MyValidationEventHandler implements ValidationEventHandler {
		//tried a personal validation handler
	    public boolean handleEvent(ValidationEvent event) {
	        System.out.println("\nEVENT");
	        System.out.println("SEVERITY:  " + event.getSeverity());
	        System.out.println("MESSAGE:  " + event.getMessage());
	        System.out.println("LINKED EXCEPTION:  " + event.getLinkedException());
	        System.out.println("LOCATOR");
	        System.out.println("    LINE NUMBER:  " + event.getLocator().getLineNumber());
	        System.out.println("    COLUMN NUMBER:  " + event.getLocator().getColumnNumber());
	        System.out.println("    OFFSET:  " + event.getLocator().getOffset());
	        System.out.println("    OBJECT:  " + event.getLocator().getObject());
	        System.out.println("    NODE:  " + event.getLocator().getNode());
	        System.out.println("    URL:  " + event.getLocator().getURL());
	        return true;
	    }
	}

}