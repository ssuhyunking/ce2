package study.coco.model;
import study.coco.TextAdventureGame;
import study.coco.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Player {
    private int health;
    private Room currentRoom;
    private Map<String, Item> inventory;

    public Map<String, Item> getInventory() {
        return inventory;
    }

    private double cookingSuccessRate;

    private static final int INITIAL_HEALTH = 50;
    private static final double DEFAULT_COOKING_SUCCESS_RATE = 0.3;

    public Player() {
        this.health = INITIAL_HEALTH;
        this.inventory = new HashMap<>();
        this.cookingSuccessRate = DEFAULT_COOKING_SUCCESS_RATE;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void handleInput(String input) {
        decreaseHealth();
        String[] parts = input.toLowerCase().split(" ");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "go":
                if (parts.length < 2) {
                    System.out.println("Specify the direction.");
                    return;
                }
                String direction = parts[1];
                Room nextRoom = currentRoom.getExit(direction);
                if (nextRoom == null) {
                    System.out.println("There is no room in that direction.");
                } else {
                    setCurrentRoom(nextRoom);
                    System.out.println("Moved to " + nextRoom.getName());
                }
                break;
            case "look":
                System.out.println(currentRoom.getDescription());
                for (Item item : currentRoom.getItems()) {
                    System.out.println("- " + item.getName() + ": " + item.getDescription());
                }
                break;
            case "use":
                if (parts.length < 2) {
                    System.out.println("Specify the item to use.");
                    return;
                }
                String itemName = parts[1];
                Item item = inventory.get(itemName); // Try to get item from inventory first
                if (item == null) { // If item not found in inventory, try to find it in the room
                    item = currentRoom.getItem(itemName);
                }
                if (item == null) {
                    System.out.println("Item not found.");
                } else {
                    item.use(this);
                }
                break;
            case "inventory":
                System.out.println("Inventory:");
                for (Item invItem : inventory.values()) {
                    System.out.println("- " + invItem.getName() + ": " + invItem.getDescription());
                }
                break;
            case "harvest":
                if (currentRoom instanceof Greenhouse) { //in greenhouse
                    Greenhouse greenhouse = (Greenhouse) currentRoom;
                    PlantItem plant = greenhouse.getPlant();
                    if (plant != null) {
                        inventory.put(plant.getName(), new PlantItem(plant.getName(), plant.getDescription()));
                        plant.height = 0;
                        System.out.println("You harvested the plant. You obtained some ingredients.");
                    } else {
                        System.out.println("There are no plants to harvest.");
                    }
                } else { //not in greenhouse
                    System.out.println("You can only harvest plants in the greenhouse.");
                }
                break;
            case "clean":
                if (!currentRoom.isClean()) {
                    currentRoom.clean();
                    System.out.println("You cleaned the room.");
                } else {
                    System.out.println("The room is already clean.");
                }
                break;
            case "cook":
                if (currentRoom.getName().equalsIgnoreCase("kitchen")) { // in kitchen
                    if (inventory.containsKey("basil")) { // with basil
                        // try cooking
                        if (Math.random() < getCookingSuccessRate()) { //success or fail decide by cooking success rate
                            System.out.println("You successfully cooked a meal.");
                            // if every room is cleaned, Perfect ending
                            if (areAllRoomsClean()) {
                                System.out.println("Perfect ending! You successfully cooked a meal and cleaned all rooms.");
                            } else { //if not Happy ending
                                System.out.println("Happy ending! You successfully cooked a meal but some rooms are not clean.");
                            }
                        } else {  // fail cooking
                            health -= 10; // health decrease
                            System.out.println("You failed to cook the meal. Health decreased. Current health: " + health);
                        }
                    } else { // without basil
                        System.out.println("You need basil to cook a meal.");
                    }
                } else {
                    System.out.println("You can only cook in the kitchen."); // not in kitchen
                }
                break;
            case "sleep":
                if (areAllRoomsClean() && currentRoom.getName().equalsIgnoreCase("bedroom")) {
                    System.out.println("You slept and restored your health.");
                    health = INITIAL_HEALTH;
                } else { //not in bedroom
                    System.out.println("You cannot sleep now.");
                }
                break;
            case "get":
                if (parts.length < 2) {
                    System.out.println("Specify the item to get.");
                    return;
                }
                String getItemName = parts[1];
                get(getItemName);
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void decreaseHealth() { //every movement
        health--;
        System.out.println("Current health: " + health);
        if (health <= 0) { //if health goes under 0, bad ending
            System.out.println("Health reached 0, player fainted... Bad Ending!");
            System.exit(0);
        }
    }

    public void setCookingSuccessRate(double cookingSuccessRate) {
        this.cookingSuccessRate = cookingSuccessRate;
    }

    public double getCookingSuccessRate() {
        return cookingSuccessRate;
    }

    private boolean areAllRoomsClean() { //check if all rooms are cleaned
        for (Room room : TextAdventureGame.rooms.values()) {
            if (!room.isClean()) {
                return false;
            }
        }
        return true;
    }


    public void get(String itemName) {
        Item item = currentRoom.getItem(itemName);
        if (item == null) {
            System.out.println("Item not found.");
        } else {
            inventory.put(item.getName(), item);
            currentRoom.removeItem(item);
            System.out.println("You obtained the " + itemName + ".");
        }
    }
}
