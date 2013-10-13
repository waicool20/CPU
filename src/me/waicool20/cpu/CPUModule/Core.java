package me.waicool20.cpu.CPUModule;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Core {
    private Block block;
    private Block output;
    private Inventory coreInventory;

    Core(Block block) {
        this.block = block;
        org.bukkit.material.Chest chest = (org.bukkit.material.Chest) block.getState().getData();
        this.output = block.getRelative(chest.getFacing().getOppositeFace(),1);
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

    public void setCoreInventory(Inventory coreInventory) {
        this.coreInventory = coreInventory;
    }

    private Inventory getCore_INV(){
        InventoryHolder inventoryHolder = (Chest) block.getState();
        return inventoryHolder.getInventory();
    }
}
