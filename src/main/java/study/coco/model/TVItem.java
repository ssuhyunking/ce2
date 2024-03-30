package study.coco.model;

public class TVItem extends Item {
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
