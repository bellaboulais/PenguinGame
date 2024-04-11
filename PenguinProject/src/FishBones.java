import processing.core.PImage;

import java.util.List;
public class FishBones extends Entity{
    public FishBones(String id,
                 Point position,
                 List<PImage> images) {
        super(id, position, images, 0);
    }
}
