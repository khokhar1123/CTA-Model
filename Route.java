//Mohammad Umar
//12/10/2020
// Route is an object class which contains information with respect to a CTA Line.It contains an ArrayList variable of CtaStations,  representation of the line as part of the CtaSystem, the  (color) of the line
package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Route {
 
	// instant variables from line 14-16
    private int colorIndex;//integer representation of line
    private String lineColor;//line color
    private ArrayList<CtaStation> ctaRoute; //array list of cta routes
    //default constructor
    public Route() {
        this.colorIndex = 0;
        this.lineColor = "";
        this.ctaRoute = new ArrayList<CtaStation>();
    }
    // non default constructor
    public Route(int colorIndex, String lineColor, ArrayList<CtaStation> ctaRoute) {
        this.colorIndex = colorIndex;
        this.lineColor = lineColor;
        this.ctaRoute = ctaRoute;
    }
    //method to check if  a station is on that line
    public boolean checkLine(CtaStation station) {
        for (CtaStation Station : ctaRoute) {
            if (Station.equals(station)) {
                return true;
            }
        }
        return false;
    }
// accessors and  mutators of variables from line 39-61
	public int getColorIndex() {
		return colorIndex;
	}

	public void setColorIndex(int colorIndex) {
		this.colorIndex = colorIndex;
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

	public ArrayList<CtaStation> getCtaRoute() {
		return ctaRoute;
	}

	public void setCtaRoute(ArrayList<CtaStation> ctaRoute) {
		this.ctaRoute = ctaRoute;
	}
	// defined to String  method
    public String toString() {
    	int count=0;
        String data = "Stations on the "+lineColor+" Line:\n";
        for (CtaStation currStation : ctaRoute) {
         
        	data += "("+count+") - "+currStation.toString()+"\n";
            count++;
         }
        return data;
     }
    //defined equals method to check if route is equal or not
     public boolean equals(Route ctaRoute) {
            boolean flag = true;
            if (ctaRoute.getColorIndex() != this.getColorIndex()) {
                flag=false;
            }
            if (!ctaRoute.getLineColor().equals(this.getLineColor())) {
                 flag=false;
            }
            for (int i = 0; i < this.ctaRoute.size(); i ++) {
                if (!ctaRoute.getCtaRoute().get(i).equals(this.getCtaRoute().get(i))) {
                    flag=false;
                }
            }
            return flag;
        }
     //it checks if the ctastation is at the end of line if it is it returns true else it returns false. 
     public boolean lastElement(CtaStation station) {
         int routePosition = station.getPosLine().get(this.colorIndex);
         if (routePosition == 0 || routePosition == ctaRoute.size()-1) {
             return true;
         }
         return false;
     }
     //used to add a station
     public boolean addStation(CtaStation stationAddition) {
         try {
             ctaRoute.add(stationAddition);
             return true;
         } catch (Exception e) {
             return false;
         }
     }
    // used to remove a station
     public boolean removeStation(CtaStation stationRemoval) {
         for (int i = 0; i < ctaRoute.size();i++) {
             if (ctaRoute.get(i).equals(stationRemoval)) {
                 ctaRoute.remove(i);
                 return true;
             }
         }
         return false;
     }
    // checks if the parameter name is equalt to any other station name and then returns all the stations with those name
     public CtaStation lookupStation(String searchName) {
         for (CtaStation currStation : this.getCtaRoute()) {
             if (searchName.equalsIgnoreCase(currStation.getName())) {
                 return currStation;
             }
         }
         return null;
     }
     //this methods searches the station using the position
     public CtaStation searchRoutePos(int Pos) {
         for (CtaStation Station : this.getCtaRoute()) {
             if (Station.getPosLine().get(this.getColorIndex()) == Pos) {
                 return Station;
             }
         }
         return null;
     }
     // method that displays the nearest station to the passed station.  uses the calcDistance method which returns the distance between two stations.
     public CtaStation nearestStation(GeoLocation location) {
         CtaStation min = ctaRoute.get(0);
         int counter =0;
         double minDistance = min.calcDistance(ctaRoute.get(0).getLat(), ctaRoute.get(0).getLng(),location.getLat(), location.getLng());// first one is set as min at start
         for (int i = 1; i < ctaRoute.size(); i++) { //loops over all 
             if (ctaRoute.get(i) != null) {
              double var = ctaRoute.get(i).calcDistance(ctaRoute.get(i).getLat(), ctaRoute.get(i).getLng(),location.getLat(), location.getLng());
              if (var<=minDistance) {
                  minDistance=var;
                  counter=i;
             }
         }
         }
         return ctaRoute.get(counter);
     }
     // method updates the route positions 
     public boolean updateIndex() {
         for (int i = 0; i < ctaRoute.size(); i++) {
             ArrayList<Integer> newdata = ctaRoute.get(i).getPosLine();
             newdata.set(colorIndex, i);
             ctaRoute.get(i).setPosLine(newdata);
         }
         return true;
     }
     // this method sorts the data. Selection sort has been used here
     public void sortdata() {
         for (int i = 0; i < ctaRoute.size()-1; i++) {
             int minval = i;
             for (int j = i+1; j < ctaRoute.size(); j++) {
                 if (ctaRoute.get(j).getPosLine().get(colorIndex) < 
                         ctaRoute.get(minval).getPosLine().get(colorIndex)) {
                     minval= j;
                 }
             }
             if (minval != i) {
                 ctaRoute.add(i,ctaRoute.get(minval));
                 ctaRoute.remove(minval+1);
             }
         }
     }
     // used in other classes to add a station between two stations
     public boolean addInBetween(CtaStation stationToAdd, CtaStation beforeStation, CtaStation aheadStation) {
         for (int i = 0; i < ctaRoute.size();i++) {
             if (ctaRoute.get(i).equals(beforeStation)) {
                 if (i != 0 && ctaRoute.get(i-1).equals(aheadStation)) {
                     ctaRoute.add(i,stationToAdd);
                     return true;
                 } else if (i != ctaRoute.size() && ctaRoute.get(i+1).equals(aheadStation)) {
                     ctaRoute.add(i+1,stationToAdd);
                     return true;
                 }
             }
         }
         return false;
     }
     // used in other class to add a station at end or start
     public boolean addEndStart(CtaStation station, CtaStation aheadStation) {
         for (int i = 0; i < ctaRoute.size();i++) {
             if (ctaRoute.get(i).equals(aheadStation)) {
                 if ( i == ctaRoute.size()-1 ) {//adds at end
                     ctaRoute.add(i+1,station);
                     return true;
                 }
                 if(i==0) {//adds at start
                	 ctaRoute.add(i,station);
                     return true;
                 }
             }
         }
         return false;
     }
     // this method is used in main class. It extracts all the data according to lines stores them in Arraylist. Works similarly to the exctract stations method in CtaStation.
     public static ArrayList<Route> extractRoutes(ArrayList<CtaStation> Stations, String filename) {
    	  try {
    	      // Read from file
    	int linecounter=0;
    	      File inputFile = new File(filename);
    	      Scanner filereader = new Scanner(inputFile);
    	      ArrayList<Route> Routes = new ArrayList<Route>();
    	      while (filereader.hasNextLine()) {
    	          String[] filedata = filereader.nextLine().split(","); // split at commas

    	              if (linecounter == 0) {
    	              for (int i = 0; i < filedata.length - 5; i++) {
    	                  Routes.add(new Route());
    	                  Routes.get(i).setColorIndex(i);
    	                  Routes.get(i).setLineColor(filedata[5+i].split(":")[0]);
    	              }
    	              for (Route c_Route : Routes) {
    	                  for (CtaStation c_Station : Stations) {
    	                      if (c_Station.getPosLine().get(c_Route.getColorIndex()) != -1) {
    	                          c_Route.addStation(c_Station);
    	                      }
    	                  }
    	                  c_Route.sortdata();// sorts the data during the process
    	              }

    	      }
    	           linecounter++;
    	     }    
    	      filereader.close();
    	      return Routes;
    	  } catch (FileNotFoundException e) {
    	      System.out.println("There is no such file.");
    	      return new ArrayList<Route>();
    	  }
    	}
     // method that displays the station that are on a specific line color and have specified wheelchair access.Color and access is passed as parameters
     public String wheelchairAndLineCheck(boolean wheelchairFlag,String color) {
         int count=0;
         String wheelchairString = "";
         if (wheelchairFlag==true && lineColor.equalsIgnoreCase(color)) {
             wheelchairString = "The following stations on  "+ lineColor +" Line have wheelchair access:\n";
         } else if (wheelchairFlag==true && lineColor.equalsIgnoreCase(color)) {
             wheelchairString = "The following stations on  "+lineColor+" Line do not have wheelchair access:\n";
         }
         for (CtaStation station : ctaRoute) {
             if (station.isWheelchair() == wheelchairFlag && lineColor.equalsIgnoreCase(color)) {
                 wheelchairString += station.toString()+"\n";
                 count++;
             }
         }
               if(count==0) {
            	   return "There is no such station";
               }
            	 return wheelchairString;
             }
         }


     
    
    
    
    

