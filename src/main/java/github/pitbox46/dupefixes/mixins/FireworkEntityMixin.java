package github.pitbox46.dupefixes.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalInt;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkEntityMixin extends Entity {
    public FireworkEntityMixin(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Shadow protected abstract boolean isAttachedToEntity();

    @Shadow private LivingEntity boostedEntity;

    @Shadow @Final private static DataParameter<OptionalInt> BOOSTED_ENTITY_ID;

    @Inject(at = @At(value = "HEAD"), method = "func_213893_k()V", cancellable = true)
    public void onExplode(CallbackInfo ci) {
        if(isAttachedToEntity() && boostedEntity instanceof ServerPlayerEntity && ((ServerPlayerEntity) boostedEntity).hasDisconnected()) {
            this.boostedEntity = null;
            this.dataManager.set(BOOSTED_ENTITY_ID, OptionalInt.empty());
        }
    }
}
