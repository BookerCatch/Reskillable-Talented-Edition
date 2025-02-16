package codersafterdark.reskillable.common.util;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class DamageSourceUtil {

    public static DamageSource newType(@Nonnull String damageType) {
        return new DamageSource(damageType);
    }

    public static DamageSource withEntityDirect(@Nonnull String damageType, @Nullable Entity source) {
        return new EntityDamageSource(damageType, source);
    }

    public static DamageSource withEntityIndirect(@Nonnull String damageType, @Nullable Entity actualSource, @Nullable Entity indirectSource) {
        return new EntityDamageSourceIndirect(damageType, indirectSource, actualSource);
    }

    @Nullable
    public static DamageSource withEntityDirect(@Nonnull DamageSource damageType, @Nullable Entity source) {
        return override(damageType, source, null);
    }

    @Nullable
    public static DamageSource withEntityIndirect(@Nonnull DamageSource damageType, @Nullable Entity actualSource, @Nullable Entity indirectSource) {
        return override(damageType, indirectSource, actualSource);
    }

    @Nullable
    public static DamageSource setToFireDamage(@Nonnull DamageSource src) {
        return changeAttribute(src, DamageSource::setFireDamage);
    }

    @Nullable
    public static DamageSource setToBypassArmor(@Nonnull DamageSource src) {
        return changeAttribute(src, DamageSource::setDamageBypassesArmor);
    }

    @Nullable
    public static DamageSource changeAttribute(@Nonnull DamageSource src, Consumer<DamageSource> update) {
        return overrideWithChanges(src, update);
    }

    private static boolean mayChangeAttributes(DamageSource src) {
        Class<?> srcClass = src.getClass();
        return srcClass.equals(DamageSource.class) || srcClass.equals(EntityDamageSource.class) ||
                srcClass.equals(EntityDamageSourceIndirect.class);
    }

    @Nullable
    private static DamageSource overrideWithChanges(@Nonnull DamageSource source, Consumer<DamageSource> run) {
        DamageSource dst = override(source, null, null);
        if (dst != null) {
            run.accept(dst);
        }
        return dst;
    }

    @Nullable
    private static DamageSource override(DamageSource src, @Nullable Entity directSource, @Nullable Entity trueSource) {
        if (!mayChangeAttributes(src)) {
            return null;
        }
        DamageSource dst;
        if (src.getClass().equals(DamageSource.class)) {
            dst = new DamageSource(src.getDamageType());
        } else if (src.getClass().equals(EntityDamageSource.class)) {
            dst = new EntityDamageSource(src.getDamageType(),
                    directSource != null ? directSource : src.getImmediateSource());
        } else { // equals EntityDamageSourceIndirect.class
            dst = new EntityDamageSourceIndirect(src.getDamageType(),
                    directSource != null ? directSource : src.getImmediateSource(),
                    trueSource != null ? trueSource : (directSource != null ? directSource : src.getTrueSource()));
        }
        copy(src, dst);
        return dst;
    }

    private static void copy(DamageSource src, DamageSource dest) {
        if(src.canHarmInCreative()) {
            dest.setDamageAllowedInCreativeMode();
        }
        if(src.isDamageAbsolute()) {
            dest.setDamageIsAbsolute();
        }
        if(src.isProjectile()) {
            dest.setProjectile();
        }
        if(src.isExplosion()) {
            dest.setExplosion();
        }
        if(src.isFireDamage()) {
            dest.setFireDamage();
        }
        if(src.isMagicDamage()) {
            dest.setMagicDamage();
        }
        if(src.isDifficultyScaled()) {
            dest.setDifficultyScaled();
        }
    }

}
