
package trainjourneydriver;

public class TrainJourneyDriver {

    public static void main(String[] args) {
        TrainJourney newJourney = new TrainJourney();
        
        newJourney.show();
        
        newJourney.left("Vancouver", "20:33");
        newJourney.arrived("Kamloops", "06:15");
        newJourney.left("Kamloops", "06:56");
        newJourney.arrived("Jasper", "15:59");
        newJourney.left("Jasper", "17:35");
        newJourney.arrived("Edmonton", "23:15");
        newJourney.left("Edmonton", "00:00");
        newJourney.arrived("Saskatoon", "08:00");
        newJourney.left("Saskatoon", "08:25");
        newJourney.arrived("Winnipeg", "20:00");
        newJourney.left("Winnipeg", "22:30");
        newJourney.arrived("Sioux Lookout", "04:55");
        newJourney.left("Sioux Lookout", "05:00");
        newJourney.arrived("Hornepayne", "15:00");
        newJourney.left("Hornepayne", "16:20");
        newJourney.arrived("Capreol", "00:30");
        newJourney.left("Capreol", "02:00");
        newJourney.arrived("Toronto", "10:00");
        newJourney.left("Toronto", "11:00");
        
        newJourney.show();
        
        newJourney.quit();
    }
    
}
