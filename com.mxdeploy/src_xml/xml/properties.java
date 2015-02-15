package xml;

import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.Property;

public class properties {
	private List<Property> properties = new ArrayList<Property>();
	
	public List<Property> getProperties() {
		return properties;
	}
	
	public void addProperties(Property property) {
		if( this.properties==null ){
			this.properties = new ArrayList<Property>();
		}
		this.properties.add(property);
	}	
}
