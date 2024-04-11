import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public class WorldModel
{
    private int numRows;
    private int numCols;
    private final Background[][] background;
    private final Entity[][] occupancy;
    private Set<Entity> entities;

    private static final int PROPERTY_KEY = 0;
    private static final String IGLOO_KEY = "igloo";
    private static final int IGLOO_NUM_PROPERTIES = 4;
    private static final int IGLOO_ID = 1;
    private static final int IGLOO_COL = 2;
    private static final int IGLOO_ROW = 3;

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 5;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;
    private static final int OBSTACLE_ANIMATION_PERIOD = 4;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final String PLAYER_KEY = "penguin";
    private static final int PLAYER_NUM_PROPERTIES = 7;
    private static final int PLAYER_ID = 1;
    private static final int PLAYER_COL = 2;
    private static final int PLAYER_ROW = 3;
    private static final int PLAYER_ACTION_PERIOD = 5;
    private static final int PLAYER_ANIMATION_PERIOD = 6;

    private static final String BIRD_KEY = "bird";
    private static final int BIRD_NUM_PROPERTIES = 6;
    private static final int BIRD_ID = 1;
    private static final int BIRD_COL = 2;
    private static final int BIRD_ROW = 3;
    private static final int BIRD_ANIMATION_PERIOD = 4;
    private static final int BIRD_ACTION_PERIOD = 5;

    private static final String FISH_KEY = "fish";
    private static final int FISH_NUM_PROPERTIES = 6;
    private static final int FISH_ID = 1;
    private static final int FISH_COL = 2;
    private static final int FISH_ROW = 3;
    private static final int FISH_ANIMATION_PERIOD = 4;
    private static final int FISH_ACTION_PERIOD = 5;

    private static final String SHARK_KEY = "shark";
    private static final int SHARK_NUM_PROPERTIES = 6;
    private static final int SHARK_ID = 1;
    private static final int SHARK_COL = 2;
    private static final int SHARK_ROW = 3;
    private static final int SHARK_ANIMATION_PERIOD = 4;
    private static final int SHARK_ACTION_PERIOD = 5;

    private static final String TREE_KEY = "tree";
    private static final int TREE_NUM_PROPERTIES = 7;
    private static final int TREE_ID = 1;
    private static final int TREE_COL = 2;
    private static final int TREE_ROW = 3;
    private static final int TREE_HEALTH = 6;
    private static final int TREE_ANIMATION_PERIOD = 4;
    private static final int TREE_ACTION_PERIOD = 5;


    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumRows() { return numRows; }
    public int getNumCols() { return numCols; }
    public Set<Entity> getEntities() { return entities; }

    private boolean parseBackground( String[] properties, ImageStore imageStore )
    {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }
        return properties.length == BGND_NUM_PROPERTIES;
    }
    private boolean parseIgloo( String[] properties, ImageStore imageStore )
    {
        if (properties.length == IGLOO_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[IGLOO_COL]),
                    Integer.parseInt(properties[IGLOO_ROW]));
            Igloo entity = new Igloo(properties[IGLOO_ID], pt,
                    imageStore.getImageList(IGLOO_KEY));
            tryAddEntity(entity);
        }
        return properties.length == IGLOO_NUM_PROPERTIES;
    }

    private boolean parseObstacle( String[] properties, ImageStore imageStore )
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Obstacle entity = new Obstacle(properties[OBSTACLE_ID], pt,
                    imageStore.getImageList(OBSTACLE_KEY),
                    Integer.parseInt(properties[OBSTACLE_ANIMATION_PERIOD]));
            tryAddEntity(entity);
        }
        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

