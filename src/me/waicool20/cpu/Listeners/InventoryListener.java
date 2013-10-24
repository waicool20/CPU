package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUDatabase;
import me.waicool20.cpu.CraftingAndRecipes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryListener implements Listener {
    public static ArrayList<String> players = new ArrayList<String>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryInteract(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.CHEST) {
            return;
        }
        if (!(e.getRawSlot() == e.getSlot())) {
            return;
        }
        Block block = ((Chest) e.getInventory().getHolder()).getBlock();
        ItemStack[] contents = e.getView().getTopInventory().getContents();
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            if (!cpu.isBlockPartOfCPU(block)) {
                continue;
            }
            if (Arrays.deepEquals(contents, cpu.getCore().getInventory().getContents())) {
                cpu.getType().disable();
                CPUDatabase.removeCPU(cpu);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void chestClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = e.getClickedBlock();
        Player player = e.getPlayer();
        if (block.getType() != Material.CHEST) {
            return;
        }
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            if (cpu.getCore().getBlock().equals(block) && cpu.isTypified()) {
                if(e.getItem() != null){
                    if(!e.getItem().isSimilar(CraftingAndRecipes.typifier())) player.sendMessage(ChatColor.RED + "[CPU] Don't open a typified chest!");
                    e.setCancelled(true);
                }
            }
            //TODO Add permissions for opening other peoples CPU
            if (cpu.isBlockPartOfCPU(block)) {
                if (!(player.getName().equalsIgnoreCase(cpu.getAttributes().getOwner()))) {
                    e.setCancelled(true);
                    if (e.getItem() == null) {
                        return;
                    }
                    if (!e.getItem().isSimilar(CraftingAndRecipes.typifier())) {
                        player.sendMessage(ChatColor.RED + "[CPU] You are not the owner of this CPU!");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen(InventoryOpenEvent e) {
        if (!players.contains(e.getPlayer().getName())) {
            players.add(e.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestClose(InventoryCloseEvent e) {
        if (players.contains(e.getPlayer().getName())) {
            players.remove(e.getPlayer().getName());
        }
    }
}
