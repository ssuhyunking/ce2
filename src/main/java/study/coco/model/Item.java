package study.coco.model;
import study.coco.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Item {
    private String name;
    private String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void use(Player player) {
        System.out.println("This item cannot be used.");
    }
}
