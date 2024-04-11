import processing.core.PImage;

import java.util.List;

public class Tree extends ExecuteActivity {
        //implements Transform {

//    private static final String STUMP_KEY = "stump";
//
    public Tree(String id,
                Point position,
                List<PImage> images,
                int actionPeriod,
                int animationPeriod,
                int health)
    {
        super(id, position, images, actionPeriod,
                animationPeriod, health, 0);

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

            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
    }

//    public boolean transform(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore)
//    {
//        if (getHealth() <= 0) {
//            Stump stump = new Stump(getId(),
//                    getPosition(),
//                    imageStore.getImageList(STUMP_KEY));
//
//            world.removeEntity(this);
//            scheduler.unscheduleAllEvents(this);
//
//            world.addEntity(stump);
//
//            return true;
//        }
//        return false;
//    }
}
