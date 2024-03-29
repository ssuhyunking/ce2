package study.coco;

import java.util.HashMap;
import java.util.Scanner;

public class TextAdventureGame {
    public static void main(String[] args) {
        System.out.println("Starting the housework!");

        Player player = new Player();
        Map<String, Room> rooms = createRooms();
        player.setCurrentRoom(rooms.get("Living Room"));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            }
        }
        scanner.close();;;;
    }

    private static Map<String, Room> createRooms() {
        Map<String, Room> rooms = new HashMap<>();

    }change
}
