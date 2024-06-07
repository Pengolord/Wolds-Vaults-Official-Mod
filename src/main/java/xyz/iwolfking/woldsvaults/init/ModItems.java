package xyz.iwolfking.woldsvaults.init;

import iskallia.vault.VaultMod;
import iskallia.vault.item.BasicMobEggItem;
import iskallia.vault.item.BasicScavengerItem;
import iskallia.vault.item.ItemVaultCrystalSeal;
import iskallia.vault.item.LootableItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import xyz.iwolfking.woldsvaults.WoldsVaults;
import xyz.iwolfking.woldsvaults.config.VaultAmuletConfig;
import xyz.iwolfking.woldsvaults.items.AltarResetItem;
import xyz.iwolfking.woldsvaults.items.ExpertiseOrbItem;
import xyz.iwolfking.woldsvaults.items.SkillOrbItem;
import xyz.iwolfking.woldsvaults.items.gear.VaultAmuletItem;
import xyz.iwolfking.woldsvaults.items.gear.VaultBattleStaffItem;
import xyz.iwolfking.woldsvaults.items.gear.VaultTridentItem;
import xyz.iwolfking.woldsvaults.items.rings.*;

import java.util.Random;

import static iskallia.vault.init.ModItems.GEAR_GROUP;
import static iskallia.vault.init.ModItems.VAULT_MOD_GROUP;

