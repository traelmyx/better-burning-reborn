package io.github.traelmyx.betterburning.mixin;

import io.github.traelmyx.betterburning.BetterBurning;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "baseTick", at = @At("HEAD"))
	private void betterburning$fireResistance(CallbackInfo ci) {
		BetterBurning.tick((LivingEntity) (Object) this);
	}

	@Inject(method = "die", at = @At("HEAD"))
	private void betterburning$cookedDrops(DamageSource source, CallbackInfo ci) {
		BetterBurning.dying((LivingEntity) (Object) this, source);
	}

	//? if >=1.21.11 {
	/*@Inject(method = "hurtServer", at = @At("HEAD"))
	private void betterburning$attacked(net.minecraft.server.level.ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		BetterBurning.attacked((LivingEntity) (Object) this, source);
	}
	*///?} else {
	@Inject(method = "hurt", at = @At("HEAD"))
	private void betterburning$attacked(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		BetterBurning.attacked((LivingEntity) (Object) this, source);
	}
	//?}
}
