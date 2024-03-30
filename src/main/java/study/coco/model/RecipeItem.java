package study.coco.model;

public class RecipeItem extends Item {
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
