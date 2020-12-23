//Mohammad Umar
//12/10/2020
//  GeoLocation is an object class that stores a location as a latitude and 
//  longitude. The calcDistanceMiles method,is the unique method to this class which calculates the distance to another geoLocation or latitude and  longitude pair




package project;

public class GeoLocation {

	protected double lng; //instance variable
	protected double lat; //instance variable
	
	//default constructor
	public GeoLocation() {
		lng=0;
		lat=0;
	}
	//non-default constructor
	public GeoLocation(double lat, double lng) {
		setLat(lat);
		setLng(lng);
	}
	//Accessors and mutators for latitude and longitude till line 45
	public void setLng(double lng) {
		if(verifyLng(lng)) { 
			this.lng=lng; 
		}	
	}
	
	public void setLat(double lat) {
		if(verifyLat(lat)) { 
			this.lat=lat; 
		}		
	}
	
	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}
	//to string method
	public String toString() {
		return "(" + lat + "," + lng +")";
	}
	//methods to check if latitude and longitude values are in range. till line 621
	public boolean verifyLat(double lat) {
		if (lat>= -90 && lat <= 90) {
			return true;
		}
		return false;
	}
	public boolean verifyLng(double lng) {
		if (lng>= -180 && lng <=180) {
			return true;
		}
		return false;
	}
	   //Uses distance formula to calculate the distance between two geolocations. used further in classes to check for nearest station
    public double calcDistance(GeoLocation location1, GeoLocation location2) {

        return Math.sqrt(Math.pow(location1.getLat()-location2.getLat(),2)+Math.pow(location1.getLng()-location2.getLng(),2));
    }

    //Uses distance formula to calculate the distance between two pairs of latitude and longitude
    public double calcDistance (double lat1, double lng1, double currentLat, double currentLong) {

        return Math.sqrt(Math.pow(lat1-currentLat,2)+Math.pow(lng1-currentLong,2));
    }
    
    //equals method 
    public boolean equals (GeoLocation location) {
        return (lat == location.getLat()) && (lng==location.getLng());
    }
}
