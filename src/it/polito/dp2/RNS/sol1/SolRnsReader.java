package it.polito.dp2.RNS.sol1;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.RNS.ConnectionReader;
import it.polito.dp2.RNS.GateReader;
import it.polito.dp2.RNS.GateType;
import it.polito.dp2.RNS.ParkingAreaReader;
import it.polito.dp2.RNS.PlaceReader;
import it.polito.dp2.RNS.RoadSegmentReader;
import it.polito.dp2.RNS.VehicleReader;
import it.polito.dp2.RNS.VehicleState;
import it.polito.dp2.RNS.VehicleType;
import it.polito.dp2.RNS.sol1.jaxb.Localization;
import it.polito.dp2.RNS.sol1.jaxb.ParkingArea.Services;
import it.polito.dp2.RNS.sol1.jaxb.Rns;
import it.polito.dp2.RNS.sol1.jaxb.Rns.Entity;



public class SolRnsReader implements it.polito.dp2.RNS.RnsReader{
	
	// Defining variables, they are map that contains all the places and connections, identified by their ids 
	private Map<String, SolIdentifiedEntityReader> ids; 
	private Map<String, SolPlaceReader> places; 
	private Map<String, SolVehicleReader> vehicles; 
	private Map<String, SolGateReader> gates; 
	private Map<String, SolParkingAreaReader> parkingAreas;
	private Map<String, SolRoadSegmentReader> roadSegments; 
	private Set<SolConnectionReader> connections;
	
	
	public SolRnsReader(Rns rns) {
		// Initializing variables 
		this.ids = new HashMap<String, SolIdentifiedEntityReader>(); 
		this.places = new HashMap<String, SolPlaceReader>(); 
		this.vehicles = new HashMap<String, SolVehicleReader>();
		this.gates = new HashMap<String, SolGateReader>();
		this.parkingAreas = new HashMap<String, SolParkingAreaReader>();
		this.roadSegments = new HashMap<String, SolRoadSegmentReader>();
		this.connections = new HashSet<SolConnectionReader>(); 
		
		// First load places from the Rns system, but not their connected places, done after
		for (Entity e : rns.getEntity()){
			// Search for places 
			if(e.getPlaces() != null){
				// For each entity define a identified entity reader 
				SolIdentifiedEntityReader id = new SolIdentifiedEntityReader(e.getEntityID());
				// Put that entity into the IDs collection
				ids.put(id.getId(), id);
				// Define a new SolPlaceReader
				SolPlaceReader place = new SolPlaceReader(id, e);
				// Put that place inside the places collection
				places.put(id.getId(), place);
				if(e.getPlaces().getPlace().getGate()!=null){
					// Gate
					// If the place is a gate define a new SolGateReader and add it to gates collection
					SolGateReader gate = new SolGateReader(place, e.getPlaces().getPlace().getGate().name());
					gates.put(id.getId(), gate); 
				}else if(e.getPlaces().getPlace().getParkingArea()!=null){
					// Parking Area
					// If the place is a parking area define a new SolParkingAreaReader
					// and add it to parkingAreas collection
					Services s = e.getPlaces().getPlace().getParkingArea().getServices();
					SolParkingAreaReader parking = new SolParkingAreaReader(place, s);
					parkingAreas.put(id.getId(), parking); 
				}else{
					// Road Segment 
					// If the place is a Road Segment define a new SolRoadSegmentReader 
					// and add it to roadSegments collection
					String roadName = e.getPlaces().getPlace().getRoadSegment().getRoad().getRoadName();
					String roadSegmentName = e.getPlaces().getPlace().getRoadSegment().getRoadSegmentName(); 
					SolRoadSegmentReader roadS = new SolRoadSegmentReader(place, roadSegmentName, roadName); 
					roadSegments.put(id.getId(), roadS);
				}
			}
		}
		
		// Then after all the places are loaded, proceed to load vehicles 
		for(Entity e : rns.getEntity()){
			
			if(e.getVehicles()!=null){
				// Defining an entity for each vehicle 
				SolIdentifiedEntityReader id = new SolIdentifiedEntityReader(e.getEntityID());
				ids.put(id.getId(), id);
				// Define a localization variable
				Localization l = new Localization(); 
				l = e.getVehicles().getVehicle().getLocalization(); 
				SolPlaceReader o, d, p;
				// Get places corresponding to the localization ids
				o = places.get(l.getComesFrom()); 
				d = places.get(l.getDirectedTo()); 
				p = places.get(l.getPosition()); 
				// Get entry time
				XMLGregorianCalendar date = e.getVehicles().getVehicle().getEntryTime(); 
				String type = e.getVehicles().getVehicle().getVehicleType().name();
				String state = e.getVehicles().getVehicle().getVehicleState().name(); 
				// Creates a new SolVehicleReader and update the vehicles collection 
				SolVehicleReader vehicle = new SolVehicleReader(id, d, p, o, date, state, type); 
				vehicles.put(id.getId(), vehicle); 
			}
		}
		
		// Last load place connections
		for( Entity e : rns.getEntity()){
			if(e.getPlaces() != null){
				// Get entity id
				String id = e.getEntityID(); 
				// Get connection of that place
				List<String> conns = e.getPlaces().getPlace().getConnectedTo();
				// Define the from place, that is the place itself
				SolPlaceReader from;
				from = places.get(id);
				for(String str : conns){
					SolPlaceReader to; 
					to = places.get(str);
					// Add connection to from place
					from.addConnection(to); 
					// Add a connection element inside the connections collection
					SolConnectionReader conn = new SolConnectionReader(from, to);
					connections.add(conn); 
				}
			}
		}
		

	}
	
	
	
	
	@Override
	public Set<ConnectionReader> getConnections() {
		return new LinkedHashSet<ConnectionReader>(this.connections); 
	}

	

