package io.github.traelmyx.betterburning.fabric;

//? if fabric {
/*import io.github.traelmyx.betterburning.BetterBurning;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.InteractionResult;

public class BetterBurningFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		BetterBurning.loadConfig(FabricLoader.getInstance().getConfigDir());
		UseBlockCallback.EVENT.register((player, level, hand, hit) ->
			BetterBurning.bottleOnFire(player, level, hit.getBlockPos(), player.getItemInHand(hand)) ? InteractionResult.SUCCESS : InteractionResult.PASS);
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> BetterBurning.breakingFire(player, state));
	}
}
*///?}
