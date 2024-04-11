import java.lang.Math;
public class Node implements Comparable<Node>{
    private Point current;
    private Point start;
    private Point goal;
    private int f, g, h = 0;
    private Node prior = null;

    public Node(Point current, int g, Point start, Point goal, Node prior) {
        this.current = current;
        this.start = start;
        this.goal = goal;
        this.h = getH();
        this.g = g;
        this.f = g + h;
        this.prior = prior;
    }
    public int compareTo(Node other) {
        return (getF() - other.getF());
    }
    private int getH() {
        return (Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY()));
    }
    public int getG() {
        return g;
    }

    public void setG(int g){
        this.g = g;
    }

    public Point getPoint() {
        return current;
    }
    public int getF() {
        return this.f;
    }

    public Node getPrior() {
        return this.prior;
    }
}
