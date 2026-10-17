package io.github.traelmyx.betterburning.mixin;

import io.github.traelmyx.betterburning.BetterBurning;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >=1.21.11 {
/*import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
*///?} else {
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
//?}

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin {
	@Inject(method = "getArrow", at = @At("RETURN"))
	private void betterburning$flameArrow(ItemStack arrow, float velocity, ItemStack weapon, CallbackInfoReturnable<AbstractArrow> cir) {
		BetterBurning.skeletonArrow((AbstractSkeleton) (Object) this, cir.getReturnValue());
	}
}
