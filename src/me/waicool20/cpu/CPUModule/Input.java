package me.waicool20.cpu.CPUModule;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Input {
    private final BlockFace[] adjFaces = {BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST,BlockFace.UP,BlockFace.DOWN};

    private final Block block;
    private final Inventory inputInventory;

    Input(Block block) {
        this.block = block;
        this.inputInventory = getInv();
    }

    public Block getBlock() {
        return block;
    }

    private Inventory getInv(){
        InventoryHolder inventoryHolder = (Chest) block.getState();
        return inventoryHolder.getInventory();
    }

    public Inventory getInventory() {
        return inputInventory;
    }

    public boolean isPowered(){
        if(this.block.isBlockIndirectlyPowered()){return true;}
        for(BlockFace blockFace: adjFaces){
            Block adjblock = block.getRelative(blockFace);
            if(adjblock.getType() == Material.REDSTONE_WIRE) {return true;}
            if(adjblock.isBlockPowered() && adjblock.getType() != Material.AIR){return true;}
        }
        return false;
    }

    Location getLocation(){
        return block.getLocation();
    }

}