	@Override
	public Set<GateReader> getGates(GateType arg0) {
		// Gets readers for all the gates available in the RNS system with the given type
		// Returns a set of interfaces for reading the gates with the given type
		// or all the gates if arg0 is null 
		return new LinkedHashSet<GateReader>(this.gates
												.values()
												.stream()
												.filter(p -> p.hasType(arg0))
												.collect(Collectors.toSet()));
	}

	@Override
	public Set<ParkingAreaReader> getParkingAreas(Set<String> arg0) {
		// Gets readers for all the parking areas available in the RNS system having the specified services
		// Returns a set of interfaces for reading the parking areas with the specified services,
		// or all the parking areas if arg0 is null
		return new LinkedHashSet<ParkingAreaReader>(this.parkingAreas
														.values()
														.stream()
														.filter(p-> p.hasServices(arg0))
														.collect(Collectors.toSet())); 
	}

	@Override
	public PlaceReader getPlace(String arg0) {
		// Returns the place given its id or null if the place doesn't exist 
		return this.places.get(arg0); 
	}

	@Override
	public Set<PlaceReader> getPlaces(String arg0) {
		// Returns a set of interface for reading places that have  arg0 has prefix to their id
		// Returns all the places if arg0 is null
		return new LinkedHashSet<PlaceReader>(this.places
													.values()
													.stream()
													.filter(p -> p.hasPrefix(arg0))
													.collect(Collectors.toSet()));
	}

	@Override
	public Set<RoadSegmentReader> getRoadSegments(String arg0) {
		// Returns readers for all the road segments available in the RNS system belonging to the road with the given name
		// Returns all the road segments if arg0 is null
		return new LinkedHashSet<RoadSegmentReader>(this.roadSegments
														.values()
														.stream()
														.filter(p -> p.belongsToRoad(arg0))
														.collect(Collectors.toSet()));
	}

	@Override
	public VehicleReader getVehicle(String arg0) {
		// Returns the vehicle with the selected plateId
		// or null if it does not exist 
		return this.vehicles.get(arg0);
	}

	@Override
	public Set<VehicleReader> getVehicles(Calendar arg0, Set<VehicleType> arg1, VehicleState arg2) {
		// Gets readers for a selection of all the vehicles that are in the RNS system. The selection can be made according to 
		// a number of parameters: - the entrance date and time since when vehicles have to be selected -
		// the types of vehicles to be selected - the states of vehicles to be selected
		// all of these parameter can be null to select all the vehicles for any entrance time, any state or any type.
		return new LinkedHashSet<VehicleReader>(this.vehicles
													.values()
													.stream()
													.filter(p1 -> p1.entranceSince(arg0))
													.filter(p2 -> p2.hasType(arg1))
													.filter(p3 -> p3.hasState(arg2))
													.collect(Collectors.toSet())); 		
	} 
	
}
