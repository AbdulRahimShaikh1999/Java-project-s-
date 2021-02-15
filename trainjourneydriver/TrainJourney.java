
package trainjourneydriver;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


class TrainJourney implements Journey {
    // Using ArrayList of String arrays to represent table data because tabular 
    // data easy to access through index. Disadvantage is more type conversions.
    ArrayList<String[]> trainTable = new ArrayList();
    DateTimeFormatter timeFormatter;
    public TrainJourney() {
        this.trainTable.add(new String[] {"Vancouver", "n/a", "n/a", "20:30", "", "1"});
        this.trainTable.add(new String[] {"Kamloops", "06:00", "", "06:35", "", "2"});
        this.trainTable.add(new String[] {"Jasper\t", "16:00", "", "17:30", "", "2"});
        this.trainTable.add(new String[] {"Edmonton", "23:00", "", "23:59", "", "2"});
        this.trainTable.add(new String[] {"Saskatoon", "08:00", "", "08:25", "", "3"});
        this.trainTable.add(new String[] {"Winnipeg", "20:45", "", "22:30", "", "3"});
        this.trainTable.add(new String[] {"Sioux Lookout", "05:02", "", "05:42", "", "4"});
        this.trainTable.add(new String[] {"Hornepayne", "15:35", "", "16:10", "", "4"});
        this.trainTable.add(new String[] {"Capreol\t", "00:18", "", "00:48", "", "5"});
        this.trainTable.add(new String[] {"Toronto\t", "09:30", "", "20:30", "", "5"});
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); 
    }

    public void show() {
        System.out.println("Station\t\tScheduled Arrival\tActual Arrival\tScheduled Departure\tActual Departure\tDay");
        for (String[] row : this.trainTable) {
            for (int i = 0; i < row.length ; i++){
                // Formatting tabs
                System.out.print(row[i] + "\t");
                if (i == 1) System.out.print("\t\t");
                if (i == 2) System.out.print("\t");
                if (i == 3) System.out.print("\t\t");
                if (i == 4) System.out.print("\t\t");
            }
            System.out.println("");
        }       
    }
    
    public void left(String station, String time) {
        // Time input
        try {
            this.timeFormatter.parse(time);
        } catch (DateTimeParseException e) {
            System.out.println("Error with input time");
            return;
        }
        
        // Station input
        // Flag for when station has been found
        Boolean stationFound = false;
        int hourDelta = 0;
        int minuteDelta = 0;
        for (int i = 0; i < this.trainTable.size(); i++) {
            if (this.trainTable.get(i)[0].replace("\t", "").equals(station) && "".equals(this.trainTable.get(i)[4])) {
                this.trainTable.get(i)[4] = time;
                // Flip stationFound flag
                stationFound = true;
                // Get the difference between scheduled and actual times
                int[] delta = this.getTimeDifference(this.trainTable.get(i)[3], this.trainTable.get(i)[4]);
                hourDelta = delta[0];
                minuteDelta = delta[1];
                // Short circuit so that time difference is only applied to subsequent stations
                continue;
            }
            // Update scheduled times for all sebsequent stations
            if (stationFound == true) {
                // Convert scheduled time Strings to ints
                int scheduledArrivalHour = Integer.parseInt(this.trainTable.get(i)[1].substring(0,2));
                int scheduledArrivalMinutes = Integer.parseInt(this.trainTable.get(i)[1].substring(3, 5));       
                int scheduledDepartureHour = Integer.parseInt(this.trainTable.get(i)[3].substring(0,2));
                int scheduledDepartureMinutes = Integer.parseInt(this.trainTable.get(i)[3].substring(3, 5));
                
                // Keep track of hours added due to minutes exceeding 60
                int arrivalHoursAddedFromMinutes = (int) Math.floor((scheduledArrivalMinutes + minuteDelta) / (double)60);
                int depatureHoursAddedFromMinutes = (int) Math.floor((scheduledDepartureMinutes + minuteDelta) / (double)60);                

                // Calculate new time using modulus 24 for hours and modulus 60 for minutes
                int newScheduledArrivalHour = Math.abs((scheduledArrivalHour + hourDelta + arrivalHoursAddedFromMinutes)%24);
                int newScheduledArrivalMinutes = Math.abs((scheduledArrivalMinutes + minuteDelta)%60);       
                int newScheduledDepartureHour = Math.abs((scheduledDepartureHour + hourDelta + depatureHoursAddedFromMinutes)%24);
                int newScheduledDepartureMinutes = Math.abs((scheduledDepartureMinutes + minuteDelta)%60);
                          
                // Format to HH:mm 
                String stringNewScheduledArrivalHour = String.valueOf(newScheduledArrivalHour);
                if (stringNewScheduledArrivalHour.length() == 1) stringNewScheduledArrivalHour = "0" + stringNewScheduledArrivalHour;
                String stringNewScheduledArrivalMinutes = String.valueOf(newScheduledArrivalMinutes);
                if (stringNewScheduledArrivalMinutes.length() == 1) stringNewScheduledArrivalMinutes = "0" + stringNewScheduledArrivalMinutes;
                String stringNewScheduledDepartureHour = String.valueOf(newScheduledDepartureHour);
                if (stringNewScheduledDepartureHour.length() == 1) stringNewScheduledDepartureHour = "0" + stringNewScheduledDepartureHour;
                String stringNewScheduledDepartureMinutes = String.valueOf(newScheduledDepartureMinutes);
                if (stringNewScheduledDepartureMinutes.length() == 1) stringNewScheduledDepartureMinutes = "0" + stringNewScheduledDepartureMinutes;
                
                // Update data
                this.trainTable.get(i)[1] = stringNewScheduledArrivalHour + ":" + stringNewScheduledArrivalMinutes;
                this.trainTable.get(i)[3] = stringNewScheduledDepartureHour + ":" + stringNewScheduledDepartureMinutes;   
            }
        }
        if (stationFound == false) System.out.println("Station input not found");
    }
    
    public void arrived(String station, String time) {
        // Time input
        try {
            this.timeFormatter.parse(time);
        } catch (DateTimeParseException e) {
            System.out.println("Error with input time");
            return;
        }

        
        // station input
        // Find expected desintation station
        String expectedDestination = "";        
        for (int i = 0; i < this.trainTable.size(); i++) {
            if ("".equals(this.trainTable.get(i)[2])) {
                expectedDestination = this.trainTable.get(i)[0].replace("\t", "");
                break;
            }
        }
        // Check if user is trying to arrive at a station before leaving previous stations
        for (int i = 0; i < this.trainTable.size(); i++) {
            if (!this.trainTable.get(i)[0].replace("\t", "").equals(expectedDestination) && "".equals(this.trainTable.get(i)[4])) {
                    System.out.println("can't arrive at station until leaving previous stations");
                    break;
            }
            if (this.trainTable.get(i)[0].replace("\t", "").equals(expectedDestination)) {
                this.trainTable.get(i)[2] = time;
                break;
            }
        }
    }

    public void quit() {
        System.out.println("exiting program");
        System.exit(0);
    }
    
    private int[] getTimeDifference(String scheduled, String actual) {
        int scheduledHour = Integer.parseInt(scheduled.substring(0,2));
        int scheduledMinutes = Integer.parseInt(scheduled.substring(3,5));
        int actualHour = Integer.parseInt(actual.substring(0,2));
        int actualMinutes = Integer.parseInt(actual.substring(3,5));

        return new int[] {actualHour - scheduledHour, actualMinutes - scheduledMinutes};
    }
    
}
