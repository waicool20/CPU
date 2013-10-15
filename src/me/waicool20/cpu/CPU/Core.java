package me.waicool20.cpu.CPU;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Core {
    private final Block block;
    private final Block output;
    private Inventory coreInventory;

    Core(Block block) {
        this.block = block;
        org.bukkit.material.Chest chest = (org.bukkit.material.Chest) block.getState().getData();
        this.output = block.getRelative(chest.getFacing().getOppositeFace(), 1);
        this.coreInventory = getCore_INV();
    }

    public Block getBlock() {
        return block;
    }

    public Block getOutput() {
        return output;
    }

    public Inventory getInventory() {
        return coreInventory;
    }

    private Inventory getCore_INV() {
        InventoryHolder inventoryHolder = (Chest) block.getState();
        return inventoryHolder.getInventory();
    }
}
