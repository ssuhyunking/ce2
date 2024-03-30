package study.coco.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;
    private Map<String, Item> items;
    private boolean clean;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new HashMap<>();
        this.clean = false;
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    public void removeItem(Item item) {
        items.remove(item.getName());
    }

    public Collection<Item> getItems() {
        return items.values();
    }

    public boolean isClean() {
        return clean;
    }

    public void clean() {
        clean = true;
    }
}
