package io.github.traelmyx.betterburning.test.fabric;

//? if fabric {
/*import io.github.traelmyx.betterburning.test.BurningTests;
import net.minecraft.gametest.framework.GameTestHelper;
//? if >=1.21.5 {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//?} else {
/^import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
^///?}

//? if >=1.21.5 {
public class FabricGameTests {
	private static final String EMPTY = "betterburning_gametest:empty";

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void fireResistanceExtinguishes(GameTestHelper helper) {
		BurningTests.fireResistanceExtinguishes(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void fireResistanceExtinguishesOff(GameTestHelper helper) {
		BurningTests.fireResistanceExtinguishesOff(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void fireDeathDropsCookedFood(GameTestHelper helper) {
		BurningTests.fireDeathDropsCookedFood(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void fireDeathDropsCookedFoodOff(GameTestHelper helper) {
		BurningTests.fireDeathDropsCookedFoodOff(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void fireSpreadsByHits(GameTestHelper helper) {
		BurningTests.fireSpreadsByHits(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void fireSpreadsByHitsOff(GameTestHelper helper) {
		BurningTests.fireSpreadsByHitsOff(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void burningSkeletonsShootFlamingArrows(GameTestHelper helper) {
		BurningTests.burningSkeletonsShootFlamingArrows(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void burningSkeletonsShootFlamingArrowsOff(GameTestHelper helper) {
		BurningTests.burningSkeletonsShootFlamingArrowsOff(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void punchingFireBurns(GameTestHelper helper) {
		BurningTests.punchingFireBurns(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void punchingFireBurnsOff(GameTestHelper helper) {
		BurningTests.punchingFireBurnsOff(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void waterBottlePutsFireOut(GameTestHelper helper) {
		BurningTests.waterBottlePutsFireOut(helper);
	}

	@GameTest(structure = EMPTY, maxTicks = 100)
	public void waterBottlePutsFireOutOff(GameTestHelper helper) {
		BurningTests.waterBottlePutsFireOutOff(helper);
	}

}
//?} else {
/^public class FabricGameTests implements FabricGameTest {
	private static final String EMPTY = "betterburning_gametest:empty";

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "fire_resistance_extinguishes")
	public void fireResistanceExtinguishes(GameTestHelper helper) {
		BurningTests.fireResistanceExtinguishes(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "fire_resistance_extinguishes_off")
	public void fireResistanceExtinguishesOff(GameTestHelper helper) {
		BurningTests.fireResistanceExtinguishesOff(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "fire_death_drops_cooked_food")
	public void fireDeathDropsCookedFood(GameTestHelper helper) {
		BurningTests.fireDeathDropsCookedFood(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "fire_death_drops_cooked_food_off")
	public void fireDeathDropsCookedFoodOff(GameTestHelper helper) {
		BurningTests.fireDeathDropsCookedFoodOff(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "fire_spreads_by_hits")
	public void fireSpreadsByHits(GameTestHelper helper) {
		BurningTests.fireSpreadsByHits(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "fire_spreads_by_hits_off")
	public void fireSpreadsByHitsOff(GameTestHelper helper) {
		BurningTests.fireSpreadsByHitsOff(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "burning_skeletons_shoot_flaming_arrows")
	public void burningSkeletonsShootFlamingArrows(GameTestHelper helper) {
		BurningTests.burningSkeletonsShootFlamingArrows(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "burning_skeletons_shoot_flaming_arrows_off")
	public void burningSkeletonsShootFlamingArrowsOff(GameTestHelper helper) {
		BurningTests.burningSkeletonsShootFlamingArrowsOff(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "punching_fire_burns")
	public void punchingFireBurns(GameTestHelper helper) {
		BurningTests.punchingFireBurns(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "punching_fire_burns_off")
	public void punchingFireBurnsOff(GameTestHelper helper) {
		BurningTests.punchingFireBurnsOff(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "water_bottle_puts_fire_out")
	public void waterBottlePutsFireOut(GameTestHelper helper) {
		BurningTests.waterBottlePutsFireOut(helper);
	}

	@GameTest(template = EMPTY, timeoutTicks = 100, batch = "water_bottle_puts_fire_out_off")
	public void waterBottlePutsFireOutOff(GameTestHelper helper) {
		BurningTests.waterBottlePutsFireOutOff(helper);
	}

}
^///?}
*///?}
