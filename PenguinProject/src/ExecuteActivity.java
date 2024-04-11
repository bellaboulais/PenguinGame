import processing.core.PImage;

import java.util.List;

public abstract class ExecuteActivity extends EntityActions{

    private final int healthLimit;
    private final int actionPeriod;

    public ExecuteActivity(String id,
                           Point position,
                           List<PImage> images,
                           int actionPeriod,
                           int animationPeriod,
                           int health, int healthLimit) {
        super(id, position, images, animationPeriod, health);
        this.actionPeriod = actionPeriod;
        this.healthLimit = healthLimit;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);}
}
