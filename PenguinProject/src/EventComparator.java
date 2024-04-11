import java.util.Comparator;

public class EventComparator implements Comparator<Event>
{
    public int compare(Event lft, Event rht) {
        return (int)(lft.getTime() - rht.getTime());
    }
}
