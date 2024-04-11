import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */

public abstract class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int health;

    public Entity(
            String id,
            Point position,
            List<PImage> images,
            int health)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.health = health;

    }

    public  Point getPosition() {
        return position;
    }

    public void setPosition(Point point){
        position = point;
    }

    public String getId(){
        return this.id;
    }

    public List<PImage> getImages() {
        return this.images;
    }

    public int getHealth() {
        return this.health;
    }

    public void incrementHealth() {
        this.health ++;
    }

    public void decrementHealth() {
        this.health --;
    }

    public void nextImage() { imageIndex = (imageIndex + 1) % images.size(); }

    public PImage getCurrentImage() {
            return (this.images.get(this.imageIndex));
    }


}

