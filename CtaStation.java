//Mohammad Umar
//12/10/2020
//                                                                            
// Its an object class that stores information about a Cta Station. It inherits from GeoLocation Class to store the  location.
// It has variables for the station name,  location,position on line and wheelchair access 

package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CtaStation extends GeoLocation {
	
	//Instance Variables from line 17-20
	private String name;
    private String location;
    private boolean wheelchair;
    private ArrayList<Integer> posLine = new ArrayList<Integer>();//0 red, 1 green, 2 blue, 3 brown, 4 purple, 5 pink, 6 orange .  used to determine position on lint
    //default constructor
    public CtaStation() {
        name = "";
        location = "";
        wheelchair = true;
        for (int i=0; i<posLine.size(); i++) {
            posLine.set(i, -1);
        }
    }
    //non default constructor
    public CtaStation(String name, String location, boolean wheelchair,
          double latitude, double longitude, int[] posLines) {
      super(latitude,longitude);
      this.name = name;
      this.location = location;
      this.wheelchair = wheelchair;
      for (int i=0; i<posLines.length; i++) {
          this.posLine.set(i, posLines[i]);
      }
  }
    // 2nd non default constructor with this time posline being passed as an arraylist
  public CtaStation(String name, String location, boolean wheelchair,
          double latitude, double longitude, ArrayList<Integer> posLine) {
      super(latitude,longitude);
      this.name = name;
      this.location = location;
      this.wheelchair = wheelchair;
      this.posLine = posLine;
  }
  // accessors and mutators for the variables from line 51-77
  public String getName() {
      return name;
  }
  public void setName(String name) {
      this.name = name;
  }
  
  public String getLocation() {
      return location;
  }
  public void setLocation(String location) {
      this.location = location;
  }
  
  public boolean isWheelchair() {
      return wheelchair;
  }
  public void setWheelchair(boolean wheelchair) {
      this.wheelchair = wheelchair;
  }
  
  public ArrayList<Integer> getPosLine() {
      return posLine;
  }
  public void setPosLine(ArrayList<Integer> posLine) {
      this.posLine = posLine;
  } 
//calcDistance method to calculate the distance between the current CtaStation and another CtaStation
public double calcDistance(CtaStation ctaStation) {
    return Math.sqrt(Math.pow( ctaStation.getLat() - this.getLat(),2)+Math.pow(ctaStation.getLng() - this.getLng(),2));
}
// defined toString method
public String toString() {
    String data = "";
    data += "Station name: " + this.getName() + "\n";
    data += "Latitude: " + this.getLat() + "\n";
    data += "Longitude: " + this.getLng() + "\n";
    data += "Station location : " + this.getLocation() + "\n";
    if (this.isWheelchair()) {
    	data += "Wheelchair present" + "\n";
    } else {
    	data += "No Wheelchair" + "\n";
    }
    return data;
}
// method that returns an string in form that would be needed when writing data to file
public String writeToFile() {
    String ReturnData = this.getName() + "," + this.getLat() + "," + this.getLng() + "," +
            this.getLocation() + "," + Boolean.toString(this.isWheelchair()).toUpperCase() + ",";
    for (int i = 0; i < this.getPosLine().size(); i++) {
    	ReturnData += this.getPosLine().get(i);
        if (i < this.getPosLine().size()-1) {
        	ReturnData += ",";
        }
    }
    return ReturnData;
}


// equals method to check if ctastation is equal or not

public boolean equals(CtaStation ctaStation) {
    if (ctaStation == null)
        return false;
    if (this.getLat() != ctaStation.getLat())
        return false;
    if (this.getLng() != ctaStation.getLng())
        return false;
    for (int i=0; i<getPosLine().size(); i++) {
        if (this.getPosLine().get(i) != ctaStation.getPosLine().get(i))
            return false;
    }
    return true;
}

// method used in main application. It reads over the file and extract the cta sttaions and store them in an array list and return the array listr
public static ArrayList<CtaStation> extractStations(String fileName) {
    boolean flag=true;
    do {
        try {
            File inputFile = new File(fileName);
            Scanner filereader = new Scanner(inputFile);
            
            //variable
            ArrayList<CtaStation> Stations = new ArrayList<CtaStation>();
            
            //Loop over all lines
            while (filereader.hasNextLine()) {
                String ef_data = filereader.nextLine();
                String[] e_data = ef_data.split(","); 
                //Create array of CTAStation
                if (!(e_data[0].equalsIgnoreCase("Name") ||
                		e_data[0].equalsIgnoreCase("Null"))) { //ignore first line
                    ArrayList<Integer> lines = new ArrayList<Integer>();
                    for (int i = 0; i < e_data.length-5; i++) {
                        String lineValue = e_data[5+i];
                        lines.add(Integer.parseInt(lineValue));
                    }
                    CtaStation currStation = new CtaStation(
                    		e_data[0],
                    		e_data[3],
                            Boolean.parseBoolean(e_data[4]),
                            Double.parseDouble(e_data[1]),
                            Double.parseDouble(e_data[2]),
                            lines
                            );
                    Stations.add(currStation);
                }
            }
            
            filereader.close();
            return Stations;
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found. Check to make sure the file name is correct.");
        }
    } while (flag);
    return new ArrayList<CtaStation>();
}

   
}
