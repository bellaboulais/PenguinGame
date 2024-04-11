import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Player extends ExecuteActivity {
    private static final int PENGUIN_LIMIT = 4;
    private static final String FISHBONES_KEY = "fishbones";
    private int resourceLimit = 4;
    private int resourceCount = 0;

    public Player(String id,
                  Point position,
                  List<PImage> images,
                  int actionPeriod,
                  int animationPeriod,
                  int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    public void resetResourceCount() {
        resourceCount = 0;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, createAnimationAction(0), this.getAnimationPeriod());
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> PlayerTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Fish.class)));

        Optional<Entity> fullTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Igloo.class)));

        if (fullTarget.isPresent()
                && getPosition().adjacent(fullTarget.get().getPosition()))
        {
            resetResourceCount();
            System.out.println("I'm out!");
        }

        else if (PlayerTarget.isPresent()) {
            Point tgtPos = PlayerTarget.get().getPosition();

            if (getPosition().adjacent(tgtPos)) {
                if (getHealth() == PENGUIN_LIMIT && resourceCount == resourceLimit) {
                    System.out.println("I'm full!");
                }
                else {
                    if (getHealth() < PENGUIN_LIMIT) {
                        incrementHealth();
                    }
                    else {
                        resourceCount ++;
                    }
                    world.removeEntity(PlayerTarget.get());
                    FishBones bones = new FishBones("fishbones" + getId(), tgtPos,
                            imageStore.getImageList(FISHBONES_KEY));

                    world.tryAddEntity(bones);
                }
            }
        }
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                this.getActionPeriod());
    }

    public Point getPosition() {
        return super.getPosition();
    }

    public void setPosition(Point newPos) {
        super.setPosition(newPos);
    }

    public boolean movePlayer(WorldModel world, Point newPos) {
        if (world.isOccupied(newPos) && (world.getOccupancyCell(newPos).equals(Fish.class))) {
            if ((super.getHealth() == PENGUIN_LIMIT) && (resourceCount == resourceLimit) ){
                return false; }
            else {
            return true; }
        }
        else if (world.isOccupied(newPos) && (world.getOccupancyCell(newPos).equals(Igloo.class))) {
            return true;
        }
        else if (world.isOccupied(newPos)) {
            return false;
        }
        else {
            return true;
        }
    }
}

