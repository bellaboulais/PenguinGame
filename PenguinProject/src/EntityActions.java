import processing.core.PImage;

import java.util.List;

public abstract class EntityActions extends Entity{

    private final int animationPeriod;

    public EntityActions(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod, int health) {

        super(id, position, images, health);
        this.animationPeriod = animationPeriod;
    }

    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    public int getAnimationPeriod() { return animationPeriod; }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount); }

}
