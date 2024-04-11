import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Fish extends ExecuteActivity {
    private static final String FISHBONES_KEY = "fishbones";

    private PathingStrategy strategy = new SingleStep();

    public Fish(String id,
                Point position,
                List<PImage> images,
                int actionPeriod,
                int animationPeriod)
    {
        super(id, position, images, actionPeriod,
                animationPeriod, 1, 1);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createAnimationAction(0), this.getAnimationPeriod());
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {

        if (!transform(world, scheduler, imageStore)) {
            Point fishTarget = new Point(0, getPosition().getY());
            if (world.withinBounds(fishTarget)) {
                if (moveToFish(world, fishTarget, scheduler)) {
                    fishTarget.setX(39);
                    scheduler.scheduleEvent(this,
                            createActivityAction(world, imageStore),
                            this.getActionPeriod());
                }
                fishTarget.setX(0);
            }
        }
    }

    private Point nextPositionFish(
            WorldModel world, Point destPos)
    {
        List<Point> path = strategy.computePath(getPosition(), destPos,
                PathingStrategy.canPassThrough(world),
                PathingStrategy.withinReach(),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0) {
            return getPosition();
        }
        return path.get(0);
    }

    public boolean moveToFish(
            WorldModel world,
            Point target,
            EventScheduler scheduler)
    {
        if (getPosition().adjacent(target)) {
            return true;
        }
        else {
            Point nextPos = nextPositionFish(world, target);

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (getHealth() < 1) {
            FishBones fishbones = new FishBones(getId(),
                    getPosition(),
                    imageStore.getImageList(FISHBONES_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.tryAddEntity(fishbones);

            return true;
        }
        return false;
    }

}
