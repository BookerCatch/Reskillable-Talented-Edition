package codersafterdark.reskillable.skill.farming;

    import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitSeasons extends Trait {

    public TraitSeasons() {
        super(new ResourceLocation(MOD_ID, "seasonal_greetings"), 1, 2, new ResourceLocation(MOD_ID, "farming"),
                6, "reskillable:farming|12");
        setIcon(new ResourceLocation("sereneseasons:textures/items/ss_icon.png"));
    }

}
