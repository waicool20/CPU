package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU.CPU;
import me.waicool20.cpu.CPUDatabase;
import me.waicool20.cpu.CPUPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import java.util.ArrayList;

public class RedstoneUpdatesListener implements Listener {
    private ArrayList<Block> list = new ArrayList<Block>();
    private final BlockFace[] adjFaces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRedstoneChange(BlockPhysicsEvent e) {
        Block block = e.getBlock();
        if (CPUPlugin.plugin.getConfig().getBoolean("disabled")) {
            return;
        }
        if (check(block)) list.add(block);
        if (list.contains(e.getBlock())) {
            for (CPU cpu : CPUDatabase.CPUDatabaseMap) {
                if (cpu.isBlockPartOfCPU(block)) {
                    cpu.getType().updatePower();
                    list.clear();
                    return;
                }
            }
        }
    }

    private boolean check(Block block) {
        if (block.getType() == Material.CHEST) return true;
        for (BlockFace blockFace : adjFaces) {
            if (block.getRelative(blockFace).getType() == Material.CHEST) return true;
        }
        return false;
    }

}