@Mod.EventBusSubscriber(modid = WoldsVaults.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static ItemVaultCrystalSeal CRYSTAL_SEAL_MONOLITH;
    public static ItemVaultCrystalSeal CRYSTAL_SEAL_UNHINGED;

    public static ItemVaultCrystalSeal CRYSTAL_SEAL_SPIRITS;
    public static ItemVaultCrystalSeal CRYSTAL_SEAL_ENCHANTER;

    public static ItemVaultCrystalSeal CRYSTAL_SEAL_TITAN;
    public static VaultBattleStaffItem BATTLESTAFF;
    //public static VaultBowItem BOW;

    public static VaultTridentItem TRIDENT;

    public static VaultAmuletItem VAULT_AMULET;

    public static LootableItem GEM_BOX;
    public static LootableItem SUPPLY_BOX;
    public static LootableItem AUGMENT_BOX;

    public static LootableItem INSCRIPTION_BOX;
    public static LootableItem OMEGA_BOX;
    public static LootableItem CATALYST_BOX;
    public static LootableItem ENIGMA_EGG;

    public static BasicScavengerItem BENITOITE_GEMSTONE;
    public static BasicScavengerItem WUTODIC_GEMSTONE;
    public static BasicScavengerItem ECHOING_GEMSTONE;
    public static BasicScavengerItem POGGING_GEMSTONE;
    public static BasicScavengerItem ENDER_HEART;
    public static BasicScavengerItem ENDER_EYE;
    public static BasicScavengerItem ENDER_CRYSTAL;
    public static BasicScavengerItem ENDER_ARTIFACT;

    public static final BasicMobEggItem WOLD_EGG = new BasicMobEggItem(WoldsVaults.id("wold_spawn_egg"), () -> (EntityType)ModEntities.WOLD, 1447446, DyeColor.GRAY.getId(), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static final BasicMobEggItem BOOGIEMAN_EGG = new BasicMobEggItem(WoldsVaults.id("boogieman_spawn_egg"), () -> (EntityType) iskallia.vault.init.ModEntities.BOOGIEMAN, 333333, DyeColor.GRAY.getId(), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static final BasicMobEggItem MONSTER_EYE_EGG = new BasicMobEggItem(WoldsVaults.id("monster_eye_spawn_egg"), () -> (EntityType) iskallia.vault.init.ModEntities.MONSTER_EYE, 333333, DyeColor.GRAY.getId(), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static final BasicMobEggItem ROBOT_EGG = new BasicMobEggItem(WoldsVaults.id("robot_spawn_egg"), () -> (EntityType) iskallia.vault.init.ModEntities.ROBOT, 333333, DyeColor.GRAY.getId(), (new Item.Properties()).tab(VAULT_MOD_GROUP));
    public static final BasicMobEggItem BLUE_BLAZE_EGG = new BasicMobEggItem(WoldsVaults.id("blue_blaze_spawn_egg"), () -> (EntityType) iskallia.vault.init.ModEntities.BLUE_BLAZE, 333333, DyeColor.GRAY.getId(), (new Item.Properties()).tab(VAULT_MOD_GROUP));

    public static final AltarResetItem ALTAR_DECATALYZER;

    //Angel Rings
    public static final ChromaticIronAngelRing CHROMATIC_IRON_ANGEL_RING;
    public static final ChromaticGoldAngelRing CHROMATIC_GOLD_ANGEL_RING;
    public static final ChromaticSteelAngelRing CHROMATIC_STEEL_ANGEL_RING;
    public static final BlackChromaticSteelAngelRing BLACK_CHROMATIC_STEEL_ANGEL_RING;
    public static final PrismaticAngelRing PRISMATIC_ANGEL_RING;

    //Orbs
    public static final ExpertiseOrbItem EXPERTISE_ORB_ITEM;
    public static final SkillOrbItem SKILL_ORB_ITEM;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(CRYSTAL_SEAL_MONOLITH);
        registry.register(CRYSTAL_SEAL_UNHINGED);
        registry.register(CRYSTAL_SEAL_SPIRITS);
        registry.register(CRYSTAL_SEAL_ENCHANTER);
        registry.register(CRYSTAL_SEAL_TITAN);
        registry.register(BATTLESTAFF);
        registry.register(TRIDENT);
        registry.register(GEM_BOX);
        registry.register(SUPPLY_BOX);
        registry.register(AUGMENT_BOX);
        registry.register(INSCRIPTION_BOX);
        registry.register(OMEGA_BOX);
        //registry.register(CATALYST_BOX);
        registry.register(ENIGMA_EGG);
        registry.register(BENITOITE_GEMSTONE);
        registry.register(WUTODIC_GEMSTONE);
        registry.register(ECHOING_GEMSTONE);
        registry.register(POGGING_GEMSTONE);
        registry.register(ENDER_HEART);
        registry.register(ENDER_EYE);
        registry.register(ENDER_ARTIFACT);
        registry.register(ENDER_CRYSTAL);
        registry.register(ALTAR_DECATALYZER);
        registry.register(VAULT_AMULET);
        registry.register(CHROMATIC_IRON_ANGEL_RING);
        registry.register(CHROMATIC_GOLD_ANGEL_RING);
        registry.register(CHROMATIC_STEEL_ANGEL_RING);
        registry.register(BLACK_CHROMATIC_STEEL_ANGEL_RING);
        registry.register(PRISMATIC_ANGEL_RING);
        registry.register(EXPERTISE_ORB_ITEM);
        registry.register(SKILL_ORB_ITEM);
        registry.register(WOLD_EGG);
        registry.register(BOOGIEMAN_EGG);
        registry.register(ROBOT_EGG);
        registry.register(MONSTER_EYE_EGG);
        registry.register(BLUE_BLAZE_EGG);
        //registry.register(BOW);
    }

    static {
        CRYSTAL_SEAL_MONOLITH = new ItemVaultCrystalSeal(VaultMod.id("crystal_seal_monolith"));
        CRYSTAL_SEAL_UNHINGED = new ItemVaultCrystalSeal(VaultMod.id("crystal_seal_unhinged"));
        CRYSTAL_SEAL_SPIRITS = new ItemVaultCrystalSeal(VaultMod.id("crystal_seal_spirits"));
        CRYSTAL_SEAL_ENCHANTER = new ItemVaultCrystalSeal(VaultMod.id("crystal_seal_enchanter"));
        CRYSTAL_SEAL_TITAN = new ItemVaultCrystalSeal(VaultMod.id("crystal_seal_titan"));
        BATTLESTAFF = new VaultBattleStaffItem(VaultMod.id("battlestaff"), (new Item.Properties()).tab(GEAR_GROUP).stacksTo(1));
        TRIDENT = new VaultTridentItem(VaultMod.id("trident"), (new Item.Properties()).tab(GEAR_GROUP).stacksTo(1));
        GEM_BOX = new LootableItem(VaultMod.id("gem_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> (ModConfigs.GEM_BOX.POOL.getRandom(new Random())).get().generateItemStack());
        SUPPLY_BOX = new LootableItem(VaultMod.id("supply_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> (ModConfigs.SUPPLY_BOX.POOL.getRandom(new Random())).get().generateItemStack());
        AUGMENT_BOX = new LootableItem(VaultMod.id("augment_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> (ModConfigs.AUGMENT_BOX.POOL.getRandom(new Random())).get().generateItemStack());
        INSCRIPTION_BOX = new LootableItem(WoldsVaults.id("inscription_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> (ModConfigs.INSCRIPTION_BOX.POOL.getRandom(new Random())).get().generateItemStack());
        OMEGA_BOX = new LootableItem(WoldsVaults.id("omega_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> (ModConfigs.OMEGA_BOX.POOL.getRandom(new Random())).get().generateItemStack());
        //CATALYST_BOX = new LootableItem(WoldsVaults.id("catalyst_box"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> (ModConfigs.CATALYST_BOX.POOL.getRandom(new Random())).get().generateItemStack());
        ENIGMA_EGG = new LootableItem(WoldsVaults.id("enigma_egg"), (new Item.Properties()).tab(VAULT_MOD_GROUP), () -> (ModConfigs.ENIGMA_EGG.POOL.getRandom(new Random())).get().generateItemStack());
        BENITOITE_GEMSTONE = new BasicScavengerItem("benitoite_gemstone");
        WUTODIC_GEMSTONE = new BasicScavengerItem("wutodic_gemstone");
        ECHOING_GEMSTONE = new BasicScavengerItem("echoing_gemstone");
        POGGING_GEMSTONE = new BasicScavengerItem("pogging_gemstone");
        ENDER_HEART = new BasicScavengerItem("ender_heart");
        ENDER_ARTIFACT = new BasicScavengerItem("ender_artifact");
        ENDER_CRYSTAL = new BasicScavengerItem("ender_crystal");
        ENDER_EYE = new BasicScavengerItem("enderman_eye");
        ALTAR_DECATALYZER = new AltarResetItem(VaultMod.id("altar_recatalyzer"), (new Item.Properties().tab(VAULT_MOD_GROUP).rarity(Rarity.RARE)));
        VAULT_AMULET = new VaultAmuletItem(VaultMod.id("amulet"), VaultAmuletConfig.Size.SMALL);
        CHROMATIC_IRON_ANGEL_RING = new ChromaticIronAngelRing();
        CHROMATIC_GOLD_ANGEL_RING= new ChromaticGoldAngelRing();
        CHROMATIC_STEEL_ANGEL_RING = new ChromaticSteelAngelRing();
        BLACK_CHROMATIC_STEEL_ANGEL_RING = new BlackChromaticSteelAngelRing();
        PRISMATIC_ANGEL_RING = new PrismaticAngelRing();
        EXPERTISE_ORB_ITEM = new ExpertiseOrbItem(WoldsVaults.id("expertise_orb"));
        SKILL_ORB_ITEM = new SkillOrbItem(WoldsVaults.id("skill_orb"));
        //BOW = new VaultBowItem(VaultMod.id("bow"), (new Item.Properties().tab(GEAR_GROUP).stacksTo(1)));
    }
}
