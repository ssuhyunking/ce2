package study.coco.model;
import study.coco.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PlantItem extends Item {
    public int height; // height of the plant
    private boolean mature; // is plant mature or not

    public PlantItem(String name, String description) {
        super(name, description);
        this.height = 0;
        this.mature = false;
    }

    public void use(Player player) {
        System.out.println("You cannot use the plant item directly.");
    }

    public void grow() {
        height++;
        if (height >= 3) { // if you player give water 3 times, mature
            mature = true; // plant is mature
        }
    }

    public boolean isMature() {
        return mature;
    }
}
