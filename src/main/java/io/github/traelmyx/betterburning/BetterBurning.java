package io.github.traelmyx.betterburning;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
//? if >=1.21.11 {
/*import net.minecraft.world.entity.monster.zombie.Zombie;
*///?} else
import net.minecraft.world.entity.monster.Zombie;

/**
 * Port of Better Burning by Darkhax (LGPL-2.1, https://github.com/Darkhax-Minecraft/Better-Burning), which stops at
 * Minecraft 1.20.4. Small fire tweaks, each with a config switch. The client-only option that hid the fire overlay for
 * fire-immune players is not ported; fire resistance putting out the flames covers the usual case.
 */
public final class BetterBurning {
	public static final String MOD_ID = "betterburning";
	public static final Logger LOGGER = LoggerFactory.getLogger("Better Burning Reborn");

	public static boolean fireResistanceExtinguish = true;
	public static boolean fireFromDamageSource = true;
	public static boolean fireDamageSpreads = true;
	public static double fireDamageSpreadChance = 0.3;
	public static boolean flameArrowSkeletons = true;
	public static double flameArrowSkeletonChance = 0.7;
	public static boolean flintAndSteelDoesFireDamage = true;
	public static int flintAndSteelFireDamage = 3;
	public static int fireHitBurnTime = 1;
	public static int soulfireHitBurnTime = 2;
	public static boolean punchOutFlames = true;
	public static boolean extinguishWithBottledWater = true;

	/** Lets the tests force the random chances. */
	public static Double forcedChance = null;
	/** Entities the game tests keep on vanilla behaviour, to show each change really is this mod's doing. */
	public static final java.util.Set<java.util.UUID> VANILLA = java.util.concurrent.ConcurrentHashMap.newKeySet();

	private BetterBurning() {
	}

	static boolean roll(double chance) {
		return ThreadLocalRandom.current().nextDouble() < (forcedChance != null ? forcedChance : chance);
	}

	/** Every tick: fire resistance puts the flames out instead of only hiding the damage. */
	public static void tick(LivingEntity entity) {
		if (fireResistanceExtinguish && !VANILLA.contains(entity.getUUID()) && !entity.level().isClientSide() && entity.isOnFire() && entity.hasEffect(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE))
			entity.clearFire();
	}

