package study.coco.model;
import study.coco.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class KeyItem extends Item {
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
