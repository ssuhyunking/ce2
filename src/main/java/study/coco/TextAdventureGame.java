package study.coco;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Collection;

public class TextAdventureGame {
    public static Map<String, Room> rooms;
    public static void main(String[] args) {
        System.out.println("Starting the housework!");

        Player player = new Player();
        rooms = createRooms();
        player.setCurrentRoom(rooms.get("livingroom"));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(player.getCurrentRoom().getDescription());
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Exiting the game.");
                break;
            } else if (input.equalsIgnoreCase("help")) {
                System.out.println("Command list:");
                System.out.println("- go direction: Move in the specified direction.");
                System.out.println("- look: Display the current room's description and item list.");
                System.out.println("- use item name: Use the specified item.");
                System.out.println("- inventory: Display the list of items in the inventory.");
                System.out.println("- harvest: Harvest plants to obtain ingredients.");
                System.out.println("- clean: Clean the living room, bedroom, and kitchen.");
                System.out.println("- cook: Cook a meal.");
                System.out.println("- sleep: Sleep on the bed.");
                System.out.println("- quit: Quit the game.");
            } else {
                player.handleInput(input);
            }
        }
        scanner.close();
    }

    private static Map<String, Room> createRooms() {
        rooms = new HashMap<>();

        Room livingRoom = new Room("living Room", "There is a spacious living room.");
        Room kitchen = new Room("kitchen", "It's a clean kitchen.");
        Room bedroom = new Room("bedroom", "It's a warm bedroom.");
        Greenhouse greenhouse = new Greenhouse("greenhouse", "It's a greenhouse with beautiful plants.");

        livingRoom.setExit("kitchen", kitchen);
        livingRoom.setExit("bedroom", bedroom);
        kitchen.setExit("livingroom", livingRoom);
        kitchen.setExit("greenhouse", greenhouse);
        bedroom.setExit("livingroom", livingRoom);
        greenhouse.setExit("kitchen", kitchen);

        livingRoom.addItem(new TVItem("tv", "A TV that can show cooking channels."));
        kitchen.addItem(new KeyItem("key", "A key to enter the greenhouse.", kitchen));
        kitchen.addItem(new WateringCanItem("wateringcan", "A can used for watering plants."));
        bedroom.addItem(new RecipeItem("recipe", "A recipe book.", "Enjoy text adventure games!"));
        greenhouse.addItem(new PlantItem("basil", "A basil plant that you can cook with. You can make it grow."));

        rooms.put("livingroom", livingRoom);
        rooms.put("kitchen", kitchen);
        rooms.put("bedroom", bedroom);
        rooms.put("greenhouse", greenhouse);

        return rooms;
    }
}

class Player {
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

class Room {
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

class TVItem extends Item {
    public TVItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void use(Player player) {
        System.out.println("Watching TV...");
        System.out.println("While watching TV, you feel inspired for cooking. Cooking success rate increased.");
        player.setCookingSuccessRate(player.getCookingSuccessRate() + 0.3);
    }
}

class KeyItem extends Item {
    private Room targetRoom;

    public KeyItem(String name, String description, Room targetRoom) {
        super(name, description);
        this.targetRoom = targetRoom;
    }

    public Room getTargetRoom() {
        return targetRoom;
    }

    @Override
    public void use(Player player) {
        if (player.getCurrentRoom().equals(targetRoom)) {
            System.out.println("You unlocked the door to the greenhouse.");
        } else {
            System.out.println("The key doesn't seem to work here.");
        }
    }
}

class RecipeItem extends Item {
    public RecipeItem(String name, String description, String content) {
        super(name, description);
    }

    @Override
    public void use(Player player) {
        System.out.println("Reading the recipe...");
        System.out.println("You feel more confident about your cooking skills. Cooking success rate increased.");
        player.setCookingSuccessRate(player.getCookingSuccessRate() + 0.4);
    }
}

class WateringCanItem extends Item {
    public WateringCanItem(String name, String description) {
        super(name, description);
    }

    // Get method to add the watering can to inventory
    public void get(Player player) {
        player.getInventory().put(getName(), this); // Add the watering can to player's inventory
        System.out.println("You obtained the wateringcan.");
    }

    // Use method to grow plants if the player is in the greenhouse
    public void use(Player player) {
        if (player.getCurrentRoom() instanceof Greenhouse) {
            Greenhouse greenhouse = (Greenhouse) player.getCurrentRoom();
            PlantItem plant = greenhouse.getPlant();
            if (plant != null) {
                greenhouse.growPlant(); // grow plant
                System.out.println("You used the watering can.");
                System.out.println("The plant is watered and looks healthier.");
                System.out.println("The plant has grown by " + plant.height + " units.");
            } else {
                System.out.println("There are no plants to water.");
            }
        } else {
            System.out.println("You can only use the watering can in the greenhouse.");
        }
    }
}


class PlantItem extends Item{
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

class Greenhouse extends Room {
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
