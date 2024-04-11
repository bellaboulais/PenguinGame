import processing.core.PImage;

import java.util.List;

public class Igloo extends Entity{
    public Igloo(String id,
                 Point position,
                 List<PImage> images) {
        super(id, position, images, 0);
    }
}
