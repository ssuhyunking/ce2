package study.coco;

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
            String input = scanner.nextLine().toLowerCase(); // 입력값을 소문자로 변환
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

        livingRoom.setExit("kitchen", kitchen);
        livingRoom.setExit("bedroom", bedroom);
        kitchen.setExit("living room", livingRoom);
        kitchen.setExit("greenhouse", greenhouse);
        bedroom.setExit("living room", livingRoom);
        greenhouse.setExit("kitchen", kitchen);

        livingRoom.addItem(new TVItem("TV", "A TV that can show cooking channels."));
        kitchen.addItem(new WateringCanItem("Watering Can", "A watering can to water the plants."));
        kitchen.addItem(new KeyItem("Key", "A key to enter the greenhouse.", greenhouse));
        kitchen.addItem(new CookingItem("Cooking Utensils", "Cook a meal using ingredients."));
        bedroom.addItem(new BookItem("Book", "A readable book.", "Enjoy text adventure games!"));

        greenhouse.addPlant(new Plant("Basil","A basil plant that can be used as a ingredients for cooking."));

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
        String command = parts[0].toLowerCase(); // 명령어를 소문자로 변환

        switch (command) {
            case "go":
                if (parts.length < 2) {
                    System.out.println("Specify the direction.");
                    return;
                }
                String direction = parts[1];
                Room nextRoom = currentRoom.getExit(direction.toLowerCase()); // 방향을 소문자로 변환
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
                Item item = currentRoom.getItem(itemName.toLowerCase()); // 아이템 이름을 소문자로 변환
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
                cleanRoom();
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
                System.out.println("Harvested " + plant.getName());
                // Add harvested plant to inventory or do something else
            } else {
                System.out.println("No plants to harvest.");
            }
        } else {
            System.out.println("Harvesting is not available in this room.");
        }
    }

    private void cleanRoom() {
        if (currentRoom instanceof Room) {
            Room room = (Room) currentRoom;
            if (!room.isClean()) {
                room.clean();
                System.out.println("The room is now clean.");
            } else {
                System.out.println("The room is already clean.");
            }
        } else {
            System.out.println("Cleaning is not available in this room.");
        }
    }

    private void cookMeal() {
        // Implement cooking
    }

    private void sleep() {
        // Implement sleeping
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
        exits.put(direction.toLowerCase(), neighbor); // 방향을 소문자로 저장
    }

    public Room getExit(String direction) {
        return exits.get(direction.toLowerCase()); // 방향을 소문자로 조회
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addItem(Item item) {
        items.put(item.getName().toLowerCase(), item); // 아이템 이름을 소문자로 저장
    }

    public Item getItem(String itemName) {
        return items.get(itemName.toLowerCase()); // 아이템 이름을 소문자로 조회
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

class Greenhouse extends Room {
    private Plant plant;

    public Greenhouse(String name, String description) {
        super(name, description);
    }

    public Plant getPlant() {
        return plant;
    }

    public void addPlant(Plant plant) {
        this.plant = plant;
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
        // Implement use action
    }
}

class TVItem extends Item {
    public TVItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void use(Player player) {
        System.out.println("You watch TV.");
    }
}

class WateringCanItem extends Item {
    public WateringCanItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void use(Player player) {
        System.out.println("You use the watering can.");
    }
}

class KeyItem extends Item {
    private Room unlockRoom;

    public KeyItem(String name, String description, Room unlockRoom) {
        super(name, description);
        this.unlockRoom = unlockRoom;
    }

    @Override
    public void use(Player player) {
        player.setCurrentRoom(unlockRoom);
        System.out.println("You unlock the door and move to " + unlockRoom.getName());
    }
}

class CookingItem extends Item {
    public CookingItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void use(Player player) {
        System.out.println("You start cooking.");
    }
}

class BookItem extends Item {
    private String content;

    public BookItem(String name, String description, String content) {
        super(name, description);
        this.content = content;
    }

    @Override
    public void use(Player player) {
        System.out.println("You read the book: " + content);
    }
}