	/** A mob killed by fire damage counts as burning, so it drops cooked food even if the flames never caught. */
	public static void dying(LivingEntity entity, net.minecraft.world.damagesource.DamageSource source) {
		if (fireFromDamageSource && !VANILLA.contains(entity.getUUID()) && !entity.level().isClientSide() && source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE) && !entity.isOnFire())
			entity.igniteForSeconds(1);
	}

	/** A burning attacker with an empty hand can set its target alight; flint and steel used as a weapon always does. */
	public static void attacked(LivingEntity target, net.minecraft.world.damagesource.DamageSource source) {
		if (!fireDamageSpreads && !flintAndSteelDoesFireDamage || VANILLA.contains(target.getUUID()) || !(target.level() instanceof ServerLevel level))
			return;
		if (!(source.getEntity() instanceof LivingEntity attacker))
			return;
		ItemStack held = attacker.getMainHandItem();
		if (fireDamageSpreads && !(attacker instanceof Zombie) && held.isEmpty() && attacker.isOnFire() && roll(fireDamageSpreadChance)) {
			float difficulty = Math.max(1, level.getCurrentDifficultyAt(target.blockPosition()).getEffectiveDifficulty());
			target.igniteForSeconds(2 * (int) difficulty);
		}
		if (flintAndSteelDoesFireDamage && held.is(Items.FLINT_AND_STEEL))
			target.igniteForSeconds(flintAndSteelFireDamage);
	}

	/** A skeleton that is on fire may shoot a burning arrow. */
	public static void skeletonArrow(LivingEntity skeleton, Entity arrow) {
		if (flameArrowSkeletons && !VANILLA.contains(skeleton.getUUID()) && !skeleton.level().isClientSide() && skeleton.isOnFire() && skeleton.isAlive() && roll(flameArrowSkeletonChance))
			arrow.setRemainingFireTicks(skeleton.getRemainingFireTicks());
	}

	/** Breaking a fire block by hand burns you a little; returns false if it may not be broken at all. */
	public static boolean breakingFire(Player player, BlockState state) {
		if (player.level().isClientSide() || VANILLA.contains(player.getUUID())) return true;
		int seconds = state.is(Blocks.FIRE) ? fireHitBurnTime : state.is(Blocks.SOUL_FIRE) ? soulfireHitBurnTime : 0;
		if (seconds > 0) player.igniteForSeconds(seconds);
		return !(state.getBlock() instanceof BaseFireBlock) || punchOutFlames;
	}

	/** Using a water bottle on fire puts it out and gives the bottle back; true if it did. */
	public static boolean bottleOnFire(Player player, Level level, BlockPos pos, ItemStack stack) {
		if (!extinguishWithBottledWater || level.isClientSide() || VANILLA.contains(player.getUUID())) return false;
		BlockState state = level.getBlockState(pos);
		if (!state.is(Blocks.FIRE) && !state.is(Blocks.SOUL_FIRE)) return false;
		PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
		if (!stack.is(Items.POTION) || contents == null || !contents.is(Potions.WATER)) return false;
		level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		level.levelEvent(null, 1009, pos, 0);
		stack.shrink(1);
		ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
		if (!player.getInventory().add(bottle))
			level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(level, player.getX(), player.getY(), player.getZ(), bottle));
		return true;
	}

	public static void loadConfig(Path configDir) {
		Path file = configDir.resolve(MOD_ID + ".properties");
		Properties props = new Properties();
		if (Files.exists(file)) {
			try (Reader reader = Files.newBufferedReader(file)) {
				props.load(reader);
			} catch (IOException e) {
				LOGGER.warn("Could not read {}, using defaults", file, e);
			}
		}
		loadFrom(props);
		try {
			Files.createDirectories(configDir);
			try (Writer writer = Files.newBufferedWriter(file)) {
				props.store(writer, "Better Burning Reborn. Chances are 0 to 1, times are in seconds.");
			}
		} catch (IOException e) {
			LOGGER.warn("Could not write {}", file, e);
		}
	}

	private static void loadFrom(Properties props) {
		fireResistanceExtinguish = bool(props, "fireResistanceExtinguish", true);
		fireFromDamageSource = bool(props, "fireFromDamagesource", true);
		fireDamageSpreads = bool(props, "fireDamageSpreads", true);
		fireDamageSpreadChance = number(props, "fireDamageSpreadChance", 0.3);
		flameArrowSkeletons = bool(props, "flameArrowSkeletons", true);
		flameArrowSkeletonChance = number(props, "flameArrowSkeletonChance", 0.7);
		flintAndSteelDoesFireDamage = bool(props, "ShouldFlintAndSteelDoFireDamage", true);
		flintAndSteelFireDamage = (int) number(props, "flintAndSteelFireDamage", 3);
		fireHitBurnTime = (int) number(props, "fireHitBurnTime", 1);
		soulfireHitBurnTime = (int) number(props, "soulfireHitBurnTime", 2);
		punchOutFlames = bool(props, "punchOutFlames", true);
		extinguishWithBottledWater = bool(props, "extinguishWithBottledWater", true);
	}

	private static boolean bool(Properties props, String key, boolean def) {
		boolean value = Boolean.parseBoolean(props.getProperty(key, Boolean.toString(def)).trim());
		props.setProperty(key, Boolean.toString(value));
		return value;
	}

	private static double number(Properties props, String key, double def) {
		double value;
		try {
			value = Math.max(0, Double.parseDouble(props.getProperty(key, Double.toString(def)).trim()));
		} catch (NumberFormatException e) {
			value = def;
		}
		props.setProperty(key, value == Math.rint(value) && def == Math.rint(def) ? Integer.toString((int) value) : Double.toString(value));
		return value;
	}
}
