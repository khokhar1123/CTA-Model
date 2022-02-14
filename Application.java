//Mohammad Umar
//12/10/2020

// Application is the main application class which asks the user for choices and based on the user input, performs different tasks .It uses all other classes to accomplish this,mostly CtaCombined
package project;
 
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {

	public static void main(String[] args) {
		boolean wChoice;
      
		Scanner input=new Scanner(System.in);// scanner to take input from user
		String filename = "src/project/CTAStops.csv";
        ArrayList<CtaStation> allStops =CtaStation.extractStations(filename);// extracts all the stations and saves them in all Stops
        ArrayList<Route> allLines= Route.extractRoutes(allStops, filename);// extracts all the Routes and saves them in all Lines
        CtaCombined ctaCombined = new CtaCombined(allLines, allStops);// Passes all lines and all stops through ctaCombines
		boolean flag = true;
	
		//displays the choice menu to user 
		do {
			System.out.println("What do you want to do? Please enter the option number");
			System.out.println("1.Create a new station.");
			System.out.println("2.Modify an existing station.");
			System.out.println("3.Remove a station.");
			System.out.println("4.Search for a station.");
			System.out.println("5.Find the nearest Station.");
			System.out.println("6.Generate a Path.");
			System.out.println("7.Exit the Program");
			int choice=0;
			try {//catches exception on the choice number input
				choice=Integer.parseInt(input.nextLine());
			}catch(Exception e) {
			     System.out.println("");
			}
            switch (choice) {
            case 1 : addStation(ctaCombined,input); 
                     System.out.println("Do you want to save the changes to the CTAStops.csv File? Enter yes or no");
                     wChoice=CtaCombined.inBool(input);
                     if(wChoice) {
           	         writeFileData(ctaCombined,input);//asks if data needs to be saved to the file
                     }                     
                     break;
   
            case 2 : modifyStation(ctaCombined,input); 
                     System.out.println("Do you want to save the changes to the CTAStops.csv File? Enter yes or no");
                     wChoice=CtaCombined.inBool(input);
                     if(wChoice) {//asks if data needs to be saved to the file
                    	 writeFileData(ctaCombined,input);
                     }
            		 break;
            
            case 3 : deleteStation(ctaCombined,input);
                     System.out.println("Do you want to save the changes to the CTAStops.csv File? Enter yes or no");
                     wChoice=CtaCombined.inBool(input);
                     if(wChoice) {
  	                 writeFileData(ctaCombined,input);
                     }                     
                     break;
         
            case 4 : searchStation(ctaCombined,input);
                     break;
           
            case 5 : findNearestStation(ctaCombined,input);          
                     break;
            case 6 : createpath(ctaCombined,input);        
                     break;
            case 7 : System.out.println("Do you want to save the changes to the CTAStops.csv File? Enter yes or no");
                     wChoice=CtaCombined.inBool(input);
                     if(wChoice) {
  	                 writeFileData(ctaCombined,input);
                     } 
                     System.out.println("Bye.");                    
            	     flag=false;
                     break;
            default: System.out.println("That is not a valid choice. Please enter the choice number from 1-7");               
            }
		}while(flag);
	}
	//this method finds the nearest station to users location. latitude and longitude are taken as input.Creates an instance of geolocation and calls the nearest station method
public static void findNearestStation(CtaCombined ctaComb, Scanner input) {
        System.out.println("Enter the latitude: ");
        double userLatitude = CtaCombined.inDouble(input);
        System.out.println("Enter the longitude: ");
        double userLongitude = CtaCombined.inDouble(input);
        GeoLocation g1 = new GeoLocation(userLatitude, userLongitude);
        CtaStation nearestStation = ctaComb.nearestStation(g1);
        System.out.println("The nearest station to your entered location is  "+nearestStation.getName());
        input.nextLine();
    }
// used to delete a station. asks the user the station they wish to delete. if multiple stations with that name user is asked which station they meant and afterwards that station is removed.
public static void deleteStation(CtaCombined ctaComb, Scanner input) {
    CtaStation station= CtaCombined.lookStation(ctaComb, input);
    if (ctaComb.removeStation(station)) {
    	System.out.println("The station has been deleted");
    }else {
    	System.out.println("The station wasn't deleted.");
    }
}
// used to create a path between two stations. User enters the initial and final station name. Stations are looked up if their are multiple ones and then user specifes the correct station. It uses createPath method form CtaCombined
public static void createpath(CtaCombined ctaComb, Scanner input) {
    System.out.print("For the start station,");
    CtaStation initial_station = CtaCombined.lookStation(ctaComb, input);
    System.out.print("For the end station,");
    CtaStation destination = CtaCombined.lookStation(ctaComb, input);
    System.out.println(ctaComb.createPath(initial_station, destination));
}
// used to search for a station. gives user options on how to perform a search and then call respective methods to look up for stations as determined from CtaCombined class
public static void searchStation(CtaCombined ctaComb, Scanner input) {
		boolean flag=true;
		do {
			System.out.println("How do you want to perform the search. Enter the option number.");
			System.out.println("(1) Search by name");
			System.out.println("(2) Search for station with or without wheelchair access");
			System.out.println("(3) Search for station on a specific line color");
			System.out.println("(4) Search for station with or without wheelchair access on a specific line color.");
			System.out.println("(5) Exit the Search Menu and go back to the main menu.");
			int choice1=0;
			try {
				 choice1=Integer.parseInt(input.nextLine());
				}catch(Exception e) {
					System.out.println("");
				}

		switch (choice1) {
		// search by name
		case 1 : CtaStation stationToDisplay = CtaCombined.lookStationSearchOnly(ctaComb, input);
		         if(stationToDisplay!=null) {
	             System.out.println(stationToDisplay);
                 }
	             flag=false;
	             break;
	              
	    // search by wheelchair           
		case 2 : CtaStation stationToDisplay1 = CtaCombined.lookStationwc(ctaComb, input);
                 System.out.println(stationToDisplay1);
		         flag=false;
		         break;
	//search by line color	           
		case 3 : Route stationToDisplay3 = CtaCombined.lookStationLC(ctaComb, input);
                    if(stationToDisplay3!=null) {
		           System.out.println(stationToDisplay3);
                    }
                   flag=false;
                   break;
      //search by linecolor and wheelchair             
		case 4 : System.out.println("Please enter the line color: ");
		           String color=CtaCombined.inString(input);
		           System.out.println("Please enter true if you want to search for  stations that have wheelchair access on "+color+" line.");
		           System.out.println("Please enter false if you want to search for  stations that does have wheelchair access on "+color+" line.");
		           boolean access=CtaCombined.inBool(input);
		           System.out.println(ctaComb.onLineWc(access,color));
		           flag=false;
                   break;
		case 5 : System.out.println("You will be taken back to the main menu now.");
		         flag=false;
		         break;
       default: System.out.println("That is not a valid choice.");             
		         
		}
	}while(flag);	
	}
// used to modify stations. ask user what they need  to modify. sets the new values for that station
public static void modifyStation(CtaCombined ctaComb, Scanner input) {
	String name="";
	boolean wheelchair=true;
	String location="";
	double latitude=0;
	double longitude=0;
	boolean madeChange=false;
	System.out.print("For the station you wish to modify,");
	CtaStation station = CtaCombined.lookStation(ctaComb, input);
	System.out.println("Do u want to change the name for this station? Enter yes or no.");
    boolean choice=CtaCombined.inBool(input);
    if (choice) {
    	madeChange=true;
    	System.out.println("Enter the new name: ");
    	 name =CtaCombined.inString(input);
    }else {
    	 name = station.getName();
    }
    System.out.println("Do u want to change the wheelchair access for this station? Enter yes or no");
      choice=CtaCombined.inBool(input);
    if (choice) {
    	madeChange=true;
    	System.out.println("Does it has wheelchair access? Enter yes or no ");
    	boolean selection = CtaCombined.inBool(input);
    	if(selection) {
    		 wheelchair=true;
    	}else {
    		 wheelchair=false;
    	}
    }else {
    	 wheelchair = station.isWheelchair();
    }
    System.out.println("Do u want to change the location relative to ground?");
    choice=CtaCombined.inBool(input);
      if (choice) {
      	madeChange=true;
	System.out.println("What is the location relative to the ground? e.g. (elevated, subway, surface, embankment)");
	 location = input.nextLine();
      }else {
	location = station.getLocation();
}
    System.out.println("Do u want to change the latitude value for this station?");
    choice=CtaCombined.inBool(input);
    if (choice) {
    	madeChange=true;
     System.out.println("Enter the new latitude value");
    latitude = CtaCombined.inDouble(input);
    if(station.verifyLat(latitude)==false) {
    	do {
    		System.out.println("The latitude value is not in range,Please enter the value again");
    		latitude=CtaCombined.inDouble(input);
    	}while(!station.verifyLat(latitude));
    }
    }else {
     latitude = station.getLat();
}
    System.out.println("Do u want to change the longitude value for this station?");
    choice=CtaCombined.inBool(input);
    if (choice) {
    	madeChange=true;
     System.out.println("Enter the new longitude value");
     longitude = CtaCombined.inDouble(input);
     if(station.verifyLng(longitude)==false) {
    	
     	do {
     		System.out.println("The longitude value is not in range,Please enter the value again");
     		longitude=CtaCombined.inDouble(input);
     	}while(!station.verifyLng(longitude)==true);
     }
     }else {
    	longitude = station.getLng();
}
    
     
    station.setLat(latitude);
    station.setLng(longitude);
    station.setName(name);
    station.setLocation(location);
    station.setWheelchair(wheelchair);
    if(madeChange) {
    	    System.out.println("The station has been modified");
    System.out.println(station.toString());
    }else {
    	System.out.println("You did not make any change");
    }
    }

// used to add a station. Takes input for all CtaStation variables. Ask user for the line colors where the station is on and then on those lines determines where to insert station by asking user the stations that come before and after
public static void addStation(CtaCombined ctaComb, Scanner input) {
    CtaStation newStation = new CtaStation();
    System.out.println("Enter the name of a station to add:");
    String Name =CtaCombined.inString(input);
    System.out.println("Enter latitude value:");
    double  Lat = CtaCombined.inDouble(input);
    if(newStation.verifyLat(Lat)==false) {
    	do {
    		System.out.println("The latitude value is not in range,Please enter the value again");
    		Lat=CtaCombined.inDouble(input);
    	}while(!newStation.verifyLat(Lat));
    	}
    System.out.println("Enter longitude value:");
    double  Lng =CtaCombined.inDouble(input);
    if(newStation.verifyLng(Lng)==false) {
     	do {
     		System.out.println("The longitude value is not in range,Please enter the value again");
     		Lng=CtaCombined.inDouble(input);
     	}while(!newStation.verifyLng(Lng));
     }
    System.out.println("What is the location relative to the ground? e.g. (elevated, subway, surface, embankment)");
    String  Loc = CtaCombined.inString(input);
    System.out.println("Does it have the wheelchair access. " + "Enter yes or no.");
    boolean wc = CtaCombined.inBool(input);
    ArrayList<Integer> position = new ArrayList<Integer>();
    for (int i = 0; i < ctaComb.getCtaLines().size(); i++) {
    	position.add(-1);
    }
   newStation = new CtaStation(Name,Loc,wc,Lat,Lng,position); 
    ctaComb.add(newStation);
    //below it determines if its on a specific line
    for (int i = 0; i < ctaComb.getCtaLines().size(); i++) {
        System.out.println("Is the station on "+
        		ctaComb.getCtaLines().get(i).getLineColor()+" line?");
        boolean linecheck =CtaCombined.inBool(input);
        //determines where does the station occurs on the line
        if (linecheck) {
        	 boolean isOnLine = false;
        	 CtaStation prevStn = new CtaStation();
            do {
                System.out.println("What station comes before your station");
                prevStn = CtaCombined.lookStation(ctaComb, input);
                if (ctaComb.getCtaLines().get(i).checkLine(prevStn)) {
                	isOnLine = true;
                } else {
                    System.out.println(prevStn.getName()+
                            " is not on the "+ctaComb.getCtaLines().get(i).getLineColor()+" line. Please enter again.");
                }
            } while (!isOnLine);
            if (ctaComb.getCtaLines().get(i).lastElement(prevStn)) {
                CtaStation stationOne = null;
                if (prevStn.getPosLine().get(i) == 0) {
                	stationOne = ctaComb.getCtaLines().get(i)
                        .searchRoutePos(prevStn.getPosLine().get(i)+1);
                } else {
                	stationOne = ctaComb.getCtaLines().get(i)
                            .searchRoutePos(prevStn.getPosLine().get(i)-1);
                }
                System.out.println("Does your station come after \n(1) "+stationOne+" or: \n(2) is it at the start/end of the line?");
                boolean flag1or2 = true;
                do {
                    String Choice =CtaCombined.inString(input);
                    if (Choice.equals("1") ) {
                    	ctaComb.getCtaLines().get(i).addInBetween(newStation, prevStn, stationOne);
                    	flag1or2 = false;
                    } else if (Choice.equals("2")) {
                    	ctaComb.getCtaLines().get(i).addEndStart(newStation, prevStn);
                    	flag1or2 = false;
                    } else {
                        System.out.println("Incorrect input. Please try again..");
                    }
                } while (flag1or2);
            } else {
                CtaStation firststation = ctaComb.getCtaLines().get(i)
                        .searchRoutePos(prevStn.getPosLine().get(i)+1);
                CtaStation secondstation = ctaComb.getCtaLines().get(i)
                        .searchRoutePos(prevStn.getPosLine().get(i)-1);
                System.out.println("Which station comes after your station?  "+firststation.getName()+" or "+secondstation.getName()+"?");
                boolean flag = true;
                do {
                    String userChoice = CtaCombined.inString(input);
                    if (userChoice.equalsIgnoreCase(firststation.getName())) {
                    	ctaComb.getCtaLines().get(i).addInBetween(newStation, prevStn, firststation);
                    	flag = false;
                    } else if (userChoice.equalsIgnoreCase(secondstation.getName())) {
                    	ctaComb.getCtaLines().get(i).addInBetween(newStation, prevStn, secondstation);
                    	flag = false;
                    } else {
                        System.out.println("Incorrect input. Please try again..");
                    }
                } while (flag);
            }
            System.out.println("Station Added");
            ctaComb.updateIndex();
        }
    }
    }

// used to write data back to file. 
public static void writeFileData(CtaCombined ctaComb, Scanner input) {
        String fileName ="src/project/CTAStops2.csv"; // i changed the filename because i created a new one for testing purposes. Please change it to CTAStops.csv
        try {//catches IOexception
        	 FileWriter writer = new FileWriter(fileName);
        writer.write(ctaComb.writeFileData());// uses file writing method from CtaCombined
        writer.close();
        }catch(Exception e) {
        	System.out.println("An IO exception error occured.");
        }
       
    }

}


