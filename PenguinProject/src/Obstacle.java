import processing.core.PImage;

import java.util.List;

public class Obstacle extends EntityActions {
    public Obstacle(String id,
                    Point position,
                    List<PImage> images,
                    int animationPeriod) {
        super(id, position, images, animationPeriod, 0);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createAnimationAction(0), this.getAnimationPeriod());
    }

}
