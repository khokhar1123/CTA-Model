//Mohammad Umar
//12/10/2020
//CtaCombined is an object class which stores all the stations as an ArrayList, and all the data respective to Lines as  ArrayList.
// Mostly it involves looping through the CTA Lines, calling Route methods  to avoid the need to
// do this in the Application class, it is created to have minimal code in application class and make things more clean

package project;


import java.util.ArrayList;
import java.util.Scanner;


public class CtaCombined {
    //instance variabless
    private ArrayList<CtaStation> CtaStops;// all stations
    private  ArrayList<CtaStation> Stops;//stops transfers
    private  ArrayList<Route> Lines;// all lines data
	//default constructor
    public CtaCombined() {
        Lines = new ArrayList<Route>();
        CtaStops = new ArrayList<CtaStation>();
        Stops = new ArrayList<CtaStation>();
    }
// non default constructor
    public CtaCombined(ArrayList<Route> Lines, ArrayList<CtaStation> ctaStops) {
        this.Lines= Lines;
        
        
        this.CtaStops = ctaStops;
        this.Stops=addStopsData();
    }
//mutators and accessors form line 35-55
    public ArrayList<Route> getCtaLines() {
        return Lines;
    }
    public void setCtaLines(ArrayList<Route> Lines) {
        this.Lines = Lines;
    }

    public ArrayList<CtaStation> getCtaStops() {
        return CtaStops;
    }
    public void setCtaStops(ArrayList<CtaStation> ctaStops) {
        this.CtaStops = ctaStops;
    }
    
    public ArrayList<CtaStation> getStops() {
        return Stops;
    }
    public void setStops(ArrayList<CtaStation> Stops) {
        this.Stops = Stops;
    }
    //defined to String mehtod
    public String toString() {
        String returndata = "";
        for (Route line : Lines) {
            returndata += line.toString() + "\n";
        }
        return returndata;
    }
    //used to determine the nearest station when a geolocation is passed. calls the neareststation from Route and loops over all lines data 
    public CtaStation nearestStation(GeoLocation userGeoLocation) {
        Route nearestStations = new Route();
        for (Route line : Lines) {
            nearestStations.addStation(line.nearestStation(userGeoLocation));
        }
        return nearestStations.nearestStation(userGeoLocation);
    }
    // it returns all the lines on which a station is present
    public ArrayList<String> getLines(CtaStation ctaStation) {
        boolean flag=true;
        ArrayList<String> lines = new ArrayList<String>();
        for (Route line : Lines) {
            if (ctaStation.getPosLine().get(line.getColorIndex()) != -1) {
                flag = false;
                lines.add(line.getLineColor());
            }
        }
        if (!flag) {
            return lines;
        } else {
            return null;
        }
    }
    
