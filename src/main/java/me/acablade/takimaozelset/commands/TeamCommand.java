package me.acablade.takimaozelset.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import me.acablade.takimaozelset.TakimaOzelSetPlugin;
import me.acablade.takimaozelset.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TeamCommand implements CommandExecutor {

    private TakimaOzelSetPlugin plugin;

    public TeamCommand(TakimaOzelSetPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        // /team asda

        if(!(commandSender instanceof Player)) return false;
        if(args.length > 1) return false;

        Player player = (Player) commandSender;

        if("one".equalsIgnoreCase(args[0])){
            plugin.getTeamManager().getTeamOne().add(player.getUniqueId());
            plugin.getTeamManager().getTeamTwo().remove(player.getUniqueId());
            player.sendMessage(ChatColor.YELLOW + "Başarıyla 1. takıma katıldın!");
            sendEquipmentPacket(player, plugin.getTeamManager().getTeamOne());
        }else if("two".equalsIgnoreCase(args[0])){
            plugin.getTeamManager().getTeamTwo().add(player.getUniqueId());
            plugin.getTeamManager().getTeamOne().remove(player.getUniqueId());
            player.sendMessage(ChatColor.YELLOW + "Başarıyla 2. takıma katıldın!");
            sendEquipmentPacket(player, plugin.getTeamManager().getTeamTwo());
        }

        return true;
    }

    private static final ItemStack ALLY_HELMET = new ItemBuilder(Material.LEATHER_HELMET).color(Color.GREEN).build();
    private static final ItemStack ALLY_CHESTPLATE = new ItemBuilder(Material.LEATHER_CHESTPLATE).color(Color.GREEN).build();
    private static final ItemStack ALLY_LEGGINGS = new ItemBuilder(Material.LEATHER_LEGGINGS).color(Color.GREEN).build();
    private static final ItemStack ALLY_BOOTS = new ItemBuilder(Material.LEATHER_BOOTS).color(Color.GREEN).build();

    private static final ItemStack ENEMY_HELMET = new ItemBuilder(Material.LEATHER_HELMET).color(Color.RED).build();
    private static final ItemStack ENEMY_CHESTPLATE = new ItemBuilder(Material.LEATHER_CHESTPLATE).color(Color.RED).build();
    private static final ItemStack ENEMY_LEGGINGS = new ItemBuilder(Material.LEATHER_LEGGINGS).color(Color.RED).build();
    private static final ItemStack ENEMY_BOOTS = new ItemBuilder(Material.LEATHER_BOOTS).color(Color.RED).build();

    private static final List<Pair<EnumWrappers.ItemSlot,ItemStack>> ENEMY_SLOT_ITEM_PAIRS = Arrays.asList(
            new Pair(EnumWrappers.ItemSlot.HEAD, ENEMY_HELMET),
            new Pair(EnumWrappers.ItemSlot.CHEST, ENEMY_CHESTPLATE),
            new Pair(EnumWrappers.ItemSlot.LEGS, ENEMY_LEGGINGS),
            new Pair(EnumWrappers.ItemSlot.FEET, ENEMY_BOOTS)
    );

    private static final List<Pair<EnumWrappers.ItemSlot,ItemStack>> ALLY_SLOT_ITEM_PAIRS = Arrays.asList(
            new Pair(EnumWrappers.ItemSlot.HEAD, ALLY_HELMET),
            new Pair(EnumWrappers.ItemSlot.CHEST, ALLY_CHESTPLATE),
            new Pair(EnumWrappers.ItemSlot.LEGS, ALLY_LEGGINGS),
            new Pair(EnumWrappers.ItemSlot.FEET, ALLY_BOOTS)
    );

    private void sendEquipmentPacket(Player playerToSend, Set<UUID> teammates){


        Bukkit.getOnlinePlayers().forEach(player -> {

            if(player==playerToSend) return;

            // PACKET TO SEND TO playerToSend
            boolean teammate = teammates.contains(player.getUniqueId());
            PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
            packetContainer.getIntegers().write(0,player.getEntityId());
            if(teammate){
                packetContainer.getSlotStackPairLists().write(0,ALLY_SLOT_ITEM_PAIRS);
            }else{
                packetContainer.getSlotStackPairLists().write(0,ENEMY_SLOT_ITEM_PAIRS);
            }
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(playerToSend,packetContainer);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            // PACKET TO SEND TO ALLIES AND ENEMIES
            packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
            packetContainer.getIntegers().write(0,playerToSend.getEntityId());
            if(teammate){
                packetContainer.getSlotStackPairLists().write(0,ALLY_SLOT_ITEM_PAIRS);
            }else{
                packetContainer.getSlotStackPairLists().write(0,ENEMY_SLOT_ITEM_PAIRS);
            }
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player,packetContainer);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        });

    }

}
