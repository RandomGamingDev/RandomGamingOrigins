package me.randomgamingdev.randomgamingorigins;

import com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.*;

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
            case Shulk:
                player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(6);
                break;
            default:
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
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
        playerData.feared = false;
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
    public void onEntityResurrectEvent(EntityResurrectEvent event) {
        if (event.isCancelled())
            return;
        Entity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;
        Player player = (Player)entity;
        PlayerData playerData = playersData.get(player.getUniqueId());
        new ApplyEffectsTask(plugin, player, playerData.origin).runTaskLater(plugin, 1);
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
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 4 * 20, 2, true, false));
                break;
            case Evoker:
                if (playerData.abilityTimer > 0)
                    break;
                playerData.abilityTimer = 60;
                event.setCancelled(true);
                for (int i = 0; i < 3; i++)
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
            String playerIdStr = playerId.toString();
            PlayerData playerData = mapEntry.getValue();
            if (playerData.origin == null)
                continue;

            saveData.append(String.format("Player: %s\nOrigin: %d\nOriInv: ",
                    playerIdStr,
                    playerData.origin.ordinal()));

            if (playerData.inventory == null) {
                saveData.append("null\n");
                continue;
            }

            saveData.append("\n");
            try {
                saveData.append(InvSerialize(playerData.inventory));
            }
            catch (Exception e) {
                System.out.println(String.format("RandomGamingOrigins: Failed to save the inventory of %s",
                                    playerIdStr));
                saveData.append("null\n");
            }
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

                        StringBuilder invStr = new StringBuilder();

                        while (myReader.hasNextLine()) {
                            line = myReader.nextLine();
                            if (line.charAt(6) == ':')
                                break;
                            invStr.append(line + '\n');
                        }
                        try {
                            playerData.inventory = InvDeserialize(invStr.toString());
                        }
                        catch (Exception e) {
                            System.out.println(String.format("RandomGamingOrigins: Failed to load the inventory of %s",
                                    playerId.toString()));
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

    public static String InvSerialize(Inventory inventory) throws IOException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++)
                dataOutput.writeObject(inventory.getItem(i));

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IOException("RandomGamingOrigins: Something went wrong while trying to serialize an inventory!", e);
        }
    }

    public static Inventory InvDeserialize(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("RandomGamingOrigins: Something went wrong while trying to deserialize an inventory!", e);
        }
    }
}
