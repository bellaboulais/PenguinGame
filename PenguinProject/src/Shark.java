import processing.core.PImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Shark extends ExecuteActivity{
    private PathingStrategy strategy = new AStar();
    public Shark(String id,
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
            EventScheduler scheduler)
    {
        Optional<Entity> sharkTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Player.class)));

        if (sharkTarget.isPresent()) {
            if (moveToShark(world, sharkTarget.get(), scheduler)) {
                System.out.println("Ouch!");
            }
        }
        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                this.getActionPeriod());
    }

    private Point nextPositionShark(
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

    public boolean moveToShark(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (getPosition().adjacent(target.getPosition())) {
            if (target.getHealth() > 0) {
                target.decrementHealth();
            }
            else {
                System.out.println("You were eaten! Game Over.");
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);
            }
            return true;
        }
        else {
            Point nextPos = nextPositionShark(world, target.getPosition());

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
