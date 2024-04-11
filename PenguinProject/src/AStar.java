import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;

public class AStar implements PathingStrategy {
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> path = new LinkedList<>();
        HashMap<Point, Node> closedMap = new HashMap<>(); /* HashMap<Point position, current Node> */
        HashMap<Point, Node> openMap = new HashMap<>(); /* HashMap<Point position, Integer f> */
        Queue<Node> openList = new PriorityQueue<>();

        // Step 2: create start node, add to both open lists, mark as current
        Node current = new Node(start, 0, start, end, null);
        openMap.put(current.getPoint(), current);
        openList.add(current);

        do {

            // Step 3. create list of valid neighbors from potential neighbors
            List<Point> neighbors = potentialNeighbors.apply(current.getPoint())
                    .filter(canPassThrough)
                    .filter(pt -> !pt.equals(start) && !pt.equals(end))
                    .collect(Collectors.toList());

            // Step 3. go through each neighbor and add to open list if not there
            for (Point neighbor : neighbors) {
                // make sure node hasn't been used already
                if (!closedMap.containsKey(neighbor)) {
                    // step 2b. determine g value
                    int g = current.getG() + 1;
                    // step 2a. add to open list if not already in it
                    if (openMap.containsKey(neighbor)) {
                        // step 3c. if neighbor already in list, compare g values
                        if (g < openMap.get(neighbor).getF()) {
                            openList.remove(openMap.get(neighbor)); // remove from sorted list
                            openMap.get(neighbor).setG(g);          // update g value
                            openList.add(openMap.get(neighbor));    // add to openList again
                        }
                    }
                    else {
                        // if not in openList already, add it
                        Node n = new Node(neighbor, g, start, end, current);
                        openMap.put(neighbor, n);
                        openList.add(n);
                    }
                }
            }
            // move current node to closed list
            closedMap.put(current.getPoint(), current);

            if (openList.isEmpty()) {
                return new LinkedList<>();
            }
            // get next node with smallest f value
            current = openList.remove();
        } while (!withinReach.test(current.getPoint(), end));

        // get path
        Node temp = current;

        while (temp.getPoint() != start) {
            path.add(0, temp.getPoint());
            temp = current.getPrior();
            current = temp;
        }

        return path;
    }
}
