package codersafterdark.reskillable.profession;

import codersafterdark.reskillable.api.profession.Profession;
import net.minecraft.util.ResourceLocation;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class ProfessionRogue extends Profession {
    public ProfessionRogue() {
        super(new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "textures/gui/profession_bg/adamantium_block.png"));
        this.setGuiIndex(2);
        setColor(15456528);
        addSubProfession("assassin", 0);
        addSubProfession("archer", 2);
    }
}
