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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class InventoryChangeListener implements Listener {

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
    public void onChestOpen(PlayerInteractEvent e) {
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
                e.setCancelled(true);
            }
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
}
