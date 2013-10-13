package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPU;
import me.waicool20.cpu.CPUModule.CPUModule;
import me.waicool20.cpu.ModuleDatabase;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import java.util.ArrayList;

public class RedstoneUpdatesListener implements Listener {
    ArrayList<Block> list = new ArrayList<Block>();
    private final BlockFace[] adjFaces = {BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST,BlockFace.UP,BlockFace.DOWN};

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRedstoneChange(BlockPhysicsEvent e) {
        if(CPU.plugin.getConfig().getBoolean("Disabled")){return;}
        if(check(e.getBlock())) list.add(e.getBlock());
        if(list.contains(e.getBlock())){
            startUpdate(e.getBlock());
            list.remove(e.getBlock());
        }
    }

    private void startUpdate(Block block){
        for(CPUModule cpuModule : ModuleDatabase.ModuleDatabaseMap){
            if(cpuModule.isBlockPartOfModule(block)){
                cpuModule.getType().updatePower();
                return;
            }
        }
    }

    private boolean check(Block block){
        for(CPUModule cpuModule: ModuleDatabase.ModuleDatabaseMap){
            if(cpuModule.getOutput().getBlock().equals(block)) return false;
        }
        if(block.getType() == Material.CHEST) return true;
        for(BlockFace blockFace : adjFaces){
            if(block.getRelative(blockFace).getType() == Material.CHEST) return true;
        }
        return false;
    }


}
