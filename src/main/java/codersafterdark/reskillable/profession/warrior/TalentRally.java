package codersafterdark.reskillable.profession.warrior;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TalentRally extends Talent {

   public TalentRally() {
       super(new ResourceLocation(MOD_ID, "rally"), 1, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
               3, "reskillable:attack 10", "reskillable:defense 8");
       setCap(5);
       this.setIcon(new ResourceLocation("textures/items/iron_sword.png"));
       MinecraftForge.EVENT_BUS.register(this);
   }

    @SubscribeEvent
    public void onKillMob(LivingDeathEvent event) {
        if (event.isCanceled()) return;
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer pl = (EntityPlayer)event.getSource().getTrueSource();
            if (PlayerDataHandler.get(pl).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                AxisAlignedBB playerRange = new AxisAlignedBB(pl.posX - 6, pl.posY - 6, pl.posZ + 6, pl.posX + 6, pl.posY + 6, pl.posZ - 6);
                List<EntityPlayer> party = pl.world.getEntitiesWithinAABB(EntityPlayer.class, playerRange);
                for (EntityPlayer entity: party) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100));
                    entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100));
                }
            }
        }
    }

}