//    private boolean parseShark( String[] properties, ImageStore imageStore )
//    {
//        if (properties.length == SHARK_NUM_PROPERTIES) {
//            Point pt = new Point(Integer.parseInt(properties[SHARK_COL]),
//                    Integer.parseInt(properties[SHARK_ROW]));
//            String id = properties[SHARK_ID];
//            Shark entity = new Shark(id, pt, imageStore.getImageList(SHARK_KEY), SHARK_ACTION_PERIOD, SHARK_ANIMATION_PERIOD);
//
//            tryAddEntity(entity);
//        }
//        return properties.length == SHARK_NUM_PROPERTIES;
//    }

    private boolean parsePlayer( String[] properties, ImageStore imageStore )
    {
        if (properties.length == PLAYER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[PLAYER_COL]),
                    Integer.parseInt(properties[PLAYER_ROW]));
            Player entity = new Player(properties[PLAYER_ID],
                    pt, imageStore.getImageList(PLAYER_KEY),
                    Integer.parseInt(properties[PLAYER_ACTION_PERIOD]),
                    Integer.parseInt(properties[PLAYER_ANIMATION_PERIOD]), 0, 3);
            tryAddEntity(entity);
        }
        return properties.length == PLAYER_NUM_PROPERTIES;
    }

    private boolean parseBird( String[] properties, ImageStore imageStore )
    {
        if (properties.length == BIRD_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BIRD_COL]),
                    Integer.parseInt(properties[BIRD_ROW]));
            Bird entity = new Bird(properties[BIRD_ID],
                    pt, imageStore.getImageList(BIRD_KEY),
                    Integer.parseInt(properties[BIRD_ACTION_PERIOD]),
                    Integer.parseInt(properties[BIRD_ANIMATION_PERIOD]));
            tryAddEntity(entity);
        }
        return properties.length == BIRD_NUM_PROPERTIES;
    }

    private boolean parseFish( String[] properties, ImageStore imageStore)
    {
        if (properties.length == FISH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                    Integer.parseInt(properties[FISH_ROW]));
            Fish entity = new Fish(properties[FISH_ID],
                    pt, imageStore.getImageList(FISH_KEY),
                    Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                    Integer.parseInt(properties[FISH_ANIMATION_PERIOD]));
            tryAddEntity(entity);
        }
        return properties.length == FISH_NUM_PROPERTIES;
    }

    private boolean parseTree( String[] properties, ImageStore imageStore )
    {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                    Integer.parseInt(properties[TREE_ROW]));
            Tree entity = new Tree(properties[TREE_ID],
                    pt, imageStore.getImageList(TREE_KEY),
                    Integer.parseInt(properties[TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[TREE_HEALTH]));
            tryAddEntity(entity);
        }
        return properties.length == TREE_NUM_PROPERTIES;
    }

    private boolean processLine( String line, ImageStore imageStore )
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, imageStore);
                case PLAYER_KEY:
                    return parsePlayer(properties, imageStore);
                case OBSTACLE_KEY:
                    return parseObstacle(properties, imageStore);
                case FISH_KEY:
                    return parseFish(properties, imageStore);
                case BIRD_KEY:
                    return parseBird(properties, imageStore);
                case IGLOO_KEY:
                    return parseIgloo(properties, imageStore);
                case TREE_KEY:
                    return parseTree(properties, imageStore);
            }
        }
        return false;
    }

    public void load(Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    public boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < numRows && pos.getX() >= 0
                && pos.getX() < numCols;
    }

    private Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity other : entities) {
                int otherDistance = other.getPosition().distanceSquared(pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }
            return Optional.of(nearest);
        }
    }
    public Optional<Entity> findNearest(Point pos, List<Class> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind: kinds)
        {
            for (Entity entity : entities) {
                if (kind.isInstance(entity)) {
                    ofType.add(entity);
                }
            }
        }
        return nearestEntity(ofType, pos);
    }
    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */
    private void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }
    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }
    public void movePlayer(Player player, Point pos) {
        Point oldPos = player.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, player);
            player.setPosition(pos);
        }
    }
    public void removeEntity(Entity entity) {
        removeEntityAt(entity.getPosition());
    }
    private void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }
    public void setOccupancyCell(Point pos, Entity entity) {
        occupancy[pos.getY()][pos.getX()] = entity;
    }
    public Entity getOccupancyCell(Point pos) {
        return occupancy[pos.getY()][pos.getX()];
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }
    private Background getBackgroundCell(Point pos) {
        return background[pos.getY()][pos.getX()];
    }

    private void setBackground(Point pos, Background background) {
        if (withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }
    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.getY()][pos.getX()] = background;
    }

}


