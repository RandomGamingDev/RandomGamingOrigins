package me.randomgamingdev.randomgamingorigins;

import com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static me.randomgamingdev.randomgamingorigins.OriginsGui.createGuiItem;

enum Origin {
    Null("", null, null, 1),
    Avian("Avian",
            createGuiItem(Material.FEATHER, true,
                    "§r§fAvian",
                    "§7- Tailwind: You have speed 1",
                    "§7- Featherweight: You have feather falling 1",
                    "§7- Vegetarian: You can't eat meat"),
            new Object[]{ new Pair(PotionEffectType.SPEED, 0), new Pair(PotionEffectType.SLOW_FALLING, 0) }, 10 * 2),
    Phantom("Phantom",
            createGuiItem(Material.PHANTOM_MEMBRANE, true,
                    "§r§fPhantom",
                    "§7- Phantom Form: Press your offhand",
                    "§7swap key to turn invisible",
                    "§7- Fragile: You have 3 less hearts than normal players"),
            new Object[]{}, 7 * 2),
    Human("Human",
            createGuiItem(Material.PLAYER_HEAD, true,
                    "§r§fHuman",
                    "§7- You're a normal human"),
            new Object[]{}, 10 * 2),
    Elytrian("Elytrian",
            createGuiItem(Material.ELYTRA, true,
                    "§r§fElytrian",
                    "§7- Winged: You have a elytra",
                    "§7- Gift of Wings: You can launch yourself by clicking",
                    "§7your offhand swap key every 60 seconds",
                    "§7- Arial Combat: While flying you deal more damage",
                    "§7- Need for Mobility: You cant wear chestplates"),
            new Object[]{}, 10 * 2),
    Blazeborn("Blazeborn",
            createGuiItem(Material.BLAZE_POWDER, true,
                    "§r§fBlazeborn",
                    "§7- Fire Immunity: You don't take fire damage",
                    "§7- Burning Wrath: You deal more damage while on fire",
                    "§7- Hydrophobia: You take damage in water as if it were lava"),
            new Object[]{ new Pair(PotionEffectType.FIRE_RESISTANCE, 0) }, 10 * 2),
    Feline("Feline",
            createGuiItem(Material.ORANGE_WOOL, true,
                    "§r§fFeline",
                    "§7- Acrobatics: you take no fall damage",
                    "§7- Strong Ankles: You have jump boost 1",
                    "§7- Nine Lives: You have 9 hearts total"),
            new Object[]{ new Pair(PotionEffectType.JUMP, 0) }, 9 * 2),
    Enderian("Enderian",
            createGuiItem(Material.ENDER_PEARL, true,
                    "§r§fEnderian",
                    "§7- Teleportation: Press you offhand swap key to throw a enderpearl",
                    "§7- Hydrophobia: You take damage in water as if it were lava"),
            new Object[]{}, 10 * 2),
    Piglin("Piglin",
            createGuiItem(Material.GOLDEN_SWORD, true,
                    "§r§fPiglin",
                    "§7- Gold Digger: Gold tools deal more damage",
                    "§7- Piglin Lineage: You have 8 hearts instead of 10",
                    "§7- Golden tools break WAY slower",
                    "§7- More features coming soon!"),
            new Object[]{}, 8 * 2),
    Shulk("Shulk",
            createGuiItem(Material.SHULKER_SHELL, true,
                    "§r§fShulk",
                    "§7- Heavy Shell: You loose hunger faster",
                    "§7- Shulker Inventory: By pressing your offhand swap key you",
                    "§7can open up a second inventory with 9 slots that's preserved",
                    "§7even through death",
                    "§7- Hard Shell: You have natural armor",
                    "§7- Unwieldy: You cannot use shields"),
            new Object[]{ new Pair(PotionEffectType.HUNGER, 0), new Pair(PotionEffectType.DAMAGE_RESISTANCE, 0) }, 10 * 2),
    Fox("Fox",
            createGuiItem(Material.SWEET_BERRIES, true,
                    "§r§fFox",
                    "§7- Pounce: Press your offhand swap key to get launched in the air",
                    "§7while falling you deal a lot of damage",
                    "§7- Light Body: You have permanent jump boost 1 and speed 1",
                    "§7- Mighty Mouth: By pressing your offhand swap key and sneaking, thus",
                    "§7allowing you to open up a second inventory with 9 slots that's preserved",
                    "§7even through death",
                    "§7- Acrobatics: You never take fall damage",
                    "§7- Nocturnal: You have permanent night vision",
                    "§7- Berry Craver: You gain 1 extra hunger point from sweat berries",
                    "§7- Fast Metabolism: You loose hunger faster",
                    "§7- Unwieldy: You cannot use shields",
                    "§7- Smaller Heart: You have 6 max hearts"),
            new Object[]{ new Pair(PotionEffectType.HUNGER, 0), new Pair(PotionEffectType.NIGHT_VISION, 0), new Pair(PotionEffectType.JUMP, 0), new Pair(PotionEffectType.SPEED, 1) }, 6 * 2),
    Merling("Merling",
            createGuiItem(Material.COD, true,
                    "§r§fMerling",
                    "§7- Gills: You can breathe under water",
                    "§7- Adaptation: Water invigorates you, allowing you to mine faster",
                    "§7- Fins: You have permanent dolphins grace",
                    "§7- Gills: You cannot breath on land"),
            new Object[]{ new Pair(PotionEffectType.DOLPHINS_GRACE, 0) }, 10 * 2),
    Frog("Frog",
            createGuiItem(Material.SLIME_BALL, true,
                    "§r§fFrog",
                    "§7- Amphibious: You can breathe both in and out of water",
                    "§7- Webbed Feet: You have dolphins grace",
                    "§7- Hippity-Hoppity: You have jump boost 2",
                    "§7- Small Heart: You have 3 less hearts than a normal player",
                    "§7- Unwieldy: You cannot use shields",
                    "§7- Leap of Faith: Press your offhand swap key to get launched into the air"),
            new Object[]{
                    new Pair(PotionEffectType.DOLPHINS_GRACE, 0),
                    new Pair(PotionEffectType.WATER_BREATHING, 0),
                    new Pair(PotionEffectType.JUMP, 1)
            }, 7 * 2),
    Evoker("Evoker",
         createGuiItem(Material.TOTEM_OF_UNDYING, true,
                 "§r§fEvoker",
                 "§7- Feared: You constantly have bad omen 5",
                 "§7- Fearsome: Villagers refuse to trade with you and",
                 "§7iron golems attack on sight",
                 "§7- Ally: You're allies with the rest of the illagers and",
                 "§7their spawn unless you provoke them",
                 "§7- Magician: You can summon vexes to fight for you at will",
                 "§7- Old Age: You have 1 less heart than a normal player and",
                 "§7 permanent slowness 1",
                 "§7- Unwieldy: You cannot use shields"),
            new Object[]{
                new Pair(PotionEffectType.BAD_OMEN, 4),
                new Pair(PotionEffectType.SLOW, 0),
    }, 9 * 2);

