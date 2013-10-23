package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUDatabase;
import me.waicool20.cpu.CPUPlugin;
import me.waicool20.cpu.CraftingAndRecipes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CreateCPUListener implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();

        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getItem() == null || clickedBlock == null) {
            return;
        }
        if (!e.getItem().isSimilar(CraftingAndRecipes.redstoneActivator())) {
            return;
        }
        e.setCancelled(true);

        createCPU(player, clickedBlock, false);
    }

    private static boolean hasPermission(Player player, CPU cpu) {
        return player.isOp() || player.hasPermission("cpu.create." + cpu.getType().getName().toLowerCase());
    }

    private static boolean isEmpty(ItemStack[] ie) {
        for (ItemStack item : ie) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }

    private static boolean alreadyActivated(CPU newCpu) {
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            if (newCpu.getLocation().equals(cpu.getLocation())) {
                return true;
            }
        }
        return false;
    }

    static void createCPU(Player player, Block clickedBlock, boolean typified) {
        if (clickedBlock.getType() != Material.CHEST) {
            player.sendMessage(ChatColor.RED + "[CPU] Please use the activator on CPUs only!!");
            return;
        }
        InventoryHolder inventoryHolder = (Chest) clickedBlock.getState();
        ItemStack[] clickedBlockContents = inventoryHolder.getInventory().getContents();

        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            if (clickedBlock.equals(cpu.getInput1().getBlock()) || clickedBlock.equals(cpu.getInput2().getBlock())) {
                player.sendMessage(ChatColor.RED + "[CPU] This is a part of a existing CPU!");
                return;
            }
        }

        if (isEmpty(clickedBlockContents)) {
            player.sendMessage(ChatColor.RED + "[CPU] Unable to activate a " + ChatColor.DARK_RED + "EMPTY " + ChatColor.RED + "CPU!");
            return;
        }

        CPU newCpu = new CPU(player.getName(), clickedBlock.getWorld(), clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());

        newCpu.setTypified(typified);

        if (newCpu.getType() == null) {
            player.sendMessage(ChatColor.RED + "[CPU] Invalid CPU Type!     " + ChatColor.GREEN + "HINT:  Check Inventory!");
            return;
        }

        if (!hasPermission(player, newCpu)) {
            player.sendMessage(ChatColor.RED + "[CPU] You do not have the permission to create a " + ChatColor.DARK_RED + newCpu.getType().getName() + ChatColor.RED + " CPU");
            return;
        }

        if (!newCpu.isValid()) {
            player.sendMessage(ChatColor.RED + "[CPU] This CPU seems to be missing some inputs?");
            return;
        }

        if (alreadyActivated(newCpu)) {
            for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
                if (clickedBlock.getLocation().equals(cpu.getLocation())) {
                    newCpu.sendCPUInfo(player);
                }
            }
            return;
        }

        CPUDatabase.addCPU(newCpu);

        newCpu.getType().updatePower();

        player.sendMessage(ChatColor.GREEN + "[CPU] You have successfully activated this CPU!!");

        if (!CPUPlugin.plugin.getConfig().getBoolean("send-info")) {
            return;
        }
        newCpu.sendCPUInfo(player);
    }
}
