package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.GeoMapApp;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new GeoMapApp.TopologicalGeoMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
		for(PointFeature pFeature : earthquakes) {
		   SimplePointMarker myMarker = createMarker(pFeature);
		   markers.add(myMarker);
		}
	    	map.addMarkers(markers);
	    }
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
    	int myColor = color(150, 150, 150);
    	float myRadius = 16.0f;
    	SimplePointMarker myMarker = new SimplePointMarker(feature.getLocation());
    	
		// finish implementing and use this method, if it helps.
    	Object magObj = feature.getProperty("magnitude");
    	float mag = Float.parseFloat(magObj.toString());
    	if(mag < THRESHOLD_LIGHT){
    		 myColor = color(0, 0, 255);
    		 myRadius = 8.0f;
    	}else if(mag < THRESHOLD_MODERATE){
    		 myColor = color(255, 255, 0);
    		 myRadius = 12.0f;
    	}else{
    		 myColor = color(255, 0, 0);
    	}

    	myMarker.setColor(myColor);
    	myMarker.setRadius(myRadius);
    	
    	return myMarker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		this.fill(200);
		this.rect(10, 50, 180, 300);
		this.fill(50);
		this.textSize(16);
		this.text("Earthquake Key", 25, 80);

		// First Entry
		this.fill(color(255, 0, 0));
		this.ellipse(33, 110, 16, 16);
		this.fill(50);
		this.textSize(12);
		this.text("5.0+ Magnitude", 50, 114);

		// Second Entry
		this.fill(color(255, 255, 0));
		this.ellipse(33, 150, 12, 12);
		this.fill(50);
		this.textSize(12);
		this.text("4.0+ Magnitude", 50, 154);

		// Third Entry
		this.fill(color(0, 0, 255));
		this.ellipse(33, 190, 8, 8);
		this.fill(50);
		this.textSize(12);
		this.text("Below 4.0", 50, 194);
	}
}
