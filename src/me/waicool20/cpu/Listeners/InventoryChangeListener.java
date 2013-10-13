package me.waicool20.cpu.Listeners;

import me.waicool20.cpu.CPUModule.CPUModule;
import me.waicool20.cpu.ModuleDatabase;
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
    public void onInventoryInteract(InventoryClickEvent e){
        if(e.getInventory().getType() != InventoryType.CHEST){return;}
        if(!(e.getRawSlot() == e.getSlot())){return;}
        Block block = ((Chest) e.getInventory().getHolder()).getBlock();
        ItemStack[] contents = e.getView().getTopInventory().getContents();
        for(CPUModule cpuModule : ModuleDatabase.ModuleDatabaseMap){
            if(!cpuModule.isBlockPartOfModule(block)){continue;}
            if(Arrays.deepEquals(contents,cpuModule.getCore().getInventory().getContents())){
                cpuModule.getType().disable();
                ModuleDatabase.removeModule(cpuModule);
            }
        }
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK){return;}
        Block block = e.getClickedBlock();
        Player player = e.getPlayer();
        if(block.getType() != Material.CHEST){return;}
        for(CPUModule cpuModule : ModuleDatabase.ModuleDatabaseMap){
            if(cpuModule.getCore().getBlock().equals(block) && cpuModule.isTypified()){
                e.setCancelled(true);
            }
            if(cpuModule.isBlockPartOfModule(block)){
                if(!(player.getName().equalsIgnoreCase(cpuModule.getAttributes().getOwner()))){
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "[CPU] You are not the owner of this Module!");
                }
            }
        }
    }
}