    // method similar to the one in  ctaStation. Used to write data in file. Returns a string in manner that writes the data in file format
    public String writeFileData() {
        String returndata = "Name,Latitude,Longitude,Description,Wheelchair,";
        for (int i = 0; i < Lines.size(); i++) {
        	returndata += Lines.get(i).getLineColor().substring(0, 1).toUpperCase() +
            		Lines.get(i).getLineColor().substring(1) + ":" + Lines.get(i).getCtaRoute().size();
            if (i < Lines.size()-1) {
            	returndata += ",";
            } else {
            	returndata += "\n";
            }
        }
        for (int i = 0; i < CtaStops.size(); i++) {
        	returndata += CtaStops.get(i).writeToFile();
            if (i < CtaStops.size()-1) {
            	returndata += "\n";
            }
        }
        return returndata;
}
    // used to collects the transfers data
  public ArrayList<CtaStation> addStopsData() {
        ArrayList<CtaStation> transfers = new ArrayList<>();
        for (CtaStation station : CtaStops) {
            int c;
            c=0;
//            System.out.println(station.getName());
            for (int j = 0; j < Lines.size(); j++) {
                if (station.getPosLine().get(j) != -1) {
                    c++;
                }
            }
            if (c > 1) {
//            	System.out.println(station.getName());
            	transfers.add(station);
            }
        }
        return transfers;
    }
  // method updates the route positions 
    public boolean updateIndex() {
        for (Route line : Lines) {
            line.updateIndex();
        }
        return true;
    }
    // defined equals method . returns true if parameter equals the current values else return false.
    public boolean equals(CtaCombined ctacombo) {
        boolean flag=true;
        if (!ctacombo.getCtaLines().equals(this.getCtaLines())) {
            return flag=false;
        }
        for (int i = 0; i < ctacombo.getCtaStops().size(); i++) {
            if (!ctacombo.getCtaStops().get(i).equals(this.getCtaStops().get(i))) {
                return flag=false;
            }
        }
        return flag;
    }
     // looks up for the stations which have the same name as the passed parameter and it returns the station that are found as array list
        public ArrayList<CtaStation> lookupStation(String stationname) {
        ArrayList<CtaStation> extractedData = new ArrayList<CtaStation>();
        boolean flag = true;
        for (CtaStation stop : CtaStops) {
            if (stationname.equalsIgnoreCase(stop.getName())) {
                extractedData.add(stop);
                flag = false;
            }
        }
        if (!flag) {
            return extractedData;
        } else {
            return null;
        }
        }
        // loooks up for stations that are on the color of line that is passed in parameter. it returns the station that are found as array list
        public ArrayList<Route> lookupStationLC(String color) {
        ArrayList<Route> extractedData = new ArrayList<Route>();
        boolean flag = true;
        for (Route stop : Lines) {
            if (stop.getLineColor().equalsIgnoreCase(color)) {
                extractedData.add(stop);
                flag = false;
            }
        }
        if (!flag) {
            return extractedData;
        } else {
            return null;
        }
        }
        // used lookupStation. It prompts the user for a name and looks up for all the stations with those names and displays the information for found one and asks the uers to select which one they meant
        public static CtaStation lookStation(CtaCombined ctaComb, Scanner input) {
            boolean flag = false;
            do {
                System.out.println("Enter the name of station : ");
                String data = inString(input);
                ArrayList<CtaStation> foundStations = ctaComb.lookupStation(data);
                if (foundStations == null) {
                    System.out.println("There is no station with this name");
                } else {
                    if (foundStations.size() ==1 ) {
                        return foundStations.get(0);
                    } else {
                        System.out.println("There are multiple stations with this name ");
                        boolean entered = false;
                        do {
                            for (int i = 0; i < foundStations.size(); i++) {
                                System.out.println(i+"- " + foundStations.get(i).toString()
                                + "Its on the " + ctaComb.getLines(foundStations.get(i)) + " line");
                            }
                            System.out.println("Which station do you mean? (Enter the choice in number)");
                            int choice = integerInput(input);
                            try {
                                foundStations.get(choice);
                                return foundStations.get(choice);
                            } catch (Exception e) {
                                System.out.println("Invalid selection. Please try again.");
                            }
                        } while (!entered);
                    }
                }
            } while (flag != true);
            return null;
        }
        //this method is the same as the previous but it designed for the search method option 1 in the application. The difference is when a station is not found it exits.
        public static CtaStation lookStationSearchOnly(CtaCombined ctaComb, Scanner input) {
            boolean flag = false;
            do {
                System.out.println("Enter the name of station : ");
                String data = inString(input);
                ArrayList<CtaStation> foundStations = ctaComb.lookupStation(data);
                if (foundStations == null) {
                    System.out.println("There is no station with this name");
                    flag=true;
                } else {
                    if (foundStations.size() ==1 ) {
                        return foundStations.get(0);
                    } else {
                        System.out.println("There are multiple stations with this name ");
                        boolean entered = false;
                        do {
                            for (int i = 0; i < foundStations.size(); i++) {
                                System.out.println(i+"- " + foundStations.get(i).toString()
                                + "Its on the " + ctaComb.getLines(foundStations.get(i)) + " line");
                            }
                            System.out.println("Which station do you mean? (Enter the choice in number)");
                            int choice = integerInput(input);
                            try {
                                foundStations.get(choice);
                                return foundStations.get(choice);
                            } catch (Exception e) {
                                System.out.println("Invalid selection. Please try again.");
                            }
                        } while (!entered);
                    }
                }
            } while (flag != true);
            return null;
        }
        
//     used lookupStationLC. It prompts the user for a color of line and looks up for all the stations with those line color 
       
