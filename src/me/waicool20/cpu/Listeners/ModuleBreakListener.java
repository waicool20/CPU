package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPUModule.CPUModule;
import me.waicool20.cpu.ModuleDatabase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class ModuleBreakListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();

        if(block.getType() != Material.CHEST){return;}
        Iterator<CPUModule> iterator = ModuleDatabase.ModuleDatabaseMap.iterator();
        while(iterator.hasNext()){
            CPUModule cpuModule = iterator.next();
            if(cpuModule.isBlockPartOfModule(block)){
                if(player.getName().equalsIgnoreCase(cpuModule.getAttributes().getOwner())){
                    checkModuleIntegrity(block, player);
                }else{
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "[CPU] You are not the owner of this CPU!");
                }
            }
        }


    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(EntityExplodeEvent e){
        for(Block block:e.blockList()){
            checkModuleIntegrity(block,null);
        }
    }

    private static void checkModuleIntegrity(Block block,Player player){
        Iterator<CPUModule> iterator = ModuleDatabase.ModuleDatabaseMap.iterator();
        while(iterator.hasNext()){
            CPUModule cpuModule = iterator.next();
            if(cpuModule.isBlockPartOfModule(block)){
                cpuModule.getType().disable();
                if(cpuModule.isTypified()){
                    cpuModule.getCore().getInventory().clear();
                    cpuModule.getInput1().getInventory().clear();
                    cpuModule.getInput2().getInventory().clear();
                }
                if(player != null) player.sendMessage(ChatColor.RED + "[CPU] You just broke a CPU!");
                ModuleDatabase.removeModule(cpuModule);
            }
        }
    }
}
