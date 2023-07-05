package me.randomgamingdev.randomgamingorigins.core;

import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import me.randomgamingdev.randomgamingorigins.core.tasks.ApplyEffectsTask;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
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

public class OriginManager implements Listener {
    public static RandomGamingOrigins plugin;
    public static HashMap<UUID, PlayerData> playersData = new HashMap<UUID, PlayerData>();
    public static final String saveFileName = "Origins.data";

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

    public static final EntityType[] nonUndeadHerobrineEntities = {
            EntityType.SPIDER,
            EntityType.CREEPER,
    };

    public static boolean IsHerobrineEntity(LivingEntity entity) {
        if (entity.getCategory().equals(EntityCategory.UNDEAD))
            return true;
        for (EntityType nonUndeadHerobrineEntity : nonUndeadHerobrineEntities)
            if (entity.getType().equals(nonUndeadHerobrineEntity))
                return true;
        return false;
    }

    public static final int elytraCode = 1; //4372198
    public static final int originOrbCode = 1; //721398

    public static void ApplyOrigin(Player player, PlayerData playerData) {
        Origin origin = playerData.origin;

        playerData.feared = false;
        origin.origin.dropInv(player, playerData);
        origin.origin.applyInv(player, playerData);
        origin.origin.applyAttribs(player, playerData);
        origin.origin.applyCustom(player, playerData);
        origin.origin.clearEffects(player, playerData);
        origin.origin.applyEffects(player, playerData);
        origin.origin.applyMaxHealth(player, playerData);
    }

    public static PlayerData GetPlayerData(String name) { // Either the username of an online player or UUID
        final Player possiblePlayer = Bukkit.getPlayer(name);
        PlayerData playerData;
        if (possiblePlayer == null) {
            try {
                playerData = playersData.get(UUID.fromString(name));
                if (playerData == null)
                    return null;
            }
            catch (Exception error) {
                return null;
            }
        }
        else
            playerData = GetPlayerData(possiblePlayer);
        return playerData;
    }

    public static PlayerData GetPlayerData(Player player) {
        return playersData.get(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerInteractEntityEvent(event, playerData);
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerChangedWorldEvent(event, playerData);
    }

    @EventHandler
    public void onPlayerBedEnderEvent(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerBedEnterEvent(event, playerData);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity aggressor = event.getDamager();
        Entity victim = event.getEntity();
        if (aggressor.getUniqueId() == victim.getUniqueId()) // we're testing for whether the entity's damaging itself here
            return;

        // both of these assume that it isn't the player attacking themselves
        if (aggressor instanceof Player) {
            Player player = (Player)aggressor;
            PlayerData playerData = GetPlayerData(player);
            playerData.origin.origin.onEntityDamageByPlayerEvent(event, playerData);
        }

        if (victim instanceof Player) {
            Player player = (Player)victim;
            PlayerData playerData = GetPlayerData(player);
            playerData.origin.origin.onPlayerDamageByEntityEvent(event, playerData);
        }
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event) {
        EntityType entity = event.getEntityType();
        Entity target = event.getTarget();
        if (!(target instanceof Player))
            return;

        Player player = (Player)target;
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onEntityTargetPlayerEvent(event, playerData);
    }

    @EventHandler
    public void onEntityResurrectEvent(EntityResurrectEvent event) {
        if (event.isCancelled())
            return;
        Entity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;

        Player player = (Player)entity;
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerResurrectEvent(event, playerData);
        new ApplyEffectsTask(plugin, player, playerData.origin).runTaskLater(plugin, 1);
    }
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;

        Player player = (Player)entity;
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerDamageEvent(event, playerData);

        if (playerData.removeDeathCause) {
            playerData.deathCause = null;
            playerData.removeDeathCause = false;
        }
        else if (playerData.deathCause != null)
            playerData.removeDeathCause = true;
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerDeathEvent(event, playerData);

        if (playerData.deathCause == null)
            return;
        event.setDeathMessage(playerData.deathCause);
        playerData.removeDeathCause = false;
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerRespawnEvent(event, playerData);

        playerData.origin.origin.applyCustom(player, playerData);
        new ApplyEffectsTask(plugin, player, GetPlayerData(player).origin).runTaskLater(plugin, 1);
    }

    @EventHandler
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerItemDamageEvent(event, playerData);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        PlayerInventory inventory = player.getInventory();
        ItemStack handItem = inventory.getItemInMainHand();
        if (handItem.getType() == Material.SLIME_BALL && handItem.getItemMeta().getCustomModelData() == originOrbCode) {
            OriginsGui.Gui(player, true);
            handItem.setAmount(handItem.getAmount() - 1);
            return;
        }

        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerInteractEvent(event, playerData);
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = GetPlayerData(player);
        Origin origin = playerData.origin;
        Material itemType = event.getItem().getType();

        if (itemType.equals(Material.MILK_BUCKET)) {
            new ApplyEffectsTask(plugin, player, origin).runTaskLater(plugin, 1);
            return;
        }

        playerData.origin.origin.onPlayerItemConsumeEvent(event, playerData);
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = GetPlayerData(player);
        playerData.origin.origin.onPlayerSwapHandItemsEvent(event, playerData);
    }

    public static boolean Save(String saveFileName) {
        StringBuilder saveData = new StringBuilder();

        for (Map.Entry<UUID, PlayerData> mapEntry : OriginManager.playersData.entrySet()) {
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

            if (playerData.pouch == null) {
                saveData.append("null\n");
                continue;
            }

            saveData.append("\n");
            try {
                saveData.append(InvSerialize(playerData.pouch));
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
                System.out.println(
                        String.format("RandomGamingOrigins: New save named %s file created!",
                                saveFileName));
            FileWriter myWriter = new FileWriter(saveFile);
            myWriter.write(saveData.toString());
            myWriter.close();
            System.out.println(
                    String.format("RandomGamingOrigins: Saved successfully to %s!",
                            saveFileName));
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println(
                    String.format("RandomGamingOrigins: Something went wrong while trying to save to %s!",
                            saveFileName));
            return false;
        }
    }

    public static boolean Load(String saveFileName) {
        File saveFile = new File(saveFileName);
        if (!saveFile.exists()) {
            System.out.println(
                    String.format("RandomGamingOrigins: %s doesn't exist!",
                            saveFileName));
            return false;
        }
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
                        playerData.origin.origin.applyInv(Bukkit.getPlayer(playerId), playerData);
                        break;
                    case "OriInv: ":
                        if (data.equals("null")) {
                            OriginManager.playersData.put(playerId, playerData);
                            playerId = null;
                            playerData = new PlayerData();
                            break;
                        }

                        StringBuilder invStr = new StringBuilder();

                        while (myReader.hasNextLine()) {
                            line = myReader.nextLine();
                            if (line.length() >= 7 && line.charAt(6) == ':')
                                break;
                            invStr.append(line + '\n');
                        }
                        try {
                            playerData.pouch = InvDeserialize(invStr.toString());
                        }
                        catch (Exception e) {
                            System.out.println(String.format("RandomGamingOrigins: Failed to load the inventory of %s",
                                    playerId.toString()));
                        }

                        OriginManager.playersData.put(playerId, playerData);
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