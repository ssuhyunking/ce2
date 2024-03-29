import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Collection;

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
                System.out.println("- use item name: Use the specified item. If the item is in the current room, it will be added to your inventory.");
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

        Room livingRoom = new Room("living Room", "There is a spacious living room.");
        Room kitchen = new Room("kitchen", "It's a clean kitchen.");
        Room bedroom = new Room("bedroom", "It's a warm bedroom.");
        Greenhouse greenhouse = new Greenhouse("greenhouse", "It's a greenhouse with beautiful plants.");

        livingRoom.setExit("kitchen", kitchen);
        livingRoom.setExit("bedroom", bedroom);
        kitchen.setExit("living Room", livingRoom);
        kitchen.setExit("greenhouse", greenhouse);
        bedroom.setExit("living Room", livingRoom);
        greenhouse.setExit("kitchen", kitchen);

        livingRoom.addItem(new TVItem("TV", "A TV that can show cooking channels."));
        kitchen.addItem(new WateringCanItem("Watering Can", "A watering can to water the plants."));
        kitchen.addItem(new KeyItem("Key", "A key to enter the greenhouse.", greenhouse));
        kitchen.addItem(new CookingItem("Cooking Utensils", "Cook a meal using ingredients."));
        bedroom.addItem(new RecipeItem("Recipe", "A recipe book.", "Enjoy text adventure games!"));

        greenhouse.addPlant(new Plant("Basil","A basil plant that can be used as ingredients for cooking."));

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
                    if (item instanceof WateringCanItem && currentRoom.getName().equalsIgnoreCase("kitchen")) {
                        inventory.put(item.getName(), item);
                        System.out.println("Added " + item.getName() + " to inventory.");
                        currentRoom.getItems().remove(item.getName());
                    } else {
                        item.use(this);
                    }
                }
                break;
            case "inventory":
                System.out.println("Inventory:");
                for (Item invItem : inventory.values()) {
                    System.out.println("- " + invItem.getName() + ": " + invItem.getDescription());
                }
                break;
            case "harvest":
                // Implement harvesting plants
                break;
            case "clean":
                // Implement cleaning rooms
                break;
            case "cook":
                // Implement cooking
                break;
            case "sleep":
                // Implement sleeping
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
}

class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;
    private Map<String, Item> items;
    private Plant plant;
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

    public void addPlant(Plant plant) {
        this.plant = plant;
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
    private boolean usable;

    public Item(String name, String description, boolean usable) {
        this.name = name;
        this.description = description;
        this.usable = usable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUsable() {
        return usable;
    }

    public void use(Player player) {
        System.out.println("This item cannot be used.");
    }
}

class TVItem extends Item {
    public TVItem(String name, String description) {
        super(name, description, true);
    }

    @Override
    public void use(Player player) {
        System.out.println("Watching TV...");
        System.out.println("While watching TV, you feel inspired for cooking. Cooking success rate increased by 50%.");
        player.setCookingSuccessRate(0.5);
    }
}

class WateringCanItem extends Item {
    public WateringCanItem(String name, String description) {
        super(name, description, true);
    }
}

class KeyItem extends Item {
    private Room targetRoom;

    public KeyItem(String name, String description, Room targetRoom) {
        super(name, description, true);
        this.targetRoom = targetRoom;
    }

    public Room getTargetRoom() {
        return targetRoom;
    }
}

class CookingItem extends Item {
    public CookingItem(String name, String description) {
        super(name, description, true);
    }
}

class RecipeItem extends Item {
    private String content;

    public RecipeItem(String name, String description, String content) {
        super(name, description, true);
        this.content = content;
    }

    @Override
    public void use(Player player) {
        System.out.println("Reading the recipe...");
        System.out.println("You feel more confident in your cooking skills. Cooking success rate increased.");
        player.setCookingSuccessRate(player.getCookingSuccessRate() + 0.1);
    }
}

class Plant {
    private String name;
    private String description;
    private int growthLevel;

    public Plant(String name, String description) {
        this.name = name;
        this.description = description;
        this.growthLevel = 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getGrowthLevel() {
        return growthLevel;
    }

    public void water() {
        System.out.println("You watered the plant. It looks happier now.");
        growthLevel++;
    }

    public void harvest() {
        System.out.println("You harvested the plant. You obtained some ingredients.");
        growthLevel = 0; //after harvest initiate growthLevel
    }
}

class Greenhouse extends Room {
    public Greenhouse(String name, String description) {
        super(name, description);
    }
}
