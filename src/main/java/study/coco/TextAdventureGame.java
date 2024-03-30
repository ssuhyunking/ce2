package study.coco;

import study.coco.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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


