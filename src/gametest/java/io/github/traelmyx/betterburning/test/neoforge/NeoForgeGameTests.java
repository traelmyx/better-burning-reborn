package io.github.traelmyx.betterburning.test.neoforge;

//? if neoforge {
import io.github.traelmyx.betterburning.test.ClientSmoke;
import io.github.traelmyx.betterburning.test.BurningTests;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
//? if >=1.21.5 {
/*import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
*///?} else {
import net.minecraft.gametest.framework.GameTest;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
//?}

//? if <1.21.5 {
@GameTestHolder(NeoForgeGameTests.MOD_ID)
@PrefixGameTestTemplate(false)
//?}
@Mod(NeoForgeGameTests.MOD_ID)
public class NeoForgeGameTests {
	public static final String MOD_ID = "betterburning_gametest";

	public NeoForgeGameTests(IEventBus modBus, Dist dist) {
		//? if >=1.21.5 {
		/*FUNCTIONS.register(modBus);
		modBus.addListener(NeoForgeGameTests::registerTests);
		*///?}
		if (dist.isClient() && ClientSmoke.start()) {
			NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> ClientSmoke.tick(net.minecraft.client.Minecraft.getInstance()));
		}
	}

	//? if >=1.21.5 {
	/*private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(Registries.TEST_FUNCTION, MOD_ID);
	private static final Identifier EMPTY = Identifier.fromNamespaceAndPath(MOD_ID, "empty");

	static {
		FUNCTIONS.register("fire_resistance_extinguishes", () -> BurningTests::fireResistanceExtinguishes);
		FUNCTIONS.register("fire_resistance_extinguishes_off", () -> BurningTests::fireResistanceExtinguishesOff);
		FUNCTIONS.register("fire_death_drops_cooked_food", () -> BurningTests::fireDeathDropsCookedFood);
		FUNCTIONS.register("fire_death_drops_cooked_food_off", () -> BurningTests::fireDeathDropsCookedFoodOff);
		FUNCTIONS.register("fire_spreads_by_hits", () -> BurningTests::fireSpreadsByHits);
		FUNCTIONS.register("fire_spreads_by_hits_off", () -> BurningTests::fireSpreadsByHitsOff);
		FUNCTIONS.register("burning_skeletons_shoot_flaming_arrows", () -> BurningTests::burningSkeletonsShootFlamingArrows);
		FUNCTIONS.register("burning_skeletons_shoot_flaming_arrows_off", () -> BurningTests::burningSkeletonsShootFlamingArrowsOff);
		FUNCTIONS.register("punching_fire_burns", () -> BurningTests::punchingFireBurns);
		FUNCTIONS.register("punching_fire_burns_off", () -> BurningTests::punchingFireBurnsOff);
		FUNCTIONS.register("water_bottle_puts_fire_out", () -> BurningTests::waterBottlePutsFireOut);
		FUNCTIONS.register("water_bottle_puts_fire_out_off", () -> BurningTests::waterBottlePutsFireOutOff);
	}

	private static void registerTests(RegisterGameTestsEvent event) {
		//? if >=26 {
		Holder<TestEnvironmentDefinition<?>> env = event.registerEnvironment(Identifier.fromNamespaceAndPath(MOD_ID, "default"));
		//?} else
		/^Holder<TestEnvironmentDefinition> env = event.registerEnvironment(Identifier.fromNamespaceAndPath(MOD_ID, "default"));^/
		register(event, env, "fire_resistance_extinguishes", 100, 1, 1);
		register(event, env, "fire_resistance_extinguishes_off", 100, 1, 1);
		register(event, env, "fire_death_drops_cooked_food", 100, 1, 1);
		register(event, env, "fire_death_drops_cooked_food_off", 100, 1, 1);
		register(event, env, "fire_spreads_by_hits", 100, 1, 1);
		register(event, env, "fire_spreads_by_hits_off", 100, 1, 1);
		register(event, env, "burning_skeletons_shoot_flaming_arrows", 100, 1, 1);
		register(event, env, "burning_skeletons_shoot_flaming_arrows_off", 100, 1, 1);
		register(event, env, "punching_fire_burns", 100, 1, 1);
		register(event, env, "punching_fire_burns_off", 100, 1, 1);
		register(event, env, "water_bottle_puts_fire_out", 100, 1, 1);
		register(event, env, "water_bottle_puts_fire_out_off", 100, 1, 1);
	}

	//? if >=26 {
	private static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, int maxTicks, int attempts, int successes) {
	//?} else
	/^private static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition> env, String name, int maxTicks, int attempts, int successes) {^/
		Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, name);
		event.registerTest(id, new FunctionGameTestInstance(ResourceKey.create(Registries.TEST_FUNCTION, id),
			//? if >=26.3 {
			new TestData<>(env, net.minecraft.world.level.Level.OVERWORLD, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false, 0)));
			//?} else if >=26 {
			/^new TestData<>(env, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false, 0)));
			^///?} else {
			/^new TestData<>(env, EMPTY, maxTicks, 0, true,
				net.minecraft.world.level.block.Rotation.NONE, false, attempts, successes, false)));
			^///?}
	}
	*///?} else {
	@GameTest(template = "empty", timeoutTicks = 100, batch = "fire_resistance_extinguishes")
	public static void fireResistanceExtinguishes(GameTestHelper helper) {
		BurningTests.fireResistanceExtinguishes(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "fire_resistance_extinguishes_off")
	public static void fireResistanceExtinguishesOff(GameTestHelper helper) {
		BurningTests.fireResistanceExtinguishesOff(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "fire_death_drops_cooked_food")
	public static void fireDeathDropsCookedFood(GameTestHelper helper) {
		BurningTests.fireDeathDropsCookedFood(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "fire_death_drops_cooked_food_off")
	public static void fireDeathDropsCookedFoodOff(GameTestHelper helper) {
		BurningTests.fireDeathDropsCookedFoodOff(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "fire_spreads_by_hits")
	public static void fireSpreadsByHits(GameTestHelper helper) {
		BurningTests.fireSpreadsByHits(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "fire_spreads_by_hits_off")
	public static void fireSpreadsByHitsOff(GameTestHelper helper) {
		BurningTests.fireSpreadsByHitsOff(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "burning_skeletons_shoot_flaming_arrows")
	public static void burningSkeletonsShootFlamingArrows(GameTestHelper helper) {
		BurningTests.burningSkeletonsShootFlamingArrows(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "burning_skeletons_shoot_flaming_arrows_off")
	public static void burningSkeletonsShootFlamingArrowsOff(GameTestHelper helper) {
		BurningTests.burningSkeletonsShootFlamingArrowsOff(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "punching_fire_burns")
	public static void punchingFireBurns(GameTestHelper helper) {
		BurningTests.punchingFireBurns(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "punching_fire_burns_off")
	public static void punchingFireBurnsOff(GameTestHelper helper) {
		BurningTests.punchingFireBurnsOff(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "water_bottle_puts_fire_out")
	public static void waterBottlePutsFireOut(GameTestHelper helper) {
		BurningTests.waterBottlePutsFireOut(helper);
	}

	@GameTest(template = "empty", timeoutTicks = 100, batch = "water_bottle_puts_fire_out_off")
	public static void waterBottlePutsFireOutOff(GameTestHelper helper) {
		BurningTests.waterBottlePutsFireOutOff(helper);
	}

	//?}
}
//?}
