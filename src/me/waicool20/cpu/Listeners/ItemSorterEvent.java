package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPU.Types.ItemSorter;
import me.waicool20.cpu.CPUDatabase;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ItemSorterEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void HopperPassOnEvent(InventoryMoveItemEvent e) {
        Chest chest;
        Hopper hopper;
        ItemStack passedItem = e.getItem();
        if (e.getDestination().getType() == InventoryType.HOPPER && e.getSource().getType() == InventoryType.CHEST) {
            chest = (Chest) e.getSource().getHolder();
            for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
                if (cpu.getCore().getBlock().equals(chest.getBlock())) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if (e.getDestination().getType() == InventoryType.CHEST && e.getInitiator().getType() == InventoryType.HOPPER) {
            chest = (Chest) e.getDestination().getHolder();
            hopper = (Hopper) e.getInitiator().getHolder();
        } else return;

        Block block = chest.getBlock();
        chest.update();
        for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
            if (block.equals(cpu.getCore().getBlock())) {
                if (cpu.getType() instanceof ItemSorter) {
                    ItemSorter itemSorter = (ItemSorter) cpu.getType();
                    Block botBlock = cpu.getCore().getBlock().getRelative(BlockFace.DOWN);
                    itemSorter.updateList();
                    if (itemSorter.getLeftIDList().contains(passedItem.getTypeId())) {
                        cpu.getInput1().getInventory().addItem(passedItem);
                        e.getDestination().remove(e.getItem());
                    } else if (itemSorter.getRightIDList().contains(passedItem.getTypeId())) {
                        cpu.getInput2().getInventory().addItem(passedItem);
                        e.getDestination().remove(e.getItem());
                    } else if (botBlock.getType() == Material.HOPPER) {
                        Hopper botChest = (Hopper) botBlock.getState();
                        botChest.getInventory().addItem(passedItem);
                        e.getDestination().remove(e.getItem());
                        botChest.update();
                    } else {
                        e.setCancelled(true);
                    }
                    passedItem.setType(Material.AIR);
                } else {
                    e.setCancelled(true);
                }
            }
            break;
        }
    }

}
