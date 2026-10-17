package io.github.traelmyx.betterburning.test;

import io.github.traelmyx.betterburning.BetterBurning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/** Each feature is tested with its switch on, and again with it off, where vanilla behaviour must show. */
public final class BurningTests {
	private BurningTests() {
	}

	private static void floor(GameTestHelper helper) {
		for (int x = 0; x < 8; x++) for (int z = 0; z < 8; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
	}

	private static <T extends Mob> T mob(GameTestHelper helper, String id, int x, int z) {
		//? if >=26 {
		/*var type = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace(id));
		*///?} else if >=1.21.11 {
		/*var type = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace(id));
		*///?} else
		var type = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(net.minecraft.resources.ResourceLocation.withDefaultNamespace(id));
		@SuppressWarnings("unchecked")
		T mob = (T) helper.spawn(type, new BlockPos(x, 1, z));
		mob.setNoAi(true);
		return mob;
	}

	private static void hurt(LivingEntity target, DamageSource source, float amount) {
		//? if >=1.21.11 {
		/*target.hurtServer((net.minecraft.server.level.ServerLevel) target.level(), source, amount);
		*///?} else
		target.hurt(source, amount);
	}

	private static void finish(GameTestHelper helper, net.minecraft.world.entity.Entity... vanilla) {
		for (var entity : vanilla) BetterBurning.VANILLA.remove(entity.getUUID());
		helper.succeed();
	}

	/** Keeps an entity on vanilla behaviour for a control test. */
	private static <T extends net.minecraft.world.entity.Entity> T vanilla(T entity, boolean on) {
		if (!on) BetterBurning.VANILLA.add(entity.getUUID());
		return entity;
	}

	// Fire resistance puts the flames out

	private static void fireResistance(GameTestHelper helper, boolean on) {
		floor(helper);
		Mob cow = vanilla(mob(helper, "cow", 3, 3), on);
		cow.igniteForSeconds(10);
		cow.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200));
		helper.runAfterDelay(3, () -> {
			if (on) helper.assertTrue(!cow.isOnFire(), "a cow with fire resistance is still on fire");
			else helper.assertTrue(cow.isOnFire(), "vanilla put the flames out by itself");
			finish(helper, cow);
		});
	}

	public static void fireResistanceExtinguishes(GameTestHelper helper) {
		fireResistance(helper, true);
	}

	public static void fireResistanceExtinguishesOff(GameTestHelper helper) {
		fireResistance(helper, false);
	}

	// Mobs killed by fire damage drop cooked food

	private static void cooked(GameTestHelper helper, boolean on) {
		floor(helper);
		Mob cow = vanilla(mob(helper, "cow", 3, 3), on);
		hurt(cow, helper.getLevel().damageSources().inFire(), 1000);
		helper.runAfterDelay(2, () -> {
			int cookedBeef = 0, rawBeef = 0;
			for (ItemEntity item : helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(helper.absolutePos(BlockPos.ZERO)).inflate(10))) {
				if (item.getItem().is(Items.COOKED_BEEF)) cookedBeef += item.getItem().getCount();
				if (item.getItem().is(Items.BEEF)) rawBeef += item.getItem().getCount();
				item.discard();
			}
			if (on) helper.assertTrue(cookedBeef > 0 && rawBeef == 0, "a cow killed by fire dropped " + cookedBeef + " cooked and " + rawBeef + " raw beef");
			else helper.assertTrue(rawBeef > 0 && cookedBeef == 0, "vanilla dropped " + cookedBeef + " cooked and " + rawBeef + " raw beef");
			finish(helper, cow);
		});
	}

	public static void fireDeathDropsCookedFood(GameTestHelper helper) {
		cooked(helper, true);
	}

	public static void fireDeathDropsCookedFoodOff(GameTestHelper helper) {
		cooked(helper, false);
	}

	// A burning mob hitting with an empty hand, or anything hitting with flint and steel, sets the target alight

	private static void spread(GameTestHelper helper, boolean on) {
		floor(helper);
		BetterBurning.forcedChance = 1.0;
		Mob burning = mob(helper, "cow", 1, 1), target = vanilla(mob(helper, "pig", 2, 1), on);
		burning.setRemainingFireTicks(200);
		hurt(target, helper.getLevel().damageSources().mobAttack(burning), 0.5F);
		boolean caught = target.isOnFire();
		Mob holder = mob(helper, "skeleton", 5, 5), victim = vanilla(mob(helper, "sheep", 6, 5), on);
		holder.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FLINT_AND_STEEL));
		hurt(victim, helper.getLevel().damageSources().mobAttack(holder), 0.5F);
		boolean lit = victim.isOnFire();
		if (on) helper.assertTrue(caught && lit, "burning hit set target alight: " + caught + ", flint and steel hit: " + lit);
		else helper.assertTrue(!caught && !lit, "vanilla set targets alight: " + caught + ", " + lit);
		finish(helper, target, victim);
	}

	public static void fireSpreadsByHits(GameTestHelper helper) {
		spread(helper, true);
	}

	public static void fireSpreadsByHitsOff(GameTestHelper helper) {
		spread(helper, false);
	}

	// Burning skeletons shoot flaming arrows

	private static void arrows(GameTestHelper helper, boolean on) {
		floor(helper);
		BetterBurning.forcedChance = 1.0;
		Mob skeleton = vanilla(mob(helper, "skeleton", 2, 1), on), target = mob(helper, "zombie", 2, 7);
		skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
		skeleton.setRemainingFireTicks(100);
		((net.minecraft.world.entity.monster.RangedAttackMob) skeleton).performRangedAttack(target, 1.0F);
		int burning = 0, arrowsSeen = 0;
		for (var arrow : helper.getLevel().getEntitiesOfClass(net.minecraft.world.entity.projectile.Projectile.class, new AABB(helper.absolutePos(BlockPos.ZERO)).inflate(16))) {
			if (arrow.getOwner() == skeleton) {
				arrowsSeen++;
				if (arrow.isOnFire()) burning++;
			}
			arrow.discard();
		}
		helper.assertTrue(arrowsSeen == 1, "expected one arrow, saw " + arrowsSeen);
		if (on) helper.assertTrue(burning == 1, "the burning skeleton's arrow was not on fire");
		else helper.assertTrue(burning == 0, "vanilla shot a burning arrow");
		finish(helper, skeleton);
	}

	public static void burningSkeletonsShootFlamingArrows(GameTestHelper helper) {
		arrows(helper, true);
	}

	public static void burningSkeletonsShootFlamingArrowsOff(GameTestHelper helper) {
		arrows(helper, false);
	}

	// Players

	private static ServerPlayer player(GameTestHelper helper, String name) {
		var server = helper.getLevel().getServer();
		var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), name), false);
		var player = new ServerPlayer(server, helper.getLevel(), cookie.gameProfile(), cookie.clientInformation());
		var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
		new io.netty.channel.embedded.EmbeddedChannel(connection);
		server.getPlayerList().placeNewPlayer(connection, player, cookie);
		//? if >=1.21.11 {
		/*player.connection.handleAcceptPlayerLoad(new net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket());
		*///?}
		player.setGameMode(GameType.SURVIVAL);
		Vec3 at = helper.absoluteVec(new Vec3(1.5, 1, 1.5));
		player.setPos(at.x, at.y, at.z);
		return player;
	}

	// Punching out fire burns you

	private static void punch(GameTestHelper helper, boolean on) {
		floor(helper);
		ServerPlayer player = vanilla(player(helper, "test-puncher"), on);
		BlockPos fire = new BlockPos(3, 1, 3);
		helper.setBlock(fire, Blocks.FIRE);
		player.gameMode.destroyBlock(helper.absolutePos(fire));
		boolean burning = player.isOnFire();
		helper.getLevel().getServer().getPlayerList().remove(player);
		helper.assertBlockPresent(Blocks.AIR, fire);
		if (on) helper.assertTrue(burning, "punching out fire did not burn the player");
		else helper.assertTrue(!burning, "vanilla burned the player for punching out fire");
		finish(helper, player);
	}

	public static void punchingFireBurns(GameTestHelper helper) {
		punch(helper, true);
	}

	public static void punchingFireBurnsOff(GameTestHelper helper) {
		punch(helper, false);
	}

	// A water bottle puts fire out

	private static void bottle(GameTestHelper helper, boolean on) {
		floor(helper);
		ServerPlayer player = vanilla(player(helper, "test-bottle"), on);
		BlockPos fire = new BlockPos(3, 1, 3);
		helper.setBlock(fire, Blocks.FIRE);
		ItemStack water = PotionContents.createItemStack(Items.POTION, Potions.WATER);
		player.setItemInHand(InteractionHand.MAIN_HAND, water);
		BlockPos at = helper.absolutePos(fire);
		player.gameMode.useItemOn(player, helper.getLevel(), water, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(at), Direction.UP, at, false));
		boolean out = helper.getLevel().getBlockState(at).isAir();
		boolean bottleBack = player.getInventory().countItem(Items.GLASS_BOTTLE) == 1;
		helper.getLevel().getServer().getPlayerList().remove(player);
		if (on) helper.assertTrue(out && bottleBack && water.isEmpty(), "fire out: " + out + ", bottle back: " + bottleBack + ", potion used: " + water.isEmpty());
		else helper.assertTrue(!out, "vanilla put the fire out with a water bottle");
		finish(helper, player);
	}

	public static void waterBottlePutsFireOut(GameTestHelper helper) {
		bottle(helper, true);
	}

	public static void waterBottlePutsFireOutOff(GameTestHelper helper) {
		bottle(helper, false);
	}
}
