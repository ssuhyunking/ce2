package study.coco.model;

public class WateringCanItem extends Item {
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
