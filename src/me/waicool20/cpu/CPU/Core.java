package me.waicool20.cpu.CPU;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Core {
    private final Block block;
    private Inventory coreInventory;

    Core(Block block) {
        this.block = block;
        this.coreInventory = getCore_INV();
    }

    public Block getBlock() {
        return block;
    }

    public Inventory getInventory() {
        return coreInventory;
    }

    private Inventory getCore_INV() {
        InventoryHolder inventoryHolder = (Chest) block.getState();
        return inventoryHolder.getInventory();
    }
}
