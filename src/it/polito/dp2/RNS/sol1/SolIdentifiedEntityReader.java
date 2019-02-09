package it.polito.dp2.RNS.sol1;

import it.polito.dp2.RNS.IdentifiedEntityReader;


public class SolIdentifiedEntityReader implements IdentifiedEntityReader{

	// Defining private variable
	private String id; 
	
	// Constructor
	public SolIdentifiedEntityReader(String id) {
		// Initialize the id variable 
		this.id = id; 
	}

	@Override
	public String getId() {
		// Return the entity id 
		return id; 
	}

}
