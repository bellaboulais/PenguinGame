import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Bird extends ExecuteActivity {

    private static final String FISH_KEY = "fish";
    private PathingStrategy strategy = new AStar();

    public Bird(String id,
                 Point position,
                 List<PImage> images,
                 int actionPeriod,
                 int animationPeriod)
    {
        super(id, position, images, actionPeriod,
                animationPeriod, 0, 0);
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
        Optional<Entity> birdTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(FishBones.class)));

        if (birdTarget.isPresent()) {
            Point tgtPos = birdTarget.get().getPosition();

            if (moveToBird(world, birdTarget.get(), scheduler)) {
                Fish fish = new Fish("fish" + getId(), tgtPos,
                        imageStore.getImageList(FISH_KEY), 70,80);

                world.tryAddEntity(fish);
                fish.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                this.getActionPeriod());
    }

    private Point nextPositionBird(
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

    public boolean moveToBird(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionBird(world, target.getPosition());

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
}