        public static Route lookStationLC(CtaCombined ctaComb, Scanner input) {
            boolean flag = false;
            do {
                System.out.println("Enter the color of line : ");
                String data = inString(input);
                ArrayList<Route> foundStations = ctaComb.lookupStationLC(data);
                if (foundStations == null) {
                    System.out.println("There is no station on this line color");
                    System.out.println("Enter 'false' to search for stations on some other line  or enter 'true' if you wish to exit?");
                    flag=Boolean.parseBoolean(input.nextLine());
                } else {
                    if (foundStations.size() ==1 ) {
                        return foundStations.get(0);
                    } else {
                    	boolean entered = false;
                        do {
                            for (Route foundStation : foundStations) {
                                return (foundStation);
                            }
                            
                        } while (!entered);
                    }
                }
            } while (flag != true);
            return null;
        }
// looks for station that have wheelchair or not determined in the passed parameter and returns all the found stations
        public ArrayList<CtaStation> lookupStationWc(boolean wc) {
        ArrayList<CtaStation> extractedData = new ArrayList<CtaStation>();
        boolean flag = true;
        for (CtaStation stop : CtaStops) {
            if (stop.isWheelchair()==wc) {
            	extractedData.add(stop);
                flag = false;
            }
        }
        if (!flag) {
            return extractedData;
        } else {
            return null;
        }
        }
        // displays all the stations with either wheelchair or without wheelchair detrmined by the user and asks them which one they meants
        public static CtaStation lookStationwc(CtaCombined ctaComb, Scanner input) {
            boolean flag = false;
            do {
                System.out.println("Do you want to search stations with wheelchair or without wheelchair? (Enter 'true' for with wheelchair and 'false' for without wheelchair. ");
                boolean data = inBool(input);
                
                ArrayList<CtaStation> foundStations = ctaComb.lookupStationWc(data);
                if (foundStations == null) {
                    System.out.println("There is no station with this wheelchair");
                    flag=true;
                } else {
                    if (foundStations.size() ==1 ) {
                       	return foundStations.get(0);
                        
                    } else {
                        System.out.println("There are multiple stations with wheelchair");
                        boolean entered = false;
                        do {
                            for (int i = 0; i < foundStations.size(); i++) {
                                System.out.println(i+"- " + foundStations.get(i).toString()
                                + "Its on the " + ctaComb.getLines(foundStations.get(i)) + " line");
                            }
                            System.out.println("Which station do you mean? (Enter the choice in number)");
                            int choice = integerInput(input);
                            try {
                                foundStations.get(choice);
                                return foundStations.get(choice);
                            } catch (Exception e) {
                                System.out.println("Invalid selection. Please try again.");
                            }
                        } while (!entered);
                    }
                }
            } while (flag != true);
            return null;
        }
        
        
// adds a station
        public boolean add(CtaStation ctaStation) {
            CtaStops.add(ctaStation);
            return true;
        }
        //deletes a station
        public boolean removeStation(CtaStation station) {
            boolean flag=false;
            for (Route line : Lines) {
                if (line.removeStation(station)) {
                    flag = true;
                }
                for (int i = 0; i < CtaStops.size(); i++) {
                    if (CtaStops.get(i).equals(station)) {
                        CtaStops.remove(i);
                        flag = true;
                    }
                }
                updateIndex();
            }
            return flag;
        }
        //checks if two stations are on a common line/roue
        public  int commonRoute(CtaStation start, CtaStation end) {
            for (int i = 0; i < Lines.size(); i++) {
                if (start.getPosLine().get(i) != -1 && end.getPosLine().get(i) != -1) {
                    return i;
                }
            }
            return -1;
        }
       //  used to generate route between two stations
        public  String createPath(CtaStation start, CtaStation end) {
        	String Stringdata="";
            int cLine = commonRoute(start, end); //check if on same line. for the ones on sameline uses this part to generate route
            if (cLine != -1) {
            	Stringdata="\nTake the " + Lines.get(cLine).getLineColor() + 
                        " line from " + start.toString() + " to " + end.toString();

            	return Stringdata;
            } else  { 
               // code that gets executed when there is a single transfer between two stations
           
                for (CtaStation tStation : Stops) {
        
                    int sharedIndex = commonRoute(start, tStation);
                    if (commonRoute(start, tStation) != -1 &&
                            !tStation.equals(start) &&
                            commonRoute(tStation, end) != -1) {
                    	Stringdata= "\nTake the " + Lines.get(sharedIndex).getLineColor() + 
                                " line from " + start.toString() + " to " + tStation.toString() +
                               createPath(tStation, end);
                    }
                }
	      }
//            return "\n Sorry an error occurred";
			return Stringdata;

        }    
        // returns the station on a specific line color with a specific wheelchair access
        public String onLineWc(boolean wheelchairFlag,String color) {
            String stringReturn = "";
            for (Route line : Lines) {
            	if(!(line.wheelchairAndLineCheck(wheelchairFlag,color).equals("There is no such station"))) {
                stringReturn += line.wheelchairAndLineCheck(wheelchairFlag,color) + "\n";
            }
            }
            return stringReturn;
        }
        
     // below are four methods created to catch input exception for a specific data types from 
        public static String inString(Scanner input) {
            boolean flag = false;
            do {
                try {
                    String entry = input.nextLine();
                    return entry;
                } catch (Exception e) {
                    System.out.println("Incorrect input. Please try again.");
                }
            } while (!flag);
            return null;
        }

        public static double inDouble(Scanner input) {
            boolean flag = false;
            do {
                try {
                    double entry =Double.parseDouble( input.nextLine());
                    return entry;
                } catch (Exception e) {
        
                    System.out.println("Incorrect input. Please try again.");
                }
            } while (!flag);
            return 0;
        }

        public static int integerInput(Scanner input) {
            boolean flag = false;
            do {
                try {
                	int integer = Integer.parseInt(input.nextLine());
                    return integer;
                } catch (Exception e) {
               
                    System.out.println("That is not a valid input. Try again.");
                }
            } while (!flag);
            return 0;
        }
        
public static boolean inBool(Scanner input) {
    boolean flag = false;
    do {
        try {
            String entry = inString(input);
            if(entry.equalsIgnoreCase("yes")||entry.equalsIgnoreCase("y")||entry.equalsIgnoreCase("true")) {
            	return true;
            }else if(entry.equalsIgnoreCase("no")||entry.equalsIgnoreCase("n")||entry.equalsIgnoreCase("false")) {
            	return false;
            }else {
            	System.out.println("Invalid input. Please try again.");
            }
            
            
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    } while (!flag);
    return false;
}
        
        
        
        
        
        
        
}