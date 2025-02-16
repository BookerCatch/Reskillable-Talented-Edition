package codersafterdark.reskillable.common.skill.traits.defense;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class TraitEffectTwist extends Trait {
    private Map<Potion, Potion> badPotions;

    public TraitEffectTwist() {
        super(new ResourceLocation(MOD_ID, "effect_twist"), 3, 1, new ResourceLocation(MOD_ID, "defense"),
                8, "reskillable:defense|20", "reskillable:attack|16", "reskillable:magic|16");

        this.badPotions = new HashMap<>();
        this.badPotions.put(MobEffects.SPEED, MobEffects.SLOWNESS);
        this.badPotions.put(MobEffects.HASTE, MobEffects.MINING_FATIGUE);
        this.badPotions.put(MobEffects.STRENGTH, MobEffects.WEAKNESS);
        this.badPotions.put(MobEffects.REGENERATION, MobEffects.POISON);
        this.badPotions.put(MobEffects.NIGHT_VISION, MobEffects.BLINDNESS);
        this.badPotions.put(MobEffects.LUCK, MobEffects.UNLUCK);
    }

    @Override
    public void onHurt(LivingHurtEvent event) {
        if (event.isCanceled()) {
            return;
        }
        Entity src = event.getSource().getTrueSource();
        if (src instanceof EntityLivingBase && src instanceof IMob && src.world.rand.nextBoolean()) {
            List<PotionEffect> effects = event.getEntityLiving().getActivePotionEffects().stream().filter(p -> this.badPotions.containsKey(p.getPotion())).collect(Collectors.toList());
            if (effects.size() > 0) {
                PotionEffect target = effects.get(src.world.rand.nextInt(effects.size()));
                PotionEffect newEff = new PotionEffect(this.badPotions.get(target.getPotion()), 80 + src.world.rand.nextInt(60), 0);
                ((EntityLivingBase) src).addPotionEffect(newEff);
            }
        }
    }
}