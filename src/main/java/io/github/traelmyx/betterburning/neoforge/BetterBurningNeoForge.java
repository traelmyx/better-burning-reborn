package io.github.traelmyx.betterburning.neoforge;

//? if neoforge {
import io.github.traelmyx.betterburning.BetterBurning;
import net.minecraft.world.InteractionResult;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(BetterBurning.MOD_ID)
public class BetterBurningNeoForge {
	public BetterBurningNeoForge() {
		BetterBurning.loadConfig(FMLPaths.CONFIGDIR.get());
		NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
			if (BetterBurning.bottleOnFire(event.getEntity(), event.getLevel(), event.getPos(), event.getItemStack())) {
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.SUCCESS);
			}
		});
		//? if >=26 {
		/*NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.event.level.block.BreakBlockEvent event) -> {
		*///?} else
		NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.event.level.BlockEvent.BreakEvent event) -> {
			if (event.getPlayer() != null && !BetterBurning.breakingFire(event.getPlayer(), event.getState()))
				event.setCanceled(true);
		});
	}
}
//?}
