
package trainjourneydriver;

public interface Journey {
    public void show();
    public void arrived(String station, String time);
    public void left(String station, String time);
    public void quit();
}