    final public String name;
    final public ItemStack item;
    final public Object[] initEffects;
    final public int maxHealth;
    Origin(String name, ItemStack item, Object[] initEffects, int maxHealth) {
        this.name = name;
        this.item = item;
        this.initEffects = initEffects;
        this.maxHealth = maxHealth;
    }
}

public class Origins implements Listener {
    public static RandomGamingOrigins plugin;
    public static HashMap<UUID, PlayerData> playersData = new HashMap<UUID, PlayerData>();
    public static final String saveFileName = "./Origins.data";

    public static final Material[] meats = {
            Material.BEEF, Material.COOKED_BEEF,
            Material.PORKCHOP, Material.COOKED_PORKCHOP,
            Material.MUTTON, Material.COOKED_MUTTON,
            Material.CHICKEN, Material.COOKED_CHICKEN,
            Material.RABBIT, Material.COOKED_RABBIT,
            Material.SALMON, Material.COOKED_SALMON,
            Material.COD, Material.COOKED_COD,
            Material.TROPICAL_FISH,
            Material.PUFFERFISH,
            Material.ROTTEN_FLESH
    };

    public static final Material[] goldenTools = {
            Material.GOLDEN_SWORD,
            Material.GOLDEN_AXE,
            Material.GOLDEN_PICKAXE,
            Material.GOLDEN_SHOVEL,
            Material.GOLDEN_HOE
    };

