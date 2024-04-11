import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;
    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private Player player;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }
        view.drawViewport();
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());

        Optional<Entity> target =
                world.findNearest(pressed, new ArrayList<>(Arrays.asList(Fish.class)));
        if (target.isPresent()) {
            ExecuteActivity fish = ((ExecuteActivity)target.get());
            Bird bird = new Bird("bird", fish.getPosition(), imageStore.getImageList("bird"),
                    80, 80);
            world.removeEntity(fish);
            scheduler.unscheduleAllEvents(fish);
            world.tryAddEntity(bird);
        }

        List<Point> newObstacle = new ArrayList<Point>();
        newObstacle.add(new Point(pressed.getX() - 1, pressed.getY()));
        newObstacle.add(new Point(pressed.getX() + 1, pressed.getY()));
        newObstacle.add(new Point(pressed.getX(), pressed.getY() + 1));
        newObstacle.add(new Point(pressed.getX() , pressed.getY() - 1));
        newObstacle.add(pressed);
        for ( int i = 0; i < newObstacle.size(); i++) {
            if (world.withinBounds(newObstacle.get(i))) {
                world.setBackgroundCell(newObstacle.get(i), new Background("seaweed", imageStore.getImageList("seaweed")));
            }
        }
        Point fishPos = new Point(0,0);
        for ( int i = 0; i < newObstacle.size() - 1; i++) {
            if (world.withinBounds(newObstacle.get(i))) {
                fishPos = newObstacle.get(i);
            }
        }
        Fish newFish = new Fish("fish", fishPos, imageStore.getImageList("fish"),
                ((ExecuteActivity) target.get()).getActionPeriod(), ((ExecuteActivity) target.get()).getAnimationPeriod());
        if (!world.isOccupied(fishPos)) {
            world.tryAddEntity(newFish);
        }

        Shark newShark = new Shark("shark", pressed, imageStore.getImageList("shark"), 800, 200);
        if (!world.isOccupied(pressed)) {
            world.tryAddEntity(newShark);
            newShark.executeActivity(world, imageStore, scheduler);
        }

        scheduleActions(world, scheduler, imageStore);

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();
            System.out.println(entity.getId() + ": " + entity.getClass() + " : " + entity.getHealth());
        }

    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
            else {
                player = world.getEntities().stream().filter(sc -> sc instanceof Player)
                        .map(sc -> (Player) sc).findFirst().get();
                int dx = 0;
                int dy = 0;
                switch (keyCode) {
                    case 'W':
                        if (player.movePlayer(world, new Point(player.getPosition().getX(), player.getPosition().getY() - 1))) {
                            world.movePlayer(player, new Point(player.getPosition().getX(), player.getPosition().getY() - 1));
                            dy = -1;
                        }
                        break;
                    case 'A':
                        if (player.movePlayer(world, new Point(player.getPosition().getX() - 1, player.getPosition().getY()))) {
                            world.movePlayer(player, new Point(player.getPosition().getX() - 1, player.getPosition().getY()));
                            dx = -1;
                        }
                        break;
                    case 'D':
                        if (player.movePlayer(world, new Point(player.getPosition().getX() + 1, player.getPosition().getY()))) {
                            world.movePlayer(player, new Point(player.getPosition().getX() + 1, player.getPosition().getY()));
                            dx = 1;
                        }
                        break;
                    case 'S':
                        if (player.movePlayer(world, new Point(player.getPosition().getX(), player.getPosition().getY() + 1))) {
                            world.movePlayer(player, new Point(player.getPosition().getX(), player.getPosition().getY() + 1));
                            dy = 1;
                        }
                        break;
                }
            view.shiftView(dx, dy);
            }
        }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    void loadImages(String filename, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    // type cast # 1
    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity.getClass().equals(Tree.class)) {
                ((Tree) entity).scheduleActions(scheduler, world, imageStore);
            }
            else if (entity.getClass().equals(Bird.class)) {
                ((Bird) entity).scheduleActions(scheduler, world, imageStore);
            }
            else if (entity.getClass().equals(Player.class)) {
                ((Player) entity).scheduleActions(scheduler, world, imageStore);
            }
            else if (entity.getClass().equals(Fish.class)) {
                ((Fish) entity).scheduleActions(scheduler, world, imageStore);
            }
            else if (entity.getClass().equals(Obstacle.class)) {
                ((Obstacle) entity).scheduleActions(scheduler, world, imageStore);
            }
            else if (entity.getClass().equals(Shark.class)) {
                ((Shark) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
