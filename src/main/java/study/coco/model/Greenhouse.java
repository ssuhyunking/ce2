package study.coco.model;

import study.coco.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Greenhouse extends Room {
    private PlantItem plant;

    public Greenhouse(String name, String description) {
        super(name, description);
    }

    @Override
    public void addItem(Item item) {
        if (item instanceof PlantItem) {
            this.plant = (PlantItem) item;
        } else {
            super.addItem(item);
        }
    }

    public PlantItem getPlant() {
        return plant;
    }

    @Override
    public Item getItem(String itemName) {
        if (plant != null && plant.getName().equalsIgnoreCase(itemName)) {
            return plant;
        } else {
            return super.getItem(itemName);
        }
    }

    public void growPlant() {
        if (plant != null) {
            System.out.println("The plant grows taller.");
            // update status of the plant
            plant.grow(); // method for growing plant
            if (plant.isMature()) {
                System.out.println("The plant is fully grown and ready for harvest.");

            }
        }
    }
}