    public static final EntityType[] illagers = {
            EntityType.EVOKER,
            EntityType.VINDICATOR,
            EntityType.PILLAGER,
            EntityType.VEX,
            EntityType.ILLUSIONER,
            EntityType.RAVAGER
    };

    public static void ApplyOriginMaxHealth(Player player, Origin origin) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(origin.maxHealth);
    }

    public static void ClearEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }

    public static void ApplyOriginEffects(Player player, Origin origin) {
        for (Object effectObj : origin.initEffects) {
            Pair<PotionEffectType, Integer> effect = (Pair<PotionEffectType, Integer>)effectObj;
            player.addPotionEffect(new PotionEffect(effect.first, Integer.MAX_VALUE, effect.second, true, false));
        }
    }

    public static void ApplyOriginInv(PlayerData playerData) {
        Origin origin = playerData.origin;
        switch (origin) {
            case Shulk:
                playerData.inventory = Bukkit.createInventory(null, 1 * 9,
                        String.format("§2Your %s Inventory", origin.name));
                break;
            case Fox:
                playerData.inventory = Bukkit.createInventory(null, 1 * 9,
                        String.format("§2Your %s Inventory", origin.name));
                break;
            default:
                playerData.inventory = null;
                break;
        }
    }

    public static void ApplyOriginAttributes(Player player, Origin origin) {
        switch (origin) {
            default:
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                break;
        }
    }

    public static void ApplyOriginCustom(Player player, Origin origin) {
        PlayerInventory inventory = player.getInventory();
        ItemStack chestplate = inventory.getChestplate();

        if (chestplate != null &&
            chestplate.getType().equals(Material.ELYTRA) &&
            chestplate.getItemMeta().getCustomModelData() == 1)
                inventory.setChestplate(null);

        World world = player.getWorld();
        Location location = player.getLocation();

        switch (origin) {
            case Elytrian:
                if (inventory.getChestplate() != null)
                    for (ItemStack item : inventory.addItem(chestplate).values())
                        world.dropItem(location, item);

                ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
                ItemMeta meta = elytra.getItemMeta();
                meta.setCustomModelData(1);
                meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setDisplayName(String.format("%s's Elytrian Wings", player.getName()));
                elytra.setItemMeta(meta);
                player.getInventory().setChestplate(elytra);
                break;
        }
    }

    public static void DropOriginInv(Player player, PlayerData playerData) {
        if (playerData.inventory != null)
            for (ItemStack item : playerData.inventory.getContents())
                if (item != null)
                    player.getWorld().dropItem(player.getLocation(), item);
    }

    public static void ApplyOrigin(Player player, PlayerData playerData) {
        Origin origin = playerData.origin;
        DropOriginInv(player, playerData);
        ApplyOriginInv(playerData);
        ApplyOriginAttributes(player, playerData.origin);
        ApplyOriginCustom(player, origin);
        ClearEffects(player);
        ApplyOriginEffects(player, origin);
        ApplyOriginMaxHealth(player, origin);
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = playersData.get(player.getUniqueId());
        Entity entity = event.getRightClicked();

        switch (playerData.origin) {
            case Evoker:
                if (entity instanceof Villager)
                    event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity aggressor = event.getDamager();
        if (!(aggressor instanceof Player))
            return;
        Player player = (Player)aggressor;
        PlayerData playerData = playersData.get(player.getUniqueId());
        EntityType victim = event.getEntity().getType();

        switch (playerData.origin) {
            case Evoker:
                for (EntityType illager : illagers)
                    if (victim.equals(illager)) {
                        playerData.feared = true;
                        break;
                    }
                break;
        }
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event) {
        EntityType entity = event.getEntityType();
        Entity target = event.getTarget();
        if (!(target instanceof Player))
            return;
        Player player = (Player)target;
        PlayerData playerData = playersData.get(player.getUniqueId());

        switch (playerData.origin) {
            case Evoker:
                if (playerData.feared)
                    break;
                for (EntityType illager : illagers)
                    if (entity.equals(illager)) {
                        event.setCancelled(true);
                        break;
                    }
                break;
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;

        Player player = (Player)entity;
        PlayerData playerData = Origins.playersData.get(player.getUniqueId());

        if (playerData.removeDeathCause) {
            playerData.deathCause = null;
            playerData.removeDeathCause = false;
        }
        else if (playerData.deathCause != null)
            playerData.removeDeathCause = true;

        switch (playerData.origin) {
            case Feline:
            case Fox:
            case Frog:
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
                    event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        PlayerData playerData = playersData.get(player.getUniqueId());
        if (playerData.deathCause == null)
            return;
        event.setDeathMessage(playerData.deathCause);
        playerData.removeDeathCause = false;
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        ApplyOriginCustom(player, playersData.get(player.getUniqueId()).origin);
        new ApplyEffectsTask(plugin, player, playersData.get(player.getUniqueId()).origin).runTaskLater(plugin, 1);
    }

    @EventHandler
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        Material item = event.getItem().getType();
        if (item == null)
            return;
        Origin origin = playersData.get(player.getUniqueId()).origin;
        switch (origin) {
            case Piglin:
                for (Material tool : goldenTools)
                    if (item == tool && RandomGamingOrigins.rand.nextBoolean())
                        event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null)
            return;
        Material itemType = item.getType();
        Origin origin = playersData.get(player.getUniqueId()).origin;
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            switch (origin) {

            }
            return;
        }
        switch (origin) {
            case Shulk:
            case Fox:
            case Frog:
            case Evoker:
                if (!itemType.equals(Material.SHIELD))
                    break;
                player.sendMessage(String.format("%s's can't use shields!", origin.name));

                Location location = player.getLocation();
                World world = player.getWorld();
                PlayerInventory inventory = player.getInventory();
                if (inventory.getItemInMainHand().getType().equals(Material.SHIELD))
                    inventory.setItemInMainHand(null);
                else
                    inventory.setItemInOffHand(null);
                world.dropItem(location, item);
                break;
        }
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Origin origin = playersData.get(player.getUniqueId()).origin;
        Material itemType = event.getItem().getType();

        if (itemType.equals(Material.MILK_BUCKET)) {
            new ApplyEffectsTask(plugin, player, origin).runTaskLater(plugin, 1);
            return;
        }

        switch (origin) {
            case Avian:
                for (Material meat : meats)
                    if (itemType == meat) {
                        player.sendMessage(String.format("%s's can't eat meat!", origin.name));
                        event.setCancelled(true);
                        break;
                    }
                break;
            case Fox:
                if (itemType.equals(Material.SWEET_BERRIES))
                    player.setFoodLevel(player.getFoodLevel() + 1);
                break;
        }
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = playersData.get(player.getUniqueId());
        Origin origin = playerData.origin;

        switch (origin) {
            case Fox:
                if (player.isSneaking()) {
                    event.setCancelled(true);
                    player.openInventory(playerData.inventory);
                    break;
                }
                if (playerData.abilityTimer > 0)
                    break;
                event.setCancelled(true);
                playerData.abilityTimer = 60;
                player.setVelocity(player.getVelocity().setY(1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 1, true, false));
                break;
            case Shulk:
                event.setCancelled(true);
                player.openInventory(playerData.inventory);
                break;
            case Enderian:
                if (playerData.abilityTimer > 0)
                    break;
                playerData.abilityTimer = 60;
                event.setCancelled(true);
                player.launchProjectile(EnderPearl.class);
                break;
            case Elytrian:
                if (playerData.abilityTimer > 0)
                    break;
                playerData.abilityTimer = 60;
                event.setCancelled(true);
                player.setVelocity(player.getVelocity().setY(2));
                break;
            case Phantom:
                event.setCancelled(true);
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                else
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));
                break;
            case Frog:
                if (playerData.abilityTimer > 0)
                    break;
                playerData.abilityTimer = 60;
                event.setCancelled(true);
                player.setVelocity(player.getVelocity().setY(2));
                break;
            case Evoker:
                if (playerData.abilityTimer > 0)
                    break;
                playerData.abilityTimer = 60;
                event.setCancelled(true);
                player.getWorld().spawnEntity(player.getLocation(), EntityType.VEX);
                break;
        }
    }

    public static boolean Save(String saveFileName) {
        StringBuilder saveData = new StringBuilder();

        for (Map.Entry<UUID, PlayerData> mapEntry : Origins.playersData.entrySet()) {
            UUID playerId = mapEntry.getKey();
            if (playerId == null)
                continue;
            PlayerData playerData = mapEntry.getValue();
            if (playerData.origin == null)
                continue;

            saveData.append(String.format("Player: %s\nOrigin: %d\nOriInv: ",
                    playerId.toString(),
                    playerData.origin.ordinal()));

            if (playerData.inventory == null) {
                saveData.append("null\n");
                continue;
            }

            for (ItemStack item : playerData.inventory.getContents())
                if (item != null)
                    saveData.append('\n' + RandomGamingOrigins.gson.toJson(item.serialize()));
            saveData.append("\n");
        }

        File saveFile = new File(saveFileName);
        try {
            if (saveFile.createNewFile())
                System.out.println("RandomGamingOrigins: New save file created!");
            FileWriter myWriter = new FileWriter(saveFileName);
            myWriter.write(saveData.toString());
            myWriter.close();
            System.out.println("RandomGamingOrigins: Saved successfully!");
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println(
                    String.format("RandomGamingOrigins: Something went wrong while trying to save from %s!",
                            saveFileName));
            return false;
        }
    }

    public static boolean Load(String saveFileName) {
        File saveFile = new File(saveFileName);
        if (!saveFile.exists())
            return false;
        try {
            UUID playerId = null;
            PlayerData playerData = new PlayerData();

            Scanner myReader = new Scanner(saveFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String type = line.substring(0, 8);
                String data = line.substring(8);

                switch (type) {
                    case "Player: ":
                        playerId = UUID.fromString(data);
                        break;
                    case "Origin: ":
                        playerData.origin = Origin.values()[Integer.parseInt(data)];
                        ApplyOriginInv(playerData);
                        break;
                    case "OriInv: ":
                        if (data.equals("null")) {
                            Origins.playersData.put(playerId, playerData);
                            playerId = null;
                            playerData = new PlayerData();
                            break;
                        }
                        while (myReader.hasNextLine()) {
                            line = myReader.nextLine();
                            if (line.charAt(0) != '{')
                                break;
                            playerData.inventory.addItem(ItemStack.deserialize(RandomGamingOrigins.gson.fromJson(line, new TypeToken<Map<String, Object>>(){}.getType())));
                        }
                        Origins.playersData.put(playerId, playerData);
                        playerId = null;
                        playerData = new PlayerData();
                        break;
                }
            }
            myReader.close();
            System.out.println(
                    String.format("RandomGamingOrigins: Loaded successfully from %s!",
                            saveFileName));
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println(
                    String.format("RandomGamingOrigins: Something went wrong while trying to load the save from %s!",
                        saveFileName));
            return false;
        }
    }
}
