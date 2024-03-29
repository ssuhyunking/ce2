package study.coco;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextAdventureGame {
    public static void main(String[] args) {
        System.out.println("Starting the housework!");

        Player player = new Player();
        Map<String, Room> rooms = createRooms();
        player.setCurrentRoom(rooms.get("Living Room"));

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
        Map<String, Room> rooms = new HashMap<>();

        Room livingRoom = new Room("Living Room", "There is a spacious living room.");
        Room kitchen = new Room("Kitchen", "It's a clean kitchen.");
        Room bedroom = new Room("Bedroom", "It's a warm bedroom.");
        Greenhouse greenhouse = new Greenhouse("Greenhouse", "It's a greenhouse with beautiful plants.");

        livingRoom.setExit("Kitchen", kitchen);
        livingRoom.setExit("Bedroom", bedroom);
        kitchen.setExit("Living Room", livingRoom);
        kitchen.setExit("Greenhouse", greenhouse);
        bedroom.setExit("Living Room", livingRoom);
        greenhouse.setExit("Kitchen", kitchen);

        livingRoom.addItem(new TVItem("TV", "A TV that can show cooking channels."));
        kitchen.addItem(new KeyItem("Key", "A key to enter the greenhouse.", greenhouse));
        bedroom.addItem(new RecipeItem("Recipe", "A recipe book.", "Enjoy text adventure games!"));

        rooms.put("Living Room", livingRoom);
        rooms.put("Kitchen", kitchen);
        rooms.put("Bedroom", bedroom);
        rooms.put("Greenhouse", greenhouse);

        return rooms;
    }
}

class Player {
    private int health;
    private Room currentRoom;
    private Map<String, Item> inventory;
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
        String[] parts = input.split(" ");
        String command = parts[0];

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
                Item item = currentRoom.getItem(itemName);
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
                harvestPlants();
                break;
            case "clean":
                clean();
                break;
            case "cook":
                cookMeal();
                break;
            case "sleep":
                sleep();
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void decreaseHealth() {
        health--;
        System.out.println("Player's health decreased. Current health: " + health);
        if (health <= 0) {
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

    private void harvestPlants() {
        if (currentRoom instanceof Greenhouse) {
            Greenhouse greenhouse = (Greenhouse) currentRoom;
            Plant plant = greenhouse.getPlant();
            if (plant != null) {
                inventory.put(plant.getName(), new PlantItem(plant.getName(), plant.getDescription()));
                greenhouse.removePlant();
                System.out.println("You harvested the plant. You obtained some ingredients.");
            } else {
                System.out.println("There are no plants to harvest.");
            }
        } else {
            System.out.println("You can only harvest plants in the greenhouse.");
        }
    }

    private void clean() {
        if (!currentRoom.isClean()) {
            currentRoom.clean();
            System.out.println("You cleaned the room.");
        } else {
            System.out.println("The room is already clean.");
        }
    }

    private void sleep() {
        if (areAllRoomsClean() && currentRoom.getName().equalsIgnoreCase("Bedroom")) {
            System.out.println("You slept and restored your health.");
            health = INITIAL_HEALTH;
        } else {
            System.out.println("You cannot sleep now.");
        }
    }

    private boolean areAllRoomsClean() {
        for (Room room : TextAdventureGame.rooms.values()) {
            if (!room.isClean()) {
                return false;
            }
        }
        return true;
    }

    private void cookMeal() {
        if (currentRoom.getName().equalsIgnoreCase("Kitchen")) {
            if (inventory.containsKey("Basil")) {
                System.out.println("You cooked a meal using basil. Cooking success rate increased.");
                setCookingSuccessRate(getCookingSuccessRate() + 0.4);
            } else {
                System.out.println("You need basil to cook a meal.");
            }
        } else {
            System.out.println("You can only cook in the kitchen.");
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
        player.setCookingSuccessRate(player.getCookingSuccessRate() + 0.2);
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

class PlantItem extends Item {
    public PlantItem(String name, String description) {
        super(name, description);
    }
}

class Plant {
    private String name;
    private String description;

    public Plant(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

class Greenhouse extends Room {
    private Plant plant;

    public Greenhouse(String name, String description) {
        super(name, description);
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public void removePlant() {
        this.plant = null;
    }
}
